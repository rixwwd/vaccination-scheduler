package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("RESERVATIONS")
public class Reservation {
	@Id
	private UUID id;

	@NotNull
	private UUID cellId;

	@NotNull
	private UUID personId;

	@NotBlank
	@Size(max = 255)
	private String reservatonNumber;

	@CreatedDate
	private LocalDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getCellId() {
		return cellId;
	}

	public void setCellId(UUID cellId) {
		this.cellId = cellId;
	}

	public UUID getPersonId() {
		return personId;
	}

	public void setPersonId(UUID personId) {
		this.personId = personId;
	}

	public String getReservatonNumber() {
		return reservatonNumber;
	}

	public void setReservatonNumber(String reservatonNumber) {
		this.reservatonNumber = reservatonNumber;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
