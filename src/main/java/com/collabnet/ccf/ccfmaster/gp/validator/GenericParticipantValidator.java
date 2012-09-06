package com.collabnet.ccf.ccfmaster.gp.validator;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.model.AbstractGenericParticipantModel;

public interface GenericParticipantValidator {
	
	public static final String PARTICIPANT_CONFIG_LIST_ELEMENT_NAME = "participantConfigList";
	
	public static final String LANDSCAPE_CONFIG_LIST_ELEMENT_NAME = "landscapeConfigList";
	
	public void validate(AbstractGenericParticipantModel model, Errors errors);
	
	public Boolean validateConnection(AbstractGenericParticipantModel model);

}
