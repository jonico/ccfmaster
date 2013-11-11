package com.collabnet.ccf.ccfmaster.server.core.update;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;

public class CoreUpdateException extends CoreConfigurationException {

    private static final long serialVersionUID = 1L;

    public CoreUpdateException() {
        super();
    }

    public CoreUpdateException(String string) {
        super(string);
    }

    public CoreUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreUpdateException(Throwable cause) {
        super(cause);
    }

}
