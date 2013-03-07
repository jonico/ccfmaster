package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;

import com.collabnet.ccf.ccfmaster.gp.validator.ConfigItemValidatorUtils;
import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantRMDValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class TFSGenericParticipantRMDValidator implements IGenericParticipantRMDValidator{
	
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
	public void validate(RMDModel model, Errors errors) {
		buildTFSMetadatahelperAttr(model);
		String collectionName = null;
		List<CCFCoreProperty> configProperties = model.getParticipantSelectorFieldList();
		ConfigItemValidatorUtils.validateValue(configProperties, errors, RMD_CONFIG_LIST_ELEMENT_NAME);
		for (int i = 0; i < configProperties.size(); i++) { 
			String name = configProperties.get(i).getName();
			if(name.equalsIgnoreCase("tfsCollection")){
				collectionName = configProperties.get(i).getValue();
				boolean isvalid = tfsMetadataHelper.getTFSCollectionList().contains(collectionName);
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","Collection is not found");				
				}
				continue;
			}
			if(name.equalsIgnoreCase("tfsProjectList")){
				boolean isvalid = tfsMetadataHelper.getTFSProjectList(collectionName).contains(configProperties.get(i).getValue());
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","Project for given collection is not found");
				}
				continue;
			}
			if(name.equalsIgnoreCase("tfsWorkItemType")){
				boolean isvalid = Arrays.asList(new String[]{"User Story","Task","Bug"}).contains(configProperties.get(i).getValue());
				if(!isvalid){
					errors.rejectValue(RMD_CONFIG_LIST_ELEMENT_NAME+"[" + i + "].value","","WorkItem for given project is not found");
				}
				continue;
			}
		}
	}
	
	private void buildTFSMetadatahelperAttr(RMDModel model){
		tfsMetadataHelper.setUrl(model.getParticipantConfigMap().get(urlKey));
		String userName = model.getLandscapeConfigMap().get(userNameKey);
		if(userName.contains("\\")){
			String userDetails[] = userName.split("\\\\");
			userName = userDetails[1];
		}
		tfsMetadataHelper.setUserName(userName); 
		tfsMetadataHelper.setPassword(model.getLandscapeConfigMap().get(passwordKey));
	}

}
