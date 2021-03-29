package com.github.rixwwd.vaccination_scheduler.admin.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

class AcceptanceForm {

	@Size(max = 255)
	@NotBlank
	private String reservationNumber;

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

}
