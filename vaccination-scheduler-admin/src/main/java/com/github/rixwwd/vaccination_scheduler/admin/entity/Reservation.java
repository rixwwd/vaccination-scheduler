package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "RESERVATIONS")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private UUID id;

	@NotNull
	@Column(name = "CELL_ID")
	private UUID cellId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CELL_ID", insertable = false, updatable = false)
	private Cell cell;

	@Column(name = "PUBLIC_USER_ID")
	private UUID publicUserId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PUBLIC_USER_ID", insertable = false, updatable = false)
	private PublicUser publicUser;

	@NotBlank
	@Size(max = 255)
	@Column(name = "COUPON")
	private String coupon;

	@Column(name = "RESERVATION_NUMBER")
	private String reservationNumber;

	@Column(name = "ACCEPTED")
	private boolean accepted;

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

	public UUID getCellId() {
		return cellId;
	}

	public void setCellId(UUID cellId) {
		this.cellId = cellId;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
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

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public void setReservationNumber(String reservationNumber) {
		this.reservationNumber = reservationNumber;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
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
