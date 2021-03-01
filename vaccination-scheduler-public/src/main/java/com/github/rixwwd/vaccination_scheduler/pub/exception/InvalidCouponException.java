package com.github.rixwwd.vaccination_scheduler.pub.exception;

public class InvalidCouponException extends ReserveFailureException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3061538835538060634L;

	public InvalidCouponException(String message) {
		super(message);
	}

}
