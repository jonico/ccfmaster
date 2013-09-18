package com.collabnet.ccf.ccfmaster.web.validator;

import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.DEFAULT_ERRORMSG_NOT_EMPTY_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;
import com.collabnet.ccf.core.utils.ValidatorUtils;

public class ConfigValidator implements Validator {

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
			} else if(!ValidatorUtils.validateType( value, type )){
				errors.rejectValue("ccfCoreProperties[" + i + "].value",VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC); // Numeric validation message will always be expected
			}
		}  

	}
	
	

}
