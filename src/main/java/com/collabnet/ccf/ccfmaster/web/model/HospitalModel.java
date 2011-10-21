package com.collabnet.ccf.ccfmaster.web.model;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;


public class HospitalModel {

	private HospitalEntry hospitalEntry;
	private RepositoryDetail repositoryDetail;
	private ArtifactDetail artifactDetail;
	
	private String tfUrl;
	 
	public HospitalEntry getHospitalEntry() {
		return hospitalEntry;
	}

	public void setHospitalEntry(HospitalEntry hospitalEntry) {
		this.hospitalEntry = hospitalEntry;
	}

	public RepositoryDetail getRepositoryDetail() {
		return repositoryDetail;
	}

	public void setRepositoryDetail(RepositoryDetail repositoryDetail) {
		this.repositoryDetail = repositoryDetail;
	}

	public String getTfUrl() {
		return tfUrl;
	}

	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
	}

	public String getRepositoryId() {
		return repositoryDetail.getRepositoryId();
	}

	public String getRepositoryIcon() {
		return repositoryDetail.getIcon();
	}

	public String getRepositoryData() {
		return repositoryDetail.getDescription();
	}

	public ArtifactDetail getArtifactDetail() {
		return artifactDetail;
	}

	public void setArtifactDetail(ArtifactDetail artifactDetail) {
		this.artifactDetail = artifactDetail;
	}

	public String getArtifactId() {
		return artifactDetail.getArtifactId();
	}

	public String getArtifactIcon() {
		return artifactDetail.getIcon();
	}

	public String getArtifactData() {
		return artifactDetail.getDescription();
	}
	
	
}

