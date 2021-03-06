package com.github.rixwwd.vaccination_scheduler.admin.exception;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;

public class DoubleAcceptanceException extends AcceptanceException {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 3576052125283913545L;

	private final Reservation reservation;

	public DoubleAcceptanceException(Reservation reservation) {

		this.reservation = reservation;
	}

	public Reservation getReservation() {
		return reservation;
	}
}
