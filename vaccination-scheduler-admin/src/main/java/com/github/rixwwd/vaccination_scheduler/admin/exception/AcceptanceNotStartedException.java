package com.github.rixwwd.vaccination_scheduler.admin.exception;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;

public class AcceptanceNotStartedException extends AcceptanceException {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -8349749820401769149L;

	private final Reservation reservation;

	public AcceptanceNotStartedException(Reservation reservation) {
		this.reservation = reservation;
	}

	public Reservation getReservation() {
		return reservation;
	}

}
