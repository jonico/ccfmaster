package com.collabnet.ccf.ccfmaster.gp.validator;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;

public interface IGenericParticipantRMDValidator {

    public static final String RMD_CONFIG_LIST_ELEMENT_NAME = "participantSelectorFieldList";

    void validate(RMDModel model, Errors errors);
}
