package com.collabnet.ccf.core.utils;


/**
 * No-Op implementation of the {@link TFUserHandler} class that ships with
 * CCF core.
 * 
 * We need this to be able to validate stylesheets that reference methods on
 * this class. Ideally, we'd want to remove this class and turn on secure
 * processing for the validation, because currently, users can call arbitrary
 * code in CCFMaster by uploading a malicious stylesheet.
 */

public class TFUserHandler {

	
	public static String searchUser(String userName ,String dummyName){
		throw new UnsupportedOperationException();
	}	
	
}
