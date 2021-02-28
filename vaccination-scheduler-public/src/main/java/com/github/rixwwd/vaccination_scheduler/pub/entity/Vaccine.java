package com.github.rixwwd.vaccination_scheduler.pub.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "VACCINES")
public class Vaccine {

	@Id
	@Column(name = "ID")
	private UUID id;

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

}
