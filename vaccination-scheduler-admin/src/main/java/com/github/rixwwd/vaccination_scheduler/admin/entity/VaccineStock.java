package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "VACCINE_STOCKS")
@EntityListeners(AuditingEntityListener.class)
public class VaccineStock {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private UUID id;

	@NotNull
	@DateTimeFormat(pattern = "uuuu-MM-dd")
	@Column(name = "EXPECTED_DELIVERY_DATE")
	private LocalDate expectedDeliveryDate;

	@Min(1)
	@Column(name = "QUANTITY")
	private int quantity;

	@NotNull
	@Column(name = "ROOM_ID")
	private UUID roomId;

	@ManyToOne
	@JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
	private Room room;

	@Column(name = "RESERVATION_COUNT")
	private int reservationCount;

	@Column(name = "VACCINATED_COUNT")
	private int vaccinatedCount;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "VACCINE")
	private Vaccine vaccine;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "UPDATED_AT")
	private LocalDateTime updatedAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDate getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public UUID getRoomId() {
		return roomId;
	}

	public void setRoomId(UUID roomId) {
		this.roomId = roomId;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getReservationCount() {
		return reservationCount;
	}

	public void setReservationCount(int reservationCount) {
		this.reservationCount = reservationCount;
	}

	public int getVaccinatedCount() {
		return vaccinatedCount;
	}

	public void setVaccinatedCount(int vaccinatedCount) {
		this.vaccinatedCount = vaccinatedCount;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isEnough() {
		return quantity > reservationCount;
	}

	public int incrementReservationCount() {

		return ++this.reservationCount;
	}

	public int decrementReservationCount() {

		return --this.reservationCount;
	}

	public int incrementVaccinatedCount() {

		return ++this.vaccinatedCount;
	}

}
