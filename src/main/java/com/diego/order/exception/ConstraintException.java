package com.diego.order.exception;

public class ConstraintException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConstraintException(String message) {
		super(message);
	}
}
