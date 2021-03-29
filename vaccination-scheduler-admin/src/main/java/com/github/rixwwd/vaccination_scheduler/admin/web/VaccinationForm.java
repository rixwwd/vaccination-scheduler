package com.github.rixwwd.vaccination_scheduler.admin.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;

class VaccinationForm {
	@Size(max = 255)
	@NotBlank
	private String reservationNumber;

	@NotNull
	private Vaccine vaccine;

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

}
