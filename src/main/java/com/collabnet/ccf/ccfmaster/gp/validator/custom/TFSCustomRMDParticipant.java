package com.collabnet.ccf.ccfmaster.gp.validator.custom;

import java.util.List;

import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class TFSCustomRMDParticipant extends DefaultCustomRMDParticipant{

	@Override
	public String getParticipantRepositoryId(RMDModel model) {
		StringBuilder builder = new StringBuilder();
		List<CCFCoreProperty> configProperties = model.getParticipantSelectorFieldList();
		for (CCFCoreProperty property : configProperties) { 
			String name = property.getName();
			if(name.equalsIgnoreCase("tfsCollection")){
				builder.append(property.getValue());
				continue;
			}
			if(name.equalsIgnoreCase("tfsProjectList")){
				builder.append("-").append(property.getValue());
				continue;
			}
			if(name.equalsIgnoreCase("tfsWorkItemType")){
				builder.append("-").append(property.getValue());
				continue;
			}
		}
		return builder.toString();
	}

}
