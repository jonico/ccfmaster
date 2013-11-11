package com.collabnet.ccf.ccfmaster.gp.web.model;

/**
 * ValidationResult returns the status of connection and provides extra
 * -(success/error) information required
 * 
 * @author kbalaji
 * 
 */
public class ValidationResult {

    private boolean isConnectionValid;

    private String  message;

    public ValidationResult(boolean isConnectionValid) {
        setConnectionValid(isConnectionValid);
    }

    public ValidationResult(boolean isConnectionValid, String message) {
        setConnectionValid(isConnectionValid);
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public boolean isConnectionValid() {
        return isConnectionValid;
    }

    void setConnectionValid(boolean isConnectionValid) {
        this.isConnectionValid = isConnectionValid;
    }

    void setMessage(String message) {
        this.message = message;
    }

}
