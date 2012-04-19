package com.collabnet.ccf.ccfmaster.web.validator;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;

public class ConfigValidator implements Validator {
	
	private static final String NUMERIC_PATTERN = "^\\d*$";
	
	private static final String BOOLEAN_PATTERN = "^true|false$";
	
	public enum DataType{
		INTEGER("Integer"),LONG("Long"),BOOLEAN("boolean"),STRING("String");
		
		private String type;

		public String getType() {
			return type;
		}

		DataType(String type) {
			this.type = type;
		}
	}

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
			String type = ccfPropertyList.get(i).getType();
			if (StringUtils.isEmpty(value)) {
				errors.rejectValue("ccfCoreProperties[" + i + "].value","NotEmpty.ccfcoreproperties[0].value","Blank value not accepted");
			} else if(!validate( value, type )){
				errors.rejectValue("ccfCoreProperties[" + i + "].value","TypeMisMatch.ccfcoreproperties.numeric"); // Numeric validation message will always be expected
			}
		}  

	}
	
	private static boolean validate(String value, String type) {
		boolean isValid = true;
		for (DataType coreType : DataType.values()) {
			if (coreType.getType().equalsIgnoreCase(type)) {
				switch (coreType) {
					case INTEGER:
					case LONG:
						isValid = isValueNumeric(value);
						break;
					case BOOLEAN:
						isValid = isValueBoolean(value);
						break;
					default:
						isValid = true; // Assumption default type is String
				}
				break;
			}
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
