package com.collabnet.ccf.ccfmaster.gp.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;
import com.collabnet.ccf.core.utils.ValidatorUtils;

import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.DEFAULT_ERRORMSG_NOT_EMPTY_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.PARTICIPANT_SHOULD_MATCH_CONDITIONAL_REGEX;

public abstract class AbstractGenericParticipantValidator<T> implements IGenericParticipantValidator<T> {
	
	public abstract ValidationResult validate(T model);
	
	public abstract void validate(T model, Errors errors);
	
	public void validateValue(List<CCFCoreProperty> configProperties, Errors errors,String errorElementName){
		for (int i = 0; i < configProperties.size(); i++) { 
			String value = configProperties.get(i).getValue();
			CCFCorePropertyType type = configProperties.get(i).getType();
			String conditionalRegex = configProperties.get(i).getConditionalRegex();
			if(StringUtils.isEmpty(value)){
				errors.rejectValue(errorElementName+"[" + i + "].value", VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE,DEFAULT_ERRORMSG_NOT_EMPTY_VALUE);
			} else if(!ValidatorUtils.validateType( value, type )){
				errors.rejectValue(errorElementName+"[" + i + "].value",VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC); // Numeric validation message will always be expected
			} else if(!ValidatorUtils.isRegexMatch(conditionalRegex, value)){
				errors.rejectValue(errorElementName+"[" + i + "].value",PARTICIPANT_SHOULD_MATCH_CONDITIONAL_REGEX);
			}
		}
	}
	
}
