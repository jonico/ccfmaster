package com.collabnet.ccf.ccfmaster.web.model;

import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;


public class IdentityMappingsModel {

	private IdentityMapping identityMappingEntry;
	private RepositoryDetail repositoryDetail;
	private ArtifactDetail sourceArtifactDetail;
	private ArtifactDetail targetArtifactDetail;
	private String tfUrl;
	 
	
	public IdentityMapping getIdentityMappingEntry() {
		return identityMappingEntry;
	}

	public void setIdentityMappingEntry(IdentityMapping identityMappingEntry) {
		this.identityMappingEntry = identityMappingEntry;
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

	
	public ArtifactDetail getSourceArtifactDetail() {
		return sourceArtifactDetail;
	}

	public void setSourceArtifactDetail(ArtifactDetail sourceArtifactDetail) {
		this.sourceArtifactDetail = sourceArtifactDetail;
	}

	public ArtifactDetail getTargetArtifactDetail() {
		return targetArtifactDetail;
	}

	public void setTargetArtifactDetail(ArtifactDetail targetArtifactDetail) {
		this.targetArtifactDetail = targetArtifactDetail;
	}

	public String getArtifactId() {
		return sourceArtifactDetail.getArtifactId();
	}

	public String getArtifactIcon() {
		return sourceArtifactDetail.getIcon();
	}

	public String getArtifactData() {
		return sourceArtifactDetail.getDescription();
	}
	
	public String getTargetArtifactId() {
		return targetArtifactDetail.getArtifactId();
	}

	public String getTargetArtifactIcon() {
		return targetArtifactDetail.getIcon();
	}

	public String getTargetArtifactData() {
		return targetArtifactDetail.getDescription();
	}
}

