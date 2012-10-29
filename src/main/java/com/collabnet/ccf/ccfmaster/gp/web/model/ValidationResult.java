package com.collabnet.ccf.ccfmaster.gp.web.model;

/**
 * ConnectionResult returns the status of connection and provides extra -(success/error) information required 
 * 
 * @author kbalaji
 *
 */
public class ValidationResult {

	private boolean isConnectionValid;
	
	private String message;
	
	public ValidationResult(boolean isConnectionValid){
		setConnectionValid(isConnectionValid);
	}
	
	public ValidationResult(boolean isConnectionValid,String message){
		setConnectionValid(isConnectionValid);
		setMessage(message);
	}
	
	public boolean isConnectionValid() {
		return isConnectionValid;
	}

	public String getMessage() {
		return message;
	}

	void setConnectionValid(boolean isConnectionValid) {
		this.isConnectionValid = isConnectionValid;
	}

	void setMessage(String message) {
		this.message = message;
	}

}
