package com.collabnet.ccf.ccfmaster.gp.web.rmd;

import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;

public interface ICustomizeRMDParticipant {
	
	public String getParticipantRepositoryId(RMDModel model);
	
	public String[] getCustomConflictResolutionPolicy();

}
