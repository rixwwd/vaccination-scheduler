package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.admin.exception.AcceptanceException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.AcceptanceNotStartedException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.DoubleAcceptanceException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.ReservationNotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.ReservationRepository;

@Service
public class AcceptanceService {

	private CellRepository cellRepository;

	private ReservationRepository reservationRepository;

	public AcceptanceService(CellRepository cellRepository, ReservationRepository reservationRepository) {
		this.cellRepository = cellRepository;
		this.reservationRepository = reservationRepository;
	}

	@Transactional
	public Reservation accept(String reservationNumber) throws AcceptanceException {

		var reservation = reservationRepository.findByReservationNumber(reservationNumber)
				.orElseThrow(ReservationNotFoundException::new);

		if (reservation.isAccepted()) {
			throw new DoubleAcceptanceException(reservation);
		}

		if (LocalDateTime.now().isBefore(reservation.getCell().getBeginTime().minusMinutes(15))) {
			throw new AcceptanceNotStartedException(reservation);
		}

		reservation.setAccepted(true);

		var cell = cellRepository.findByIdForWrite(reservation.getCellId()).orElseThrow();
		cell.incrementAcceptedCount();
		cellRepository.save(cell);

		return reservationRepository.save(reservation);
	}

}
