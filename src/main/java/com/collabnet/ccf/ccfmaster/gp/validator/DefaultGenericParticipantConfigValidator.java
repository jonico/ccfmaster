package com.collabnet.ccf.ccfmaster.gp.validator;

import java.util.List;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class DefaultGenericParticipantConfigValidator extends AbstractGenericParticipantValidator<AbstractGenericParticipantModel>{

	@Override
	public void validate(AbstractGenericParticipantModel model, Errors errors) {
		List<CCFCoreProperty> landscapeConfigList = model.getLandscapeConfigList();
		List<CCFCoreProperty> participantConfigList = model.getParticipantConfigList();
		validateValue(landscapeConfigList, errors,LANDSCAPE_CONFIG_LIST_ELEMENT_NAME);
		validateValue(participantConfigList, errors,PARTICIPANT_CONFIG_LIST_ELEMENT_NAME);
	}

	public ValidationResult validate(AbstractGenericParticipantModel model) {
		return null;
	}

}
