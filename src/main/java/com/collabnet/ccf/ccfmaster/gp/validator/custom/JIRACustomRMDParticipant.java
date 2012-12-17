package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.List;

import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.gp.web.rmd.ICustomizeRMDParticipant;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;

public class JIRACustomRMDParticipant implements ICustomizeRMDParticipant{

	@Override
	public String getParticipantRepositoryId(RMDModel model) {
		StringBuilder builder = new StringBuilder();
		List<CCFCoreProperty> configProperties = model.getParticipantSelectorFieldList();
		for (CCFCoreProperty property : configProperties) { 
			String name = property.getName();
			if(name.equalsIgnoreCase("jiraCollection")){
				builder.append(property.getValue());
				continue;
			}
			if(name.equalsIgnoreCase("jiraProjectList")){
				builder.append("-").append(property.getValue());
				continue;
			}
			if(name.equalsIgnoreCase("jiraWorkItemType")){
				builder.append("-").append(property.getValue());
				continue;
			}
		}
		return builder.toString();
	}

	@Override
	public String[] getCustomConflictResolutionPolicy() {
		ConflictResolutionPolicy[]  conflictValues = ConflictResolutionPolicy.values();
		String[] conflictPolicyArray = new String[conflictValues.length];
		for(int i=0;i<conflictValues.length;i++){
			conflictPolicyArray[i]= conflictValues[i].toString();
		}
		return conflictPolicyArray;
	}

}
