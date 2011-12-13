package com.collabnet.ccf.ccfmaster.server.core;

/**
 * Root exception to be thrown by strategies in this package when problems occur
 * when managing a CCF core.
 * 
 * Should contain the underlying exception as cause (i.e. IOException,
 * DB-related exceptions)
 */
public class CoreConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CoreConfigurationException() {
		super();
	}

	public CoreConfigurationException(String message) {
		super(message);
	}

	public CoreConfigurationException(Throwable cause) {
		super(cause);
	}

	public CoreConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
