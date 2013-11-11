package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class MockQCMetaDataProvider implements QCMetaDataProvider {

    @Override
    public String showDefectFields(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        return "showDefectFields" + landscape + domain + project + qcURL
                + qcUser + qcPassword;
    }

    @Override
    public String showRequirementFields(Landscape landscape, String domain,
            String project, String requirementType, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException {
        return "showRequirementFields" + landscape + domain + project
                + requirementType + qcURL + qcUser + qcPassword;
    }

    @Override
    public String showRequirementTypes(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        return "showRequirementTypes" + landscape + domain + project + qcURL
                + qcUser + qcPassword;
    }

    @Override
    public String showVisibleDomains(Landscape landscape, String qcURL,
            String qcUser, String qcPassword) throws CoreConfigurationException {
        return "showVisibleDomains" + landscape + qcURL + qcUser + qcPassword;
    }

    @Override
    public String showVisibleProjectsInDomain(Landscape landscape,
            String domain, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        return "showVisibleProjectsInDomain" + landscape + domain + qcURL
                + qcUser + qcPassword;
    }

    @Override
    public String validateDomainAndProject(Landscape landscape, String domain,
            String project, String qcURL, String qcUser, String qcPassword)
            throws CoreConfigurationException {
        return "validateDomainAndProject" + landscape + domain + project
                + qcURL + qcUser + qcPassword;
    }

    @Override
    public String validateDomainAndProjectAndRequirementType(
            Landscape landscape, String domain, String project,
            String requirementType, String qcURL, String qcUser,
            String qcPassword) throws CoreConfigurationException {
        return "validateDomainAndProjectAndRequirementType" + landscape
                + domain + project + requirementType + qcURL + qcURL
                + qcPassword;
    }

}
