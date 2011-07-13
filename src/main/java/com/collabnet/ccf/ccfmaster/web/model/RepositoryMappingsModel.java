package com.collabnet.ccf.ccfmaster.web.model;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

public class RepositoryMappingsModel {

	private List<RepositoryMappingDirection> repositoryMappingDirection;
	
    private boolean updating = false; 

    private List hospitalCount;
    
    private List repositoryId;
    
    private List repositoryIcon;
    
    private List repositoryData;
    
    private String tfUrl;

	public List<RepositoryMappingDirection> getRepositoryMappingDirection() {
		return repositoryMappingDirection;
	}

	public void setRepositoryMappingDirection(
			List<RepositoryMappingDirection> repositoryMappingDirection) {
		this.repositoryMappingDirection = repositoryMappingDirection;
	}

	public boolean isUpdating() {
		return updating;
	}

	public void setUpdating(boolean updating) {
		this.updating = updating;
	}

	public List getHospitalCount() {
		return hospitalCount;
	}

	public void setHospitalCount(List hospitalCount) {
		this.hospitalCount = hospitalCount;
	}

	

	public List getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(List repositoryId) {
		this.repositoryId = repositoryId;
	}

	public List getRepositoryIcon() {
		return repositoryIcon;
	}

	public void setRepositoryIcon(List repositoryIcon) {
		this.repositoryIcon = repositoryIcon;
	}

	public List getRepositoryData() {
		return repositoryData;
	}

	public void setRepositoryData(List repositoryData) {
		this.repositoryData = repositoryData;
	}

	public String getTfUrl() {
		return tfUrl;
	}

	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
	}

	
	
}
