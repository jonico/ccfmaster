package com.collabnet.ccf.ccfmaster.controller.api;

public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	BadRequestException() {
		super();
	}

	BadRequestException(Throwable cause) {
		super(cause);
	}

	public BadRequestException(String message) {
		super(message);
	}

	BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
