package com.collabnet.ccf.ccfmaster.gp.web.rmd;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;

public class DefaultCustomRMDParticipant implements ICustomizeRMDParticipant{
	
	public static final String HYPHEN = "-";

	@Override
	public String getParticipantRepositoryId(RMDModel model) {
		StringBuilder builder = new StringBuilder();
		String repoId = null;
		List<CCFCoreProperty> configProperties = model.getParticipantSelectorFieldList();
		for (CCFCoreProperty property : configProperties) { 
			builder.append(property.getValue()).append(HYPHEN);
		}
		repoId = StringUtils.chomp(builder.toString(), HYPHEN);
		return repoId;
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
