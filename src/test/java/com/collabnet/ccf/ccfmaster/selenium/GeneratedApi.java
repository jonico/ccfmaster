package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;

public class GeneratedApi extends CcfAuthenticatedTestBase {

    @Test
    public void apiGetCcfCoreStatus() {
        selenium.open("/CCFMaster/api/ccfcorestatuses/");
        verifyTrue(selenium.isElementPresent("//ccfCoreStatusList"));
        if (selenium.isElementPresent("//ccfCoreStatusList/ccfCoreStatus/id")) {
            String entityId = selenium.getText("//ccfCoreStatus/id[1]");
            selenium.open("/CCFMaster/api/ccfcorestatuses/" + entityId);
            verifyTrue(selenium.isElementPresent("//ccfCoreStatus/id"));
        }
        selenium.open("/CCFMaster/api/ccfcorestatuses");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetDirection() {
        selenium.open("/CCFMaster/api/directions/");
        verifyTrue(selenium.isElementPresent("//directionList"));
        if (selenium.isElementPresent("//directionList/direction/id")) {
            String entityId = selenium.getText("//direction/id[1]");
            selenium.open("/CCFMaster/api/directions/" + entityId);
            verifyTrue(selenium.isElementPresent("//direction/id"));
        }
        selenium.open("/CCFMaster/api/directions");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetDirectionConfig() {
        selenium.open("/CCFMaster/api/directionconfigs/");
        verifyTrue(selenium.isElementPresent("//directionConfigList"));
        if (selenium
                .isElementPresent("//directionConfigList/directionConfig/id")) {
            String entityId = selenium.getText("//directionConfig/id[1]");
            selenium.open("/CCFMaster/api/directionconfigs/" + entityId);
            verifyTrue(selenium.isElementPresent("//directionConfig/id"));
        }
        selenium.open("/CCFMaster/api/directionconfigs");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetExternalApp() {
        selenium.open("/CCFMaster/api/externalapps/");
        verifyTrue(selenium.isElementPresent("//externalAppList"));
        if (selenium.isElementPresent("//externalAppList/externalApp/id")) {
            String entityId = selenium.getText("//externalApp/id[1]");
            selenium.open("/CCFMaster/api/externalapps/" + entityId);
            verifyTrue(selenium.isElementPresent("//externalApp/id"));
        }
        selenium.open("/CCFMaster/api/externalapps");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetFieldMapping() {
        selenium.open("/CCFMaster/api/fieldmappings/");
        verifyTrue(selenium.isElementPresent("//fieldMappingList"));
        if (selenium.isElementPresent("//fieldMappingList/fieldMapping/id")) {
            String entityId = selenium.getText("//fieldMapping/id[1]");
            selenium.open("/CCFMaster/api/fieldmappings/" + entityId);
            verifyTrue(selenium.isElementPresent("//fieldMapping/id"));
        }
        selenium.open("/CCFMaster/api/fieldmappings");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetFieldMappingExternalAppTemplate() {
        selenium.open("/CCFMaster/api/fieldmappingexternalapptemplates/");
        verifyTrue(selenium
                .isElementPresent("//fieldMappingExternalAppTemplateList"));
        if (selenium
                .isElementPresent("//fieldMappingExternalAppTemplateList/fieldMappingExternalAppTemplate/id")) {
            String entityId = selenium
                    .getText("//fieldMappingExternalAppTemplate/id[1]");
            selenium.open("/CCFMaster/api/fieldmappingexternalapptemplates/"
                    + entityId);
            verifyTrue(selenium
                    .isElementPresent("//fieldMappingExternalAppTemplate/id"));
        }
        selenium.open("/CCFMaster/api/fieldmappingexternalapptemplates");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetFieldMappingLandscapeTemplate() {
        selenium.open("/CCFMaster/api/fieldmappinglandscapetemplates/");
        verifyTrue(selenium
                .isElementPresent("//fieldMappingLandscapeTemplateList"));
        if (selenium
                .isElementPresent("//fieldMappingLandscapeTemplateList/fieldMappingLandscapeTemplate/id")) {
            String entityId = selenium
                    .getText("//fieldMappingLandscapeTemplate/id[1]");
            selenium.open("/CCFMaster/api/fieldmappinglandscapetemplates/"
                    + entityId);
            verifyTrue(selenium
                    .isElementPresent("//fieldMappingLandscapeTemplate/id"));
        }
        selenium.open("/CCFMaster/api/fieldmappinglandscapetemplates");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetHospitalEntry() {
        selenium.open("/CCFMaster/api/hospitalentrys/");
        verifyTrue(selenium.isElementPresent("//hospitalEntryList"));
        if (selenium.isElementPresent("//hospitalEntryList/hospitalEntry/id")) {
            String entityId = selenium.getText("//hospitalEntry/id[1]");
            selenium.open("/CCFMaster/api/hospitalentrys/" + entityId);
            verifyTrue(selenium.isElementPresent("//hospitalEntry/id"));
        }
        selenium.open("/CCFMaster/api/hospitalentrys");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetIdentityMapping() {
        selenium.open("/CCFMaster/api/identitymappings/");
        verifyTrue(selenium.isElementPresent("//identityMappingList"));
        if (selenium
                .isElementPresent("//identityMappingList/identityMapping/id")) {
            String entityId = selenium.getText("//identityMapping/id[1]");
            selenium.open("/CCFMaster/api/identitymappings/" + entityId);
            verifyTrue(selenium.isElementPresent("//identityMapping/id"));
        }
        selenium.open("/CCFMaster/api/identitymappings");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetLandscape() {
        selenium.open("/CCFMaster/api/landscapes/");
        verifyTrue(selenium.isElementPresent("//landscapeList"));
        if (selenium.isElementPresent("//landscapeList/landscape/id")) {
            String entityId = selenium.getText("//landscape/id[1]");
            selenium.open("/CCFMaster/api/landscapes/" + entityId);
            verifyTrue(selenium.isElementPresent("//landscape/id"));
        }
        selenium.open("/CCFMaster/api/landscapes");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetLandscapeConfig() {
        selenium.open("/CCFMaster/api/landscapeconfigs/");
        verifyTrue(selenium.isElementPresent("//landscapeConfigList"));
        if (selenium
                .isElementPresent("//landscapeConfigList/landscapeConfig/id")) {
            String entityId = selenium.getText("//landscapeConfig/id[1]");
            selenium.open("/CCFMaster/api/landscapeconfigs/" + entityId);
            verifyTrue(selenium.isElementPresent("//landscapeConfig/id"));
        }
        selenium.open("/CCFMaster/api/landscapeconfigs");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetLogFile() {
        selenium.open("/CCFMaster/api/logfiles/");
        verifyTrue(selenium.isElementPresent("//logFileList"));
        if (selenium.isElementPresent("//logFileList/logFile/id")) {
            String entityId = selenium.getText("//logFile/id[1]");
            selenium.open("/CCFMaster/api/logfiles/" + entityId);
            verifyTrue(selenium.isElementPresent("//logFile/id"));
        }
        selenium.open("/CCFMaster/api/logfiles");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetParticipant() {
        selenium.open("/CCFMaster/api/participants/");
        verifyTrue(selenium.isElementPresent("//participantList"));
        if (selenium.isElementPresent("//participantList/participant/id")) {
            String entityId = selenium.getText("//participant/id[1]");
            selenium.open("/CCFMaster/api/participants/" + entityId);
            verifyTrue(selenium.isElementPresent("//participant/id"));
        }
        selenium.open("/CCFMaster/api/participants");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetParticipantConfig() {
        selenium.open("/CCFMaster/api/participantconfigs/");
        verifyTrue(selenium.isElementPresent("//participantConfigList"));
        if (selenium
                .isElementPresent("//participantConfigList/participantConfig/id")) {
            String entityId = selenium.getText("//participantConfig/id[1]");
            selenium.open("/CCFMaster/api/participantconfigs/" + entityId);
            verifyTrue(selenium.isElementPresent("//participantConfig/id"));
        }
        selenium.open("/CCFMaster/api/participantconfigs");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetRepositoryMapping() {
        selenium.open("/CCFMaster/api/repositorymappings/");
        verifyTrue(selenium.isElementPresent("//repositoryMappingList"));
        if (selenium
                .isElementPresent("//repositoryMappingList/repositoryMapping/id")) {
            String entityId = selenium.getText("//repositoryMapping/id[1]");
            selenium.open("/CCFMaster/api/repositorymappings/" + entityId);
            verifyTrue(selenium.isElementPresent("//repositoryMapping/id"));
        }
        selenium.open("/CCFMaster/api/repositorymappings");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    @Test
    public void apiGetRepositoryMappingDirection() {
        selenium.open("/CCFMaster/api/repositorymappingdirections/");
        verifyTrue(selenium
                .isElementPresent("//repositoryMappingDirectionList"));
        if (selenium
                .isElementPresent("//repositoryMappingDirectionList/repositoryMappingDirection/id")) {
            String entityId = selenium
                    .getText("//repositoryMappingDirection/id[1]");
            selenium.open("/CCFMaster/api/repositorymappingdirections/"
                    + entityId);
            verifyTrue(selenium
                    .isElementPresent("//repositoryMappingDirection/id"));
        }
        selenium.open("/CCFMaster/api/repositorymappingdirections");
        verifyTrue(selenium.isElementPresent("//h1"));
    }

    /**
     * deletes all participants
     */
    @AfterClass
    public static void cleanup() {
        Selenium selenium = SeleniumSuite.getSelenium();
        Util.deleteAllParticipants(selenium);
    }

    /**
     * deletes all participants using the scaffold UI and creates a new SWP
     * landscape.
     */
    @BeforeClass
    public static void startOver() {
        Selenium selenium = SeleniumSuite.getSelenium();
        Util.deleteAllParticipants(selenium);
        Util.createSWPLandscape(selenium);
    }
}
