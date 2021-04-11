package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccinationHistory;
import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;
import com.github.rixwwd.vaccination_scheduler.admin.exception.DoubleVaccinationException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NoAcceptanceException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.VaccinatedException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.VaccineMismatchException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccineStockRepository;

@Service
public class VaccinationService {

	private static final Logger logger = LoggerFactory.getLogger(VaccinationService.class);

	private final PublicUserRepository publicUserRepository;
	private final VaccineStockRepository vaccineStockRepository;

	public VaccinationService(VaccineStockRepository vaccineStockRepository,
			PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
		this.vaccineStockRepository = vaccineStockRepository;
	}

	@Transactional
	public VaccinationHistory vaccinate(Reservation reservation, Vaccine vaccine) throws VaccinatedException {

		if (!reservation.isAccepted()) {
			// 受付してない人が来るのはおかしい
			logger.error("受付が済んでない人が接種されました。予約番号=" + reservation.getReservationNumber());
			throw new NoAcceptanceException();
		}

		if (reservation.isVaccinated()) {
			// 接種済みの人が来るのはおかしい
			logger.error("同日に2回接種しました。予約番号=" + reservation.getReservationNumber());
			throw new DoubleVaccinationException();
		}

		var publicUser = reservation.getPublicUser();

		var history = publicUser.getFirstVaccinationHistory();

		if (!history.isEmpty() && history.get().getVaccine() != vaccine) {
			logger.error("1回目と異なるワクチンを接種しました。予約番号=" + reservation.getReservationNumber());
			// FIXME 受付でブロックすべき
			throw new VaccineMismatchException();
		}

		// ワクチン接種の履歴を記録
		var vaccinationHistory = new VaccinationHistory();
		vaccinationHistory.setPublicUserId(publicUser.getId());
		vaccinationHistory.setPublicUser(reservation.getPublicUser());
		vaccinationHistory.setVaccinatedAt(LocalDate.now());
		vaccinationHistory.setVaccine(vaccine);
		vaccinationHistory.setRoomId(reservation.getCell().getRoomId());
		publicUser.getVaccinationHistories().add(vaccinationHistory);
		publicUser.getAcceptedReservation().setVaccinated(true);

		// クーポン無効化
		var disabledCoupon = publicUser.getCoupons().stream().filter(c -> c.getCoupon().equals(reservation.getCoupon()))
				.findFirst().orElseThrow();
		disabledCoupon.setUsed(true);
		disabledCoupon.setUsedAt(LocalDateTime.now());

		publicUserRepository.save(publicUser);

		// ワクチン在庫
		var vaccineStocks = vaccineStockRepository.findUnusedStockForWrite(reservation.getCell().getRoomId(),
				reservation.getCell().getBeginTime().toLocalDate());

		var vaccineStock = vaccineStocks.stream().sorted((a, b) -> {
			// 配送予定日・作成日時の順でソート➙古いのから割り当てる
			if (a.getExpectedDeliveryDate().isEqual(b.getExpectedDeliveryDate())) {
				return a.getCreatedAt().compareTo(b.getCreatedAt());
			}
			return a.getExpectedDeliveryDate().compareTo(b.getExpectedDeliveryDate());
		}).findFirst().orElseThrow();

		vaccineStock.incrementVaccinatedCount();
		vaccineStockRepository.save(vaccineStock);

		return vaccinationHistory;
	}
}
