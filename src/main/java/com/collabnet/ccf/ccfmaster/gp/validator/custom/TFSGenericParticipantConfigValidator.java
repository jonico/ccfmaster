package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.DEFAULT_ERRORMSG_NOT_EMPTY_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.PARTICIPANT_SHOULD_MATCH_CONDITIONAL_REGEX;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE;
import static com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants.VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantConfigItemValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;
import com.collabnet.ccf.core.utils.ValidatorUtils;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.exceptions.TFSUnauthorizedException;

/**
 * Sample TFS validator used to validate GenericParticipant attributes
 * 
 * @author kbalaji
 *
 */
public class TFSGenericParticipantConfigValidator implements IGenericParticipantConfigItemValidator {

	private static final String CONFIG_ERROR_MSG = "Please check the configuration again";
	private static final String CCF_PARTICIPANT_TFS_URL = "ccf.participant.tfs.url";
	private static final String CCF_LANDSCAPE_TFS_PASSWORD = "ccf.landscape.tfs.password";
	private static final String CCF_LANDSCAPE_TFS_USERNAME = "ccf.landscape.tfs.username";
	
	
	public void validateValue(List<CCFCoreProperty> configProperties, Errors errors, String errorElementName) {		
		for (int i = 0; i < configProperties.size(); i++) { 
			String value = configProperties.get(i).getValue();
			CCFCorePropertyType type = configProperties.get(i).getType();
			String conditionalRegex = configProperties.get(i).getConditionalRegex();
			if(StringUtils.isEmpty(value)){
				errors.rejectValue(errorElementName+"[" + i + "].value", VALIDATE_NOT_EMPTY_CCFCOREPROPERTIES_VALUE,DEFAULT_ERRORMSG_NOT_EMPTY_VALUE);
			} else if(!ValidatorUtils.validateType( value, type )){
				errors.rejectValue(errorElementName+"[" + i + "].value",VALIDATE_TYPE_MIS_MATCH_CCFCOREPROPERTIES_NUMERIC); // Numeric validation message will always be expected
			} else if(!ValidatorUtils.findMatch(conditionalRegex, value)){
				errors.rejectValue(errorElementName+"[" + i + "].value",PARTICIPANT_SHOULD_MATCH_CONDITIONAL_REGEX);
			}
		}
	}

	@Override
	public ValidationResult validateConnection(AbstractGenericParticipantModel model) {
		TFSTeamProjectCollection configurationServer = null;
		String userName = null, domain = null, url = null, password = null;
		List<CCFCoreProperty> landscapeConfigList = model.getLandscapeConfigList();
		List<CCFCoreProperty> partcipantConfigList = model.getParticipantConfigList();
		for(CCFCoreProperty property : landscapeConfigList ){
			if(property.getName().equals(CCF_LANDSCAPE_TFS_USERNAME)){
				String[] name = property.getValue().split("\\\\");
				if(name.length >1){
					domain = name[0];
					userName = name[1];
				}else {
					userName = name[0];
				}
			}
			if(property.getName().equals(CCF_LANDSCAPE_TFS_PASSWORD)){
				password= property.getValue();
			}
		}
		for(CCFCoreProperty property : partcipantConfigList ){
			if(property.getName().equals(CCF_PARTICIPANT_TFS_URL)){
				url = property.getValue();
			}
		}
		
		if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(url)){
			return new ValidationResult(false, CONFIG_ERROR_MSG);
		}else {
			try{
				configurationServer = new TFSTeamProjectCollection(url,  userName, domain, password);
				configurationServer.authenticate();
				return new ValidationResult(configurationServer.hasAuthenticated());
			} catch(TFSUnauthorizedException e){
				return new ValidationResult(false, e.getMessage());
			} catch(Exception e){
				return new ValidationResult(false, e.getMessage());
			}/*finally{
				if(configurationServer != null)
				configurationServer.close();
			}*/
		}
	}

	@Override
	public void validate(AbstractGenericParticipantModel model, Errors errors) {
		// NOTE: this implementation can be customized as per third party participant implementation
		List<CCFCoreProperty> landscapeConfigList = model.getLandscapeConfigList();
		List<CCFCoreProperty> participantConfigList = model.getParticipantConfigList();
		validateValue(landscapeConfigList, errors,LANDSCAPE_CONFIG_LIST_ELEMENT_NAME);
		validateValue(participantConfigList, errors,PARTICIPANT_CONFIG_LIST_ELEMENT_NAME);
	}
	
}
