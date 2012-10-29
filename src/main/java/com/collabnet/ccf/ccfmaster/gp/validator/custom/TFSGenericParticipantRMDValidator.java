package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.validator.AbstractGenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.gp.web.model.ValidationResult;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;

public class TFSGenericParticipantRMDValidator extends AbstractGenericParticipantValidator<RMDModel>{
	
	private String urlKey;
	
	private String userNameKey;
	
	private String passwordKey;
	
	private TFSMetadataHelper tfsMetadataHelper;
	
	public TFSGenericParticipantRMDValidator(String urlKey,String userNameKey,String passwordKey){
		this.urlKey = urlKey;
		this.userNameKey = userNameKey;
		this.passwordKey = passwordKey;
		this.tfsMetadataHelper = new TFSMetadataHelper();
	}
	

	@Override
	public ValidationResult validate(RMDModel model) {
		//Validate the model object and set a global error for given model
		return null;
	}

	@Override
	public void validate(RMDModel model, Errors errors) {
		buildTFSMetadatahelperAttr();
		String collectionName = null;
		List<CCFCoreProperty> configProperties = model.getParticipantSelectorFieldList();
		validateValue(configProperties, errors, RMD_CONFIG_LIST_ELEMENT_NAME);
		for (int i = 0; i < configProperties.size(); i++) { 
			String name = configProperties.get(i).getName();
			if(name.equalsIgnoreCase("tfsCollection")){
				collectionName = configProperties.get(i).getValue();
				boolean isvalid = tfsMetadataHelper.getTFSCollectionList().contains(collectionName);
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","Collection is not Found");				
				}
				continue;
			}
			if(name.equalsIgnoreCase("tfsProjectList")){
				boolean isvalid = tfsMetadataHelper.getTFSProjectList(collectionName).contains(configProperties.get(i).getValue());
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","Project for given collection is not Found");
				}
				continue;
			}
			if(name.equalsIgnoreCase("tfsWorkItemType")){
				boolean isvalid = Arrays.asList(new String[]{"User Story","Task","Bug"}).contains(configProperties.get(i).getValue());
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","Work Item is incorrect");
				}
				continue;
			}
		}
	}
	
	private void buildTFSMetadatahelperAttr(){
		Landscape landscape = ControllerHelper.findLandscape();
		ParticipantConfig urlConfig = ParticipantConfig.findParticipantConfigsByParticipantAndName(landscape.getParticipant(),urlKey).getSingleResult();
		LandscapeConfig userNameConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, userNameKey).getSingleResult();
		LandscapeConfig passwordConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, passwordKey).getSingleResult();
		tfsMetadataHelper.setUrl(urlConfig.getVal());
		String userName = userNameConfig.getVal();
		if(userNameConfig.getVal().contains("\\")){// TODO: currently username got appended with domain name; so split by "/" get the username later
			String userDetails[] = userNameConfig.getVal().split("\\\\");
			userName = userDetails[1];
		}
		tfsMetadataHelper.setUserName(userName); 
		tfsMetadataHelper.setPassword(passwordConfig.getVal());
	}

}
