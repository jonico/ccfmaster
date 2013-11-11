package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public interface QCMetaDataProvider {
    public String showDefectFields(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException;

    public String showRequirementFields(Landscape landscape, String domain,
            String project, String requirementType, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException;

    public String showRequirementTypes(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException;

    public String showVisibleDomains(Landscape landscape, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException;

    public String showVisibleProjectsInDomain(Landscape landscape,
            String domain, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException;

    public String validateDomainAndProject(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException;

    public String validateDomainAndProjectAndRequirementType(
            Landscape landscape, String domain, String project,
            String requirementType, String qcURL, String qcUser,
            String qcPassword) throws CoreConfigurationException;
}
