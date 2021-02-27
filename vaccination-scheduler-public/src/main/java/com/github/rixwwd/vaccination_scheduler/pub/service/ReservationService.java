package com.github.rixwwd.vaccination_scheduler.pub.service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.repository.ReservationRepository;

@Service
public class ReservationService {

	private static final SecureRandom random = new SecureRandom();

	private ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	public Reservation reserve(Reservation reservation) {

		// FIXME 番号の発行アルゴリズム
		reservation.setReservationNumber(String.format("%05d", random.nextInt(100000)));
		var savedReservation = reservationRepository.save(reservation);
		return savedReservation;
	}

	public Optional<Reservation> getReservation(UUID publicUserId) {
		return reservationRepository.findByPublicUserId(publicUserId);
	}
}
