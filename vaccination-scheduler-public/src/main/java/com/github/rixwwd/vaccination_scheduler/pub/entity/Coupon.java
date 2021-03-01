package com.github.rixwwd.vaccination_scheduler.pub.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
public class Coupon {

	@NotBlank
	@Size(max = 255)
	private String coupon;

	public Coupon() {
	}

	public Coupon(String coupon) {
		this.coupon = coupon;
	}

	public String getCoupon() {
		return coupon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coupon == null) ? 0 : coupon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coupon other = (Coupon) obj;
		if (coupon == null) {
			if (other.coupon != null)
				return false;
		} else if (!coupon.equals(other.coupon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return coupon;
	}

}
