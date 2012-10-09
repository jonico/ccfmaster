package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.collabnet.ccf.ccfmaster.gp.validator.DefaultGenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.gp.web.model.ConnectionResult;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.exceptions.TFSUnauthorizedException;

/**
 * Sample TFS validator used to validate GenericParticipant attributes
 * 
 * @author kbalaji
 *
 */
public class SampleTFSGenericParticipantValidator extends DefaultGenericParticipantValidator {

	private static final String CONFIG_ERROR_MSG = "Please check the configuration again";
	private static final String CCF_PARTICIPANT_TFS_URL = "ccf.participant.tfs.url";
	private static final String CCF_LANDSCAPE_TFS_PASSWORD = "ccf.landscape.tfs.password";
	private static final String CCF_LANDSCAPE_TFS_USERNAME = "ccf.landscape.tfs.username";

	@Override
	public ConnectionResult validateConnection(AbstractGenericParticipantModel model) {
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
			return new ConnectionResult(false, CONFIG_ERROR_MSG);
		}else {
			try{
				configurationServer = new TFSTeamProjectCollection(url,  userName, domain, password);
				configurationServer.authenticate();
				return new ConnectionResult(configurationServer.hasAuthenticated());
			} catch(TFSUnauthorizedException e){
				return new ConnectionResult(false, e.getMessage());
			} catch(Exception e){
				return new ConnectionResult(false, e.getMessage());
			}/*finally{
				if(configurationServer != null)
				configurationServer.close();
			}*/
		}
	}
	
}
