package com.collabnet.ccf.core.utils;

/**
 * No-Op implementation of the {@link StringUtils} class that ships with
 * CCF core.
 * 
 * We need this to be able to validate stylesheets that reference methods on
 * this class. Ideally, we'd want to remove this class and turn on secure
 * processing for the validation, because currently, users can call arbitrary
 * code in CCFMaster by uploading a malicious stylesheet.
 */
public class StringUtils {
	public static char entityToChar(String entity){
		throw new UnsupportedOperationException();
	}
	public static String convertEntities(String text) {
		return text;
	}
	public static String encodeHTMLToEntityReferences(String html) {
		throw new UnsupportedOperationException();
	}
	public static String stripHTML(String text) {
		throw new UnsupportedOperationException();
	}
	public static String stripSpecificDelimiter(String text) {
		throw new UnsupportedOperationException();
	}
	public static String convertHTML(String text) {
		throw new UnsupportedOperationException();
	}
	public static String nullValueToEmptyString(String str) {
		throw new UnsupportedOperationException();
	}
	public static boolean isEmpty(String str) {
		throw new UnsupportedOperationException();
	}
	public static boolean isValidEmailAddress(String emailId){
		throw new UnsupportedOperationException();
	}
	
}
