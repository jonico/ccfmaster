package com.collabnet.ccf.ccfmaster.web.model;

import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;

public class IdentityMappingsModel {

    private IdentityMapping  identityMappingEntry;
    private RepositoryDetail repositoryDetail;
    private ArtifactDetail   sourceArtifactDetail;
    private ArtifactDetail   targetArtifactDetail;
    private String           tfUrl;

    public String getArtifactData() {
        return sourceArtifactDetail.getDescription();
    }

    public String getArtifactIcon() {
        return sourceArtifactDetail.getIcon();
    }

    public String getArtifactId() {
        return sourceArtifactDetail.getArtifactId();
    }

    public IdentityMapping getIdentityMappingEntry() {
        return identityMappingEntry;
    }

    public String getRepositoryData() {
        return repositoryDetail.getDescription();
    }

    public RepositoryDetail getRepositoryDetail() {
        return repositoryDetail;
    }

    public String getRepositoryIcon() {
        return repositoryDetail.getIcon();
    }

    public String getRepositoryId() {
        return repositoryDetail.getRepositoryId();
    }

    public ArtifactDetail getSourceArtifactDetail() {
        return sourceArtifactDetail;
    }

    public String getTargetArtifactData() {
        return targetArtifactDetail.getDescription();
    }

    public ArtifactDetail getTargetArtifactDetail() {
        return targetArtifactDetail;
    }

    public String getTargetArtifactIcon() {
        return targetArtifactDetail.getIcon();
    }

    public String getTargetArtifactId() {
        return targetArtifactDetail.getArtifactId();
    }

    public String getTfUrl() {
        return tfUrl;
    }

    public void setIdentityMappingEntry(IdentityMapping identityMappingEntry) {
        this.identityMappingEntry = identityMappingEntry;
    }

    public void setRepositoryDetail(RepositoryDetail repositoryDetail) {
        this.repositoryDetail = repositoryDetail;
    }

    public void setSourceArtifactDetail(ArtifactDetail sourceArtifactDetail) {
        this.sourceArtifactDetail = sourceArtifactDetail;
    }

    public void setTargetArtifactDetail(ArtifactDetail targetArtifactDetail) {
        this.targetArtifactDetail = targetArtifactDetail;
    }

    public void setTfUrl(String tfUrl) {
        this.tfUrl = tfUrl;
    }
}
