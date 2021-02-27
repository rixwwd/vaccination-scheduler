package com.github.rixwwd.vaccination_scheduler.pub.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("VACCINATION_HISTORIES")
public class VaccinationHistory {
	@Id
	private UUID id;

	private UUID userId;

	private LocalDate vaccinatedAt;

	@CreatedDate
	private LocalDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDate getVaccinatedAt() {
		return vaccinatedAt;
	}

	public void setVaccinatedAt(LocalDate vaccinatedAt) {
		this.vaccinatedAt = vaccinatedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
