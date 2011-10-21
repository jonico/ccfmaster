package com.collabnet.ccf.ccfmaster.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.web.model.TFSettingsModel;

public class TFSettingsValidator implements Validator {



	@Override
	public boolean supports(Class clazz) {
		return TFSettingsModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TFSettingsModel tfSettingsModel=(TFSettingsModel)target;
		if(tfSettingsModel.getTfUserNameLandscapeConfig().getVal()==""){
			errors.rejectValue("tfUserNameLandscapeConfig.val",ControllerConstants.ERROR_TFUSERNAME_VALIDATE);
		}
		if(tfSettingsModel.getTfPasswordLandscapeConfig().getVal()==""){
			errors.rejectValue("tfPasswordLandscapeConfig.val",ControllerConstants.ERROR_TFPASSWORD_VALIDATE);
		}



	}

}
