package com.github.rixwwd.vaccination_scheduler.pub.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateRservationException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidCouponException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.OverCapacityException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.ReserveFailureException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineShortageException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.ReservationRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccineStockRepository;

@Service
public class ReservationService {

	private static final SecureRandom random = new SecureRandom();

	private ReservationRepository reservationRepository;

	private CellRepository cellRepository;

	private VaccineStockRepository vaccineStockRepository;

	public ReservationService(ReservationRepository reservationRepository, CellRepository cellRepository,
			VaccineStockRepository vaccineStockRepository) {
		this.reservationRepository = reservationRepository;
		this.cellRepository = cellRepository;
		this.vaccineStockRepository = vaccineStockRepository;
	}

	@Transactional
	public Reservation reserve(Reservation reservation, PublicUser publicUser) throws ReserveFailureException {

		if (!publicUser.hasCoupon(reservation.getCoupon())) {
			throw new InvalidCouponException("coupon mismatch.");
		}

		// ワクチンと時間枠の確保
		var cell = cellRepository.findByIdForWrite(reservation.getCellId()).orElseThrow();
		var vaccineStocks = vaccineStockRepository
				.findByRoomIdAndGraterThanEqualExpectedDeliveryDateForWrite(cell.getRoomId(), LocalDate.now());

		// ワクチン在庫確認
		if (vaccineStocks.isEmpty()) {
			throw new VaccineShortageException();
		}
		long todayReservationCount = reservationRepository.countTodayReservationByRoomId(cell.getRoomId());
		var stockIsEnough = vaccineStocks.stream().anyMatch(stock -> stock.isEnough(todayReservationCount));
		if (!stockIsEnough) {
			throw new VaccineShortageException();
		}

		// 施設の収容人数確認
		long reservationCount = reservationRepository.countByCellId(cell.getId());
		if (!cell.isEnoughCapacity((int) reservationCount)) {
			throw new OverCapacityException();
		}

		// FIXME 番号の発行アルゴリズム
		reservation.setReservationNumber(String.format("%05d", random.nextInt(100000)));

		try {
			return reservationRepository.saveAndFlush(reservation);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateRservationException();
		}
	}

	public Optional<Reservation> getReservation(UUID publicUserId) {
		return reservationRepository.findByPublicUserId(publicUserId);
	}

	public void cancel(Reservation reservation) {
		reservationRepository.delete(reservation);
	}
}
