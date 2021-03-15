package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccinationHistory;
import com.github.rixwwd.vaccination_scheduler.admin.exception.VaccinatedException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.CouponRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.ReservationRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccinationHistoryRepository;

@Service
public class VaccinationService {

	private VaccinationHistoryRepository vaccinationHistoryRepository;

	private ReservationRepository reservationRepository;

	private CouponRepository couponRepository;

	public VaccinationService(VaccinationHistoryRepository vaccinationHistoryRepository,
			ReservationRepository reservationRepository, CouponRepository couponRepository) {
		this.vaccinationHistoryRepository = vaccinationHistoryRepository;
		this.reservationRepository = reservationRepository;
		this.couponRepository = couponRepository;
	}

	@Transactional
	public VaccinationHistory vaccinate(Reservation reservation) {

		if (reservation.isAccepted()) {
			// すでに受付済みが来るのはおかしい。
			// FIXME 受付済みチェックの排他制御
			throw new VaccinatedException();
		}

		var publicUser = reservation.getPublicUser();

		// ワクチン接種の履歴を記録
		var vaccinationHistory = new VaccinationHistory();
		vaccinationHistory.setPublicUserId(publicUser.getId());
		vaccinationHistory.setPublicUser(reservation.getPublicUser());
		vaccinationHistory.setVaccinatedAt(LocalDate.now());
		var savedHistory = vaccinationHistoryRepository.save(vaccinationHistory);

		// クーポン無効化
		var disabledCoupon = publicUser.getCoupons().stream().filter(c -> c.getCoupon().equals(reservation.getCoupon()))
				.findFirst().orElseThrow();
		disabledCoupon.setUsed(true);
		disabledCoupon.setUsedAt(LocalDateTime.now());
		couponRepository.save(disabledCoupon);

		// 予約無効化
		reservation.setAccepted(true);
		reservationRepository.save(reservation);

		return savedHistory;
	}
}
