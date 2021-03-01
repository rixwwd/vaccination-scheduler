package com.github.rixwwd.vaccination_scheduler.pub.exception;

public class ReserveFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1193594538184792739L;

	public ReserveFailureException() {

	}

	public ReserveFailureException(String message) {
		super(message);
	}
}
