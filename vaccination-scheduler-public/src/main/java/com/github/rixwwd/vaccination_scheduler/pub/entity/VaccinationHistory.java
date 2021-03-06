package com.github.rixwwd.vaccination_scheduler.pub.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "VACCINATION_HISTORIES")
public class VaccinationHistory {

	@Id
	@Column(name = "ID")
	private UUID id;

	@Column(name = "PUBLIC_USER_ID")
	private UUID publicUserId;

	@ManyToOne
	@JoinColumn(name = "PUBLIC_USER_ID", insertable = false, updatable = false)
	private PublicUser publicUser;

	@Column(name = "VACCINATED_AT")
	private LocalDate vaccinatedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "VACCINE")
	private Vaccine vaccine;

	@Column(name = "ROOM_ID")
	private UUID roomId;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(UUID publicUserId) {
		this.publicUserId = publicUserId;
	}

	public PublicUser getPublicUser() {
		return publicUser;
	}

	public void setPublicUser(PublicUser publicUser) {
		this.publicUser = publicUser;
	}

	public LocalDate getVaccinatedAt() {
		return vaccinatedAt;
	}

	public void setVaccinatedAt(LocalDate vaccinatedAt) {
		this.vaccinatedAt = vaccinatedAt;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public UUID getRoomId() {
		return roomId;
	}

	public void setRoomId(UUID roomId) {
		this.roomId = roomId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
