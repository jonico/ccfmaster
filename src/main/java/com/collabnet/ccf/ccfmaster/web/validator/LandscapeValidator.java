package com.collabnet.ccf.ccfmaster.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.model.LandscapeModel;

public class LandscapeValidator implements Validator {

	

	@Override
	public boolean supports(Class clazz) {
		return LandscapeModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LandscapeModel landscapeModel=(LandscapeModel)target;
		
			if(landscapeModel.getParticipantUserNameLandscapeConfig().getVal()==""){
				errors.rejectValue("participantUserNameLandscapeConfig.val",ControllerConstants.ERROR_PARTICIPANTPASSWORD_VALIDATE);
			}	
			if(landscapeModel.getTfUserNameLandscapeConfig().getVal()==""){
				errors.rejectValue("tfUserNameLandscapeConfig.val",ControllerConstants.ERROR_TFUSERNAME_VALIDATE);
			}	
			if(landscapeModel.getTfUserNameLandscapeConfig().getVal()==""){
				errors.rejectValue("tfPasswordLandscapeConfig.val",ControllerConstants.ERROR_TFPASSWORD_VALIDATE);
			}	
			
			if(landscapeModel.getParticipant().getSystemKind().equals(SystemKind.QC)){
				if(!landscapeModel.getParticipantUrlParticipantConfig().getVal().endsWith(ControllerConstants.VALIDATEQCURL)){
					errors.rejectValue("participantUrlParticipantConfig.val", ControllerConstants.ERROR_QCURL_VALIDATE);
				}
			}
			if(landscapeModel.getParticipant().getSystemKind().equals(SystemKind.SWP)){
				if(landscapeModel.getParticipantPasswordLandscapeConfig().getVal()==""){
					errors.rejectValue("participantPasswordLandscapeConfig.val",ControllerConstants.ERROR_SWPPASSWORD_VALIDATE);
				}
				if(landscapeModel.getParticipantResyncUserNameLandscapeConfig().getVal()==""){
					errors.rejectValue("participantResyncUserNameLandscapeConfig.val",ControllerConstants.ERROR_SWPRESYNCUSERNAME_VALIDATE);
				}
				if(landscapeModel.getParticipantResyncPasswordLandscapeConfig().getVal()==""){
					errors.rejectValue("participantResyncPasswordLandscapeConfig.val",ControllerConstants.ERROR_SWPRESYNCPASSWORD_VALIDATE);
				}
				if(!landscapeModel.getParticipantUrlParticipantConfig().getVal().endsWith(ControllerConstants.VALIDATESWPURL)){
					errors.rejectValue("participantUrlParticipantConfig.val", ControllerConstants.ERROR_SWPURL_VALIDATE);
				}	
		}
		
	}

}
