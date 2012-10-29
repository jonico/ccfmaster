package com.collabnet.ccf.ccfmaster.gp.web.rmd;

public interface ICustomizeRMDParticipant<T> {
	
	public String getParticipantRepositoryId(T model);
	
	public String[] getCustomConflictResolutionPolicy();

}
