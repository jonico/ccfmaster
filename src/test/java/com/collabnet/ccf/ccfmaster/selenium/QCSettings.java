package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class QCSettings extends CcfAuthenticatedTestBase {

    @Before
    public void goToAdminUrl() {
        selenium.open("/CCFMaster/admin/ccfmaster");
    }

    @Test
    public void test01() {
        qcSettings();
    }

    @Test
    public void test02() {
        tfSettings();
    }

    @Test
    public void test03() {
        ccfProperties();
    }

    @Test
    public void test04() {
        repositoryMappings();
    }

    @Test
    public void test05() {
        createFieldMapping();
    }

    @Test
    public void test06() {
        exportFieldMappingTemplates();
    }

    @Test
    public void test07() {
        mergeFieldMappingTemplates();
    }

    @Test
    public void test08() {
        importFieldMappingTemplates();
    }

    @Test
    public void test09() {
        displayAndDeleteFieldMappingTemplate();
    }

    @Test
    public void test10() {
        failedShipments();
    }

    @Test
    public void test11() {
        connectorStatusAndLogs();
    }

    @Test
    public void test12() {
        connectorUpgrade();
    }

    private void ccfProperties() {
        selenium.click("link=Connector Properties");
        selenium.waitForPageToLoad("30000");
        selenium.type(
                "val",
                "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> added");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("css=div.greenText"));
        selenium.click("link=Log Template QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.type(
                "val",
                "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>");
        selenium.click("link=Cancel");
        selenium.waitForPageToLoad("30000");
        selenium.type(
                "val",
                "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> qdded");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("css=div.greenText"));
        verifyShouldStartAutomatically();
        verifySaveCoreConfigSettings();
        verifyRestoreDefaultSettings();
        verifyConnectorBehaviorValidation();
    }

    private void connectorStatusAndLogs() {
        try {
            boolean needRerun = false;
            selenium.click("link=Status");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Status QC to TF");
            selenium.waitForPageToLoad("30000");
            needRerun = Util.testStatus(selenium);
            if (needRerun) {
                Util.testStatus(selenium); // need to validate the status again(i.e if STARTED -> need to check STOPPED status and Vice versa)
            }
            Util.testBackup(selenium);
            testQcLogs();
        } catch (AssertionError e) {
            final String msg = "testStatus failed. Base64 screenshot:\n";
            Util.logScreenshot(msg, selenium);
            throw e;
        }
    }

    private void connectorUpgrade() {
        selenium.click("link=Status");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Connector Upgrade");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=defaultUpgradeOption");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("uploaded core (version 2.0.0 ($Revision$)) was not newer than existing landscape"));
        selenium.click("id=fileSelectUpgradeOption");
        selenium.type("id=coreFileUploader", "C:\\Selenium automated\\bad.zip");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Bad Zip File."));
        selenium.click("id=fileSelectUpgradeOption");
        selenium.type("id=coreFileUploader",
                "C:\\Selenium automated\\invalid.zip");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Bad Zip File."));
        selenium.click("id=fileSelectUpgradeOption");
        selenium.type("id=coreFileUploader",
                "C:\\Selenium automated\\valid.zip");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("uploaded core (version 2.0.0 ($Revision$)) was not newer than existing landscape"));
        selenium.click("id=fileSelectUpgradeOption");
        selenium.type("id=coreFileUploader",
                "C:\\Selenium automated\\valid-higherVersion.zip");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=_eventId_cancel");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("You canceled the core update."));
        selenium.click("link=Connector Upgrade");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=fileSelectUpgradeOption");
        selenium.type("id=coreFileUploader",
                "C:\\Selenium automated\\valid-higherVersion.zip");
        selenium.click("name=_eventId_upload");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Are you sure you want to proceed update to the uploaded core?"));
        //		selenium.click("name=_eventId_submit");
        //		selenium.waitForPageToLoad("95000");
        //		verifyTrue(selenium.isTextPresent("Core update succeeded"));
    }

    private void createFieldMapping() {
        Util.testcreateFieldMapping(selenium);
        Util.testcreateLinkFieldMapping(selenium);
        Util.testAssociateFieldMapping(selenium);
    }

    private void displayAndDeleteFieldMappingTemplate() {
        selenium.click("link=Field Mapping Templates");
        selenium.click("link=Field Mapping Templates QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Field Mapping Templates TF to QC");
        selenium.waitForPageToLoad("30000");
        //selenium.check("name=fmtid value=new Default TF Planning Folder to QC Requirement");
        selenium.click("xpath=(//input[@type='checkbox'])[last()]");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("link=Delete");
        selenium.waitForPageToLoad("30000");
        assertEquals("Selected Field Mapping Templates deleted successfully",
                selenium.getText("css=div.greenText"));
    }

    private void exportFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=fmtid");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("link=Export");
    }

    private void failedShipments() {
        navigateFailedShipmentTabs();
        Util.testFailedShipments(selenium);
        Util.testDeleteRepositoryMappings(selenium);

    }

    private void importFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.open("/CCFMaster/admin/uploadfieldmappingtemplate?direction=forward");
        selenium.waitForPageToLoad("30000");
        //XML file should be stored on below path in the machine where the selenium tests is running.
        //TODO check if we can export the field mapping template to the below path 
        selenium.type("id=file",
                "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
        selenium.click("link=Next");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=textbox",
                "new Default TF Planning Folder to QC Requirement");
        selenium.click("link=Import Selected");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Return");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isElementPresent("new Default TF Planning Folder to QC Requirement"));
    }

    private void mergeFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.open("/CCFMaster/admin/uploadfieldmappingtemplate?direction=forward");
        selenium.waitForPageToLoad("30000");
        //XML file should be stored on below path in the machine where the selenium tests is running.
        //TODO check if we can export the field mapping template to the below path 
        selenium.type("id=file",
                "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
        selenium.click("link=Next");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=textbox",
                "Default TF Planning Folder to QC Requirement");
        selenium.click("link=Import Selected");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("Updated Successfully"));
    }

    private void navigateFailedShipmentTabs() {
        selenium.click("link=Failed Shipments");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Failed Shipments");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Failed Shipment QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Failed Shipment TF to QC");
        selenium.waitForPageToLoad("30000");
    }

    private void navigateRespositoryMappingTabs() {
        selenium.click("link=Repository Mappings");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Repository Mappings QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Repository Mappings TF to QC");
        selenium.waitForPageToLoad("30000");
    }

    private void qcSettings() {
        selenium.type("participantUrlParticipantConfig.val", "");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isElementPresent("id=participantUrlParticipantConfig.val.errors"));
        verifyTrue(selenium
                .isTextPresent("Enter a valid HPQC url ends with qcbin/"));
        selenium.type("participantUrlParticipantConfig.val",
                "www.hpurl.com.qcbin/");
        selenium.type("participantPasswordLandscapeConfig.val", "admin123");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("css=div.greenText"));
        Util.applyParticipantSaveAndRestartOptions(selenium);
        validateQcUserCredentials();
    }

    private void repositoryMappings() {
        navigateRespositoryMappingTabs();
        Util.testRepositoryMappings(selenium);
        Util.testFailedShipmentCount(selenium);

    }

    private void testQcLogs() {
        selenium.open("/CCFMaster/admin/displaytftoparticipantlogs");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Logs QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Logs TF to QC");
        selenium.waitForPageToLoad("30000");
        selenium.open("/CCFMaster/admin/downloadlogfile?filename=ccf-info.log&direction=forward");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=ccf-info.log");
        selenium.waitForPageToLoad("30000");
        /*
         * for(int i=0 ; i<15000 ;i++){// this will invoke ajax request-to load
         * tail log file content
         * assertTrue(selenium.isElementPresent("//*[@id='fileContentDiv']")); }
         */
    }

    private void tfSettings() {
        Util.testTeamforgeSettings(selenium);

    }

    private void validateQcUserCredentials() {
        try {
            final String text = "HP Quality Center : Test Connection Success.";
            selenium.click("link=HP Quality Center");
            selenium.waitForPageToLoad("30000");
            selenium.type("id=participantUrlParticipantConfig",
                    "http://cu117.cloud.sp.collab.net:8080/qcbin");
            selenium.type("id=participantUserNameLandscapeConfig", "CCFUser");
            selenium.type("id=participantPasswordLandscapeConfig", "CCFUser");
            selenium.click("css=input[type=\"button\"]");
            Util.waitUntilTextPresent(selenium, text);
            verifyTrue(selenium.isTextPresent(text));
            selenium.click("link=Save");
            selenium.click("//button[2]");// clicks saveonly button
            selenium.waitForPageToLoad("30000");
            verifyTrue(selenium.isElementPresent("css=div.greenText"));
        } catch (WaitTimedOutException e) {
            final String msg = "Timed out exception occured while validating QC user credentials";
            Util.logScreenshot(msg, selenium);
            throw e;
        }
    }

    private void verifyConnectorBehaviorValidation() {
        final String pageErrorMsg = "Error saving connector properties. Please check the values entered.";
        final String fieldErrorMsg = "Value should be numeric";
        selenium.open("/CCFMaster/admin/displayccfpropertiessynctftopart");
        selenium.click("link=Connector Behavior TF to QC");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=ccfCoreProperties0.value", "1000a");
        selenium.type("id=ccfCoreProperties1.value", "21600b");
        selenium.type("id=ccfCoreProperties2.value", "8b");
        selenium.type("id=ccfCoreProperties3.value", "500b");
        selenium.click("link=Repository Settings");
        selenium.click("link=Core Settings");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent(pageErrorMsg));
        verifyTrue(selenium.isTextPresent(fieldErrorMsg));
        selenium.click("link=Core Settings");
        selenium.type("id=ccfCoreProperties0.value", "adf");
        selenium.type("id=ccfCoreProperties1.value", "adfa");
        selenium.type("id=ccfCoreProperties2.value", "afda");
        selenium.type("id=ccfCoreProperties3.value", "afd");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent(pageErrorMsg));
        verifyTrue(selenium.isTextPresent(fieldErrorMsg));
        selenium.type("id=ccfCoreProperties0.value", "adf1234qwe");
        selenium.type("id=ccfCoreProperties1.value", "adfa1233qerw");
        selenium.type("id=ccfCoreProperties2.value", "afda123qwe");
        selenium.type("id=ccfCoreProperties3.value", "afd123we");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent(pageErrorMsg));
        verifyTrue(selenium.isTextPresent(fieldErrorMsg));
        selenium.click("link=Connector Behavior QC to TF");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=ccfCoreProperties0.value", "1000a");
        selenium.type("id=ccfCoreProperties1.value", "21600b");
        selenium.type("id=ccfCoreProperties2.value", "8aa");
        selenium.type("id=ccfCoreProperties3.value", "500b");
        selenium.click("link=Repository Settings");
        selenium.type("id=ccfCoreProperties6.value", "0df");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent(pageErrorMsg));
        verifyTrue(selenium.isTextPresent(fieldErrorMsg));
        selenium.click("link=Repository Settings");
        selenium.click("link=Core Settings");
        selenium.type("id=ccfCoreProperties0.value", "adfa");
        selenium.type("id=ccfCoreProperties1.value", "adfa");
        selenium.type("id=ccfCoreProperties2.value", "adfasd");
        selenium.type("id=ccfCoreProperties3.value", "afdsa");
        selenium.click("link=Repository Settings");
        selenium.type("id=ccfCoreProperties6.value", "adasda");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent(pageErrorMsg));
        verifyTrue(selenium.isTextPresent(fieldErrorMsg));
    }

    private void verifyRestoreDefaultSettings() {
        selenium.click("link=Connector Behavior TF to QC");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Restore Default Settings");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Default Settings Restored Successfully.Click Save to update the database."));
        //verify default values.When you update ccfcoredefaultconfig.xml make sure that this testcase gets updated accordingly
        verifyEquals("1000", selenium.getValue("id=ccfCoreProperties0.value"));
        verifyEquals("21600", selenium.getValue("id=ccfCoreProperties1.value"));
        verifyEquals("8", selenium.getValue("id=ccfCoreProperties2.value"));
        verifyEquals("500", selenium.getValue("id=ccfCoreProperties3.value"));
        verifyEquals("on", selenium.getValue("id=myCheckbox_5"));
        verifyEquals(" > ", selenium.getValue("id=ccfCoreProperties5.value"));
        verifyEquals("off", selenium.getValue("id=myCheckbox_7"));
        verifyEquals("off", selenium.getValue("id=myCheckbox_8"));
        verifyEquals("off", selenium.getValue("id=myCheckbox_9"));
        verifyEquals(" > ", selenium.getValue("id=ccfCoreProperties9.value"));
    }

    private void verifySaveCoreConfigSettings() {
        selenium.click("link=Connector Behavior TF to QC");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=ccfCoreProperties0.value", "");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Values cannot be blank. Please enter a value"));
        selenium.type("id=ccfCoreProperties0.value", "1001");
        selenium.type("id=ccfCoreProperties1.value", "21601");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Connector Behavior settings saved successfully"));
        verifyEquals("1001", selenium.getValue("id=ccfCoreProperties0.value"));
        verifyEquals("21601", selenium.getValue("id=ccfCoreProperties1.value"));

    }

    private void verifyShouldStartAutomatically() {
        selenium.click("link=Connector Behavior TF to QC");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=direction.shouldStartAutomatically1");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Connector Behavior settings saved successfully"));
        selenium.click("id=direction.shouldStartAutomatically1");
        selenium.click("link=Save");
        selenium.click("//button[2]");// clicks saveonly button
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isTextPresent("Connector Behavior settings saved successfully"));
    }

    @BeforeClass
    public static void createLandscape() {
        Selenium selenium = SeleniumSuite.getSelenium();
        try {
            Util.deleteAllParticipants(selenium);
            Util.createQCLandscape(selenium);

            MockDataUtil.createExternalApp(selenium); // check external app is implicitly created
            MockDataUtil
                    .createRepositoryMappingAndRepositoryMappingDirection(selenium);
            MockDataUtil.createFailedShipments(selenium);
        } catch (RuntimeException e) {
            logScreenshot(selenium);
            throw e;
        }
    }

    @AfterClass
    public static void destroyLandscape() {
        Selenium selenium = SeleniumSuite.getSelenium();
        Util.deleteAllParticipants(selenium);
    }
}
