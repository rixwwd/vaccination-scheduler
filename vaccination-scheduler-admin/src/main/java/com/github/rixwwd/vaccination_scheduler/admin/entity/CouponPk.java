package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CouponPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1321113818969210811L;

	@Column(name = "PUBLIC_USER_ID")
	private UUID publicUserId;

	@Column(name = "COUPON")
	private String coupon;

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
}
