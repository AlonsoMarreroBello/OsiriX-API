package com.osirix.api.exception;

public class ResourceAlreadyExistingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistingException(String message) {
		super(message);		
	}
	
	public ResourceAlreadyExistingException(String message, Throwable cause) {
		super(message, cause);		
	}

}