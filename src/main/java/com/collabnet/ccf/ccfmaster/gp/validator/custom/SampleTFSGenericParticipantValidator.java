package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import com.collabnet.ccf.ccfmaster.gp.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.gp.validator.DefaultGenericParticipantValidator;

/**
 * Sample TFS validator used to validate GenericParticipant attributes
 * 
 * @author kbalaji
 *
 */
public class SampleTFSGenericParticipantValidator extends DefaultGenericParticipantValidator {

	@Override
	public Boolean validateConnection(AbstractGenericParticipantModel model) {
		return true;
	}
	
}
