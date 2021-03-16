package com.github.rixwwd.vaccination_scheduler.pub.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "CELLS")
public class Cell {

	@Id
	@Column(name = "ID")
	private UUID id;

	@NotNull
	@Column(name = "ROOM_ID")
	private UUID roomId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
	private Room room;

	@DateTimeFormat(pattern = "uuuu-MM-dd HH:mm")
	@Column(name = "BEGIN_TIME")
	@JsonFormat(pattern = "uuuu-MM-dd HH:mm")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime beginTime;

	@DateTimeFormat(pattern = "uuuu-MM-dd HH:mm")
	@Column(name = "END_TIME")
	@JsonFormat(pattern = "uuuu-MM-dd HH:mm")
	private LocalDateTime endTime;

	@Min(1)
	@Column(name = "CAPACITY")
	private int capacity;

	@Column(name = "RESERVATION_COUNT")
	private int reservationCount;

	@Column(name = "ACCEPTED_COUNT")
	private int acceptedCount;

	@CreatedDate
	@Column(name = "CREATED_AT")
	@JsonIgnore
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "UPDATED_AT")
	@JsonIgnore
	private LocalDateTime updatedAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public LocalDateTime getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(LocalDateTime beginTime) {
		this.beginTime = beginTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getReservationCount() {
		return reservationCount;
	}

	public void setReservationCount(int reservationCount) {
		this.reservationCount = reservationCount;
	}

	public int getAcceptedCount() {
		return acceptedCount;
	}

	public void setAcceptedCount(int acceptedCount) {
		this.acceptedCount = acceptedCount;
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

	public boolean isEnoughCapacity() {
		return reservationCount < capacity;
	}

	public int incrementReservationCount() {

		return ++this.reservationCount;
	}

	public int decrementReservationCount() {

		return --this.reservationCount;
	}

}
