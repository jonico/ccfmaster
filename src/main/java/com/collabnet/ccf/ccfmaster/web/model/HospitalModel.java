package com.collabnet.ccf.ccfmaster.web.model;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;

public class HospitalModel {

    private HospitalEntry    hospitalEntry;
    private RepositoryDetail repositoryDetail;
    private ArtifactDetail   artifactDetail;

    private String           tfUrl;

    public String getArtifactData() {
        return artifactDetail.getDescription();
    }

    public ArtifactDetail getArtifactDetail() {
        return artifactDetail;
    }

    public String getArtifactIcon() {
        return artifactDetail.getIcon();
    }

    public String getArtifactId() {
        return artifactDetail.getArtifactId();
    }

    public HospitalEntry getHospitalEntry() {
        return hospitalEntry;
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

    public String getTfUrl() {
        return tfUrl;
    }

    public void setArtifactDetail(ArtifactDetail artifactDetail) {
        this.artifactDetail = artifactDetail;
    }

    public void setHospitalEntry(HospitalEntry hospitalEntry) {
        this.hospitalEntry = hospitalEntry;
    }

    public void setRepositoryDetail(RepositoryDetail repositoryDetail) {
        this.repositoryDetail = repositoryDetail;
    }

    public void setTfUrl(String tfUrl) {
        this.tfUrl = tfUrl;
    }

}
