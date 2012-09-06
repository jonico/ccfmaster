package com.collabnet.ccf.core.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;

public class ValidatorUtils {
	
	private static final String NUMERIC_PATTERN = "^\\d*$";
	
	private static final String BOOLEAN_PATTERN = "^true|false$";

	
	public static boolean validateType(String value, CCFCorePropertyType coreType) {
		boolean isValid = true;
		switch (coreType) {
			case INTEGER:
				isValid = isValueNumeric(value);
				break;
			case BOOLEAN:
				isValid = isValueBoolean(value);
				break;
			default:
				isValid = true; // Assumption default type is String
		}
		return isValid;
	}
	
	public static boolean isRegexMatch(String regex, String value){
		if(StringUtils.isEmpty(regex)) return true;
		return Pattern.matches(regex, value);
	}
	
	private static boolean isValueNumeric(String value){
		return isRegexMatch(NUMERIC_PATTERN, value);
	}

	private static boolean isValueBoolean(String value) {
		return isRegexMatch(BOOLEAN_PATTERN, value);
	}
}
