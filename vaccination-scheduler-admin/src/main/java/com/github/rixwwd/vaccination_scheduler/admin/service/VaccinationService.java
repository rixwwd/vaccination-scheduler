package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccinationHistory;
import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;
import com.github.rixwwd.vaccination_scheduler.admin.exception.VaccinatedException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.CouponRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccinationHistoryRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccineStockRepository;

@Service
public class VaccinationService {

	private VaccinationHistoryRepository vaccinationHistoryRepository;

	private CouponRepository couponRepository;

	private VaccineStockRepository vaccineStockRepository;

	public VaccinationService(VaccinationHistoryRepository vaccinationHistoryRepository,
			CouponRepository couponRepository, VaccineStockRepository vaccineStockRepository) {
		this.vaccinationHistoryRepository = vaccinationHistoryRepository;
		this.couponRepository = couponRepository;
		this.vaccineStockRepository = vaccineStockRepository;
	}

	@Transactional
	public VaccinationHistory vaccinate(Reservation reservation, Vaccine vaccine) {

		if (!reservation.isAccepted()) {
			// 受付してない人が来るのはおかしい
			throw new VaccinatedException();
		}

		var publicUser = reservation.getPublicUser();

		// ワクチン接種の履歴を記録
		var vaccinationHistory = new VaccinationHistory();
		vaccinationHistory.setPublicUserId(publicUser.getId());
		vaccinationHistory.setPublicUser(reservation.getPublicUser());
		vaccinationHistory.setVaccinatedAt(LocalDate.now());
		vaccinationHistory.setVaccine(vaccine);
		var savedHistory = vaccinationHistoryRepository.save(vaccinationHistory);

		// クーポン無効化
		var disabledCoupon = publicUser.getCoupons().stream().filter(c -> c.getCoupon().equals(reservation.getCoupon()))
				.findFirst().orElseThrow();
		disabledCoupon.setUsed(true);
		disabledCoupon.setUsedAt(LocalDateTime.now());
		couponRepository.save(disabledCoupon);

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

		return savedHistory;
	}
}
