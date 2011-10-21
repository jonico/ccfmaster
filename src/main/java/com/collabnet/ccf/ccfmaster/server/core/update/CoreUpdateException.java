package com.collabnet.ccf.ccfmaster.server.core.update;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;

public class CoreUpdateException extends CoreConfigurationException {

	public CoreUpdateException() {
		super();
	}

	public CoreUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CoreUpdateException(Throwable cause) {
		super(cause);
	}

	public CoreUpdateException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 1L;

}
