package com.collabnet.ccf.ccfmaster.web.validator;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.*;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;

public class ConfigValidator implements Validator {
	
	private static final String NUMERIC_PATTERN = "^\\d*$";
	
	private static final String BOOLEAN_PATTERN = "^true|false$";

	@Override
	public boolean supports(Class<?> clazz) {
		return CCFCorePropertyList.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CCFCorePropertyList ccfCoreProperties=(CCFCorePropertyList)target;
		List<CCFCoreProperty> ccfPropertyList =ccfCoreProperties.getCcfCoreProperties();
		for (int i = 0; i < ccfPropertyList.size(); i++) { 
			String value = ccfPropertyList.get(i).getValue();
			CCFCorePropertyType type = ccfPropertyList.get(i).getType();
			if (StringUtils.isEmpty(value)) {
				errors.rejectValue("ccfCoreProperties[" + i + "].value", VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE, DEFAULT_ERRORMSG_NOT_EMPTY_VALUE);
			} else if(!validate( value, type )){
				errors.rejectValue("ccfCoreProperties[" + i + "].value",VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC); // Numeric validation message will always be expected
			}
		}  

	}
	
	private static boolean validate(String value, CCFCorePropertyType coreType) {
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
	
	private static boolean isValueNumeric(String value){
		return Pattern.matches(NUMERIC_PATTERN, value);
	}

	private static boolean isValueBoolean(String value) {
		return Pattern.matches(BOOLEAN_PATTERN, value);
	}

}
