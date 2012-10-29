package com.collabnet.ccf.ccfmaster.gp.validator;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;

public interface IGenericParticipantValidator<T> {
	
	public static final String PARTICIPANT_CONFIG_LIST_ELEMENT_NAME = "participantConfigList";
	
	public static final String LANDSCAPE_CONFIG_LIST_ELEMENT_NAME = "landscapeConfigList";
	
	public static final String RMD_CONFIG_LIST_ELEMENT_NAME = "participantSelectorFieldList";
	
	void validate(T model, Errors errors);
	
	ValidationResult validate(T model);

}
