package com.collabnet.ccf.ccfmaster.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;

public class ParticipantSettingsValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantSettingsModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ParticipantSettingsModel participantSettingsModel=(ParticipantSettingsModel)target;
		SystemKind participantKind = participantSettingsModel.getParticipant().getSystemKind();
		if(participantKind.equals(SystemKind.QC)||participantKind.equals(SystemKind.SWP)){
			if(participantSettingsModel.getParticipantUserNameLandscapeConfig().getVal()==""){
				errors.rejectValue("participantUserNameLandscapeConfig.val",ControllerConstants.ERROR_PARTICIPANTPASSWORD_VALIDATE);
			}	
		}
		if(participantKind.equals(SystemKind.QC)){
			if(!participantSettingsModel.getParticipantUrlParticipantConfig().getVal().endsWith(ControllerConstants.VALIDATEQCURL)){
				errors.rejectValue("participantUrlParticipantConfig.val", ControllerConstants.ERROR_QCURL_VALIDATE);
			}
		}
		if(participantKind.equals(SystemKind.SWP)){
			if(participantSettingsModel.getParticipantPasswordLandscapeConfig().getVal()==""){
				errors.rejectValue("participantPasswordLandscapeConfig.val",ControllerConstants.ERROR_SWPPASSWORD_VALIDATE);
			}
			if(participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getVal()==""){
				errors.rejectValue("participantResyncUserNameLandscapeConfig.val",ControllerConstants.ERROR_SWPRESYNCUSERNAME_VALIDATE);
			}
			if(participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getVal()==""){
				errors.rejectValue("participantResyncPasswordLandscapeConfig.val",ControllerConstants.ERROR_SWPRESYNCPASSWORD_VALIDATE);
			}
			if(!participantSettingsModel.getParticipantUrlParticipantConfig().getVal().endsWith(ControllerConstants.VALIDATESWPURL)){
				errors.rejectValue("participantUrlParticipantConfig.val", ControllerConstants.ERROR_SWPURL_VALIDATE);
			}	
		}

	}

}
