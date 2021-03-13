package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "COUPONS")
@IdClass(CouponPk.class)
@EntityListeners(AuditingEntityListener.class)
public class Coupon implements Serializable {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -3293561959180606196L;

	@Id
	@Column(name = "PUBLIC_USER_ID")
	private UUID publicUserId;

	@Id
	@NotBlank
	@Size(max = 255)
	@Column(name = "COUPON")
	private String coupon;

	@NotBlank
	@Size(max = 255)
	@Column(name = "NAME")
	private String name;

	@Column(name = "USED")
	private boolean used;

	@Column(name = "USED_AT")
	private LocalDateTime usedAt;

	@Column(name = "CREATED_AT", updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(name = "UPDATED_AT")
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public UUID getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(UUID publicUserId) {
		this.publicUserId = publicUserId;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public LocalDateTime getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(LocalDateTime usedAt) {
		this.usedAt = usedAt;
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
