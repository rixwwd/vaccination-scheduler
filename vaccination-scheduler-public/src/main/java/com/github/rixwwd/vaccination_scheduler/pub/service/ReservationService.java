package com.github.rixwwd.vaccination_scheduler.pub.service;

import java.security.SecureRandom;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccineStock;
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateRservationException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidCouponException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.OverCapacityException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.ReserveFailureException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccinationTooEarlyException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineMismatchException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineShortageException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.ReservationRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccineStockRepository;

@Service
public class ReservationService {

	private static final SecureRandom random = new SecureRandom();

	private final ReservationRepository reservationRepository;

	private final CellRepository cellRepository;

	private final VaccineStockRepository vaccineStockRepository;

	private final CancelNoticeService cancelNoticeService;

	private final PublicUserRepository publicUserRepository;

	public ReservationService(ReservationRepository reservationRepository, CellRepository cellRepository,
			VaccineStockRepository vaccineStockRepository, CancelNoticeService cancelNoticeService,
			PublicUserRepository publicUserRepository) {
		this.reservationRepository = reservationRepository;
		this.cellRepository = cellRepository;
		this.vaccineStockRepository = vaccineStockRepository;
		this.cancelNoticeService = cancelNoticeService;
		this.publicUserRepository = publicUserRepository;
	}

	@Transactional
	public void reserve(Reservation reservation, PublicUser publicUser) throws ReserveFailureException {

		if (!publicUser.hasCoupon(reservation.getCoupon())) {
			throw new InvalidCouponException("coupon mismatch.");
		}

		// ワクチンと時間枠の確保
		var cell = cellRepository.findByIdForWrite(reservation.getCellId()).orElseThrow();
		var vaccineStocks = vaccineStockRepository.findInStockForWrite(cell.getRoomId(),
				cell.getBeginTime().toLocalDate());

		if (cell.isStarted()) {
			throw new VaccinationTimePastException();
		}

		validateConsistensyForSecondDoses(publicUser, cell);

		var vaccineStock = vaccineStocks.stream().filter(VaccineStock::isEnough).sorted((a, b) -> {
			// 配送予定日・作成日時の順でソート➙古いのから割り当てる
			if (a.getExpectedDeliveryDate().isEqual(b.getExpectedDeliveryDate())) {
				return a.getCreatedAt().compareTo(b.getCreatedAt());
			}
			return a.getExpectedDeliveryDate().compareTo(b.getExpectedDeliveryDate());
		}).findFirst().orElseThrow(VaccineShortageException::new);

		vaccineStock.incrementReservationCount();
		vaccineStockRepository.save(vaccineStock);

		// 施設の収容人数確認
		if (!cell.isEnoughCapacity()) {
			throw new OverCapacityException();
		}
		// 整合性チェック(大丈夫だとは思うけど)
		long reservationCount = reservationRepository.countByCellId(cell.getId());
		if (reservationCount != cell.getReservationCount()) {
			throw new RuntimeException("reservation count mismatch!");
		}
		cell.incrementReservationCount();
		cellRepository.save(cell);

		// FIXME 番号の発行アルゴリズム
		reservation.setReservationNumber(String.format("%05d", random.nextInt(100000)));
		publicUser.getReservations().add(reservation);

		publicUser.getWaitingList().clear();

		try {
			publicUserRepository.saveAndFlush(publicUser);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateRservationException();
		}

	}

	void validateConsistensyForSecondDoses(PublicUser publicUser, Cell cell) throws ReserveFailureException {

		// １回目を接種しているならその時と整合性を確認する。
		var firstTime = publicUser.getFirstVaccinationHistory();
		if (firstTime.isEmpty()) {
			return;
		}

		// ワクチンの種類
		if (cell.getRoom().getVaccine() != firstTime.get().getVaccine()) {
			throw new VaccineMismatchException();
		}

		// 接種間隔
		if (cell.getBeginTime().toLocalDate().isBefore(firstTime.get().getVaccinatedAt())) {
			throw new VaccinationTooEarlyException();
		}

	}

	@Transactional
	public void cancel(PublicUser publicUser) {

		var reservation = publicUser.getReservation();
		var cell = cellRepository.findByIdForWrite(reservation.getCellId()).orElseThrow();

		cell.decrementReservationCount();
		cellRepository.save(cell);

		var vaccineStocks = vaccineStockRepository.findCancelableForWrite(cell.getRoomId(),
				cell.getBeginTime().toLocalDate());
		var vaccineStock = vaccineStocks.stream().sorted((a, b) -> {
			// 配送予定日・作成日時の順の逆でソート➙新しいのからキャンセルする
			if (b.getExpectedDeliveryDate().isEqual(a.getExpectedDeliveryDate())) {
				return b.getCreatedAt().compareTo(a.getCreatedAt());
			}
			return b.getExpectedDeliveryDate().compareTo(a.getExpectedDeliveryDate());
		}).findFirst().orElseThrow(VaccineShortageException::new);

		vaccineStock.decrementReservationCount();
		vaccineStockRepository.save(vaccineStock);

		publicUser.cancelReservation();
		publicUserRepository.save(publicUser);

		cancelNoticeService.notifyCancel(reservation.getCell());
	}
}
