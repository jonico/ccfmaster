package com.collabnet.ccf.ccfmaster.gp.validator;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;

public interface IGenericParticipantConfigItemValidator {

    public static final String PARTICIPANT_CONFIG_LIST_ELEMENT_NAME = "participantConfigList";

    public static final String LANDSCAPE_CONFIG_LIST_ELEMENT_NAME   = "landscapeConfigList";

    void validate(AbstractGenericParticipantModel model, Errors errors);

    ValidationResult validateConnection(AbstractGenericParticipantModel model);

}
