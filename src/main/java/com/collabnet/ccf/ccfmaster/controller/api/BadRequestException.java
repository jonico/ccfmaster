package com.collabnet.ccf.ccfmaster.controller.api;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }

    BadRequestException() {
        super();
    }

    BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    BadRequestException(Throwable cause) {
        super(cause);
    }

}
