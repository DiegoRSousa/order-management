package com.diego.order.exception;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException(Long id, String clazz) {
		super("Object not found!" + " Id: " + id + ", Tipo: " + clazz);
	}
}