package com.collabnet.ccf.ccfmaster.selenium;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;

public final class Util {

    private static final Logger log                                   = LoggerFactory
                                                                              .getLogger(Util.class);

    private static final String tfTestConnection_SuccessMsg           = "Team Forge: Test Connection Success.";

    private static final String tfTestConnection_InvalidUser_ErrorMsg = "Team Forge: Test Connection Failure.Error logging in. Please verify the username and password.";

    private static final String xpath_Started_elemLocator             = "//input[@name='currentStatus' and @value='STARTED']";

    private static final String xpath_Stopped_elemLocator             = "//input[@name='currentStatus' and @value='STOPPED']";

    //    private static final String xpath_EditFM_elemLocator              = "//img[@alt='Associated Field Mappings']";

    private static final String tfBackup_SuccesMsg                    = "Backup is created successfully";

    // prevent instantiation.
    private Util() {
    }

    public static void applyParticipantSaveAndRestartOptions(Selenium selenium) {
        selenium.open("/CCFMaster/admin/ccfmaster");
        selenium.click("id=save");
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }

    /**
     * retrieves the base url to use from the system property ccf.baseUrl. If
     * not present, uses http://localhost:8080. The method removes all
     * path/query components from the url.
     * 
     * @return the base url.
     */
    public static String baseUrl() {
        String baseUrl = System.getProperty("ccf.baseUrl",
                "http://localhost:8080/");
        // not interested in path.
        URI uri = URI.create(baseUrl);
        String port = uri.getPort() == -1 ? "" : ":" + uri.getPort();
        baseUrl = String.format("%s://%s%s", uri.getScheme(), uri.getHost(),
                port);
        log.info("Configured base url: " + baseUrl);
        return baseUrl;
    }

    public static void createQCLandscape(Selenium selenium) {
        try {
            selenium.open("/CCFMaster/admin/ccfmaster");
            selenium.click("//input[@id='systemKind' and @value='QC']");
            selenium.click("link=Next");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Save");
            selenium.waitForPageToLoad("30000");
            selenium.type("landscape.name", "QC Landscape");
            selenium.type("participantUrlParticipantConfig.val",
                    "http://example.org/qcbin/");
            selenium.type("participantUserNameLandscapeConfig.val", "invalid");
            selenium.type("participantPasswordLandscapeConfig.val", "invalid");
            selenium.type("tfUserNameLandscapeConfig.val", "admin");
            selenium.type("tfPasswordLandscapeConfig.val", "admin");
            //			selenium.waitForPageToLoad("30000");
            selenium.click("css=input[type=\"button\"]");
            waitUntilTextPresent(selenium, tfTestConnection_SuccessMsg);
            assertTrue(selenium.isTextPresent(tfTestConnection_SuccessMsg));
            selenium.click("link=Save");
            selenium.click("link=Save");
            selenium.waitForPageToLoad("30000");
            assertFalse("Found error ID on page.",
                    selenium.isElementPresent("id=_title_ccferror_id"));
            //			assertTrue("Max Attachment Size not present on screen after create landscape", selenium.isTextPresent("Max Attachment Size"));
            assertThat(
                    selenium.getValue("participantUrlParticipantConfig.val"),
                    equalTo("http://example.org/qcbin/"));
            assertThat(
                    selenium.getValue("participantUserNameLandscapeConfig.val"),
                    equalTo("invalid"));
        } catch (RuntimeException e) {
            log.error("create QC landscape failed. Base64 screenshot:\n"
                    + "data:image/png;base64,{}",
                    selenium.captureScreenshotToString());
            throw e;
        } catch (AssertionError e) {
            final String msg = "create QC landscape failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void createSWPLandscape(Selenium selenium) {
        try {
            selenium.open("/CCFMaster/admin/ccfmaster");
            selenium.click("//input[@id='systemKind' and @value='SWP']");
            selenium.click("link=Next");
            selenium.waitForPageToLoad("30000");
            selenium.click("link=Save");
            selenium.waitForPageToLoad("30000");
            selenium.type("landscape.name", "SWP Landscape");
            selenium.type("participantUrlParticipantConfig.val",
                    "http://example.org");
            selenium.type("participantUserNameLandscapeConfig.val", "invalid");
            selenium.type("participantPasswordLandscapeConfig.val", "invalid");
            selenium.type("participantResyncUserNameLandscapeConfig.val",
                    "invalidresync");
            selenium.type("participantResyncPasswordLandscapeConfig.val",
                    "invalid");
            selenium.type("tfUserNameLandscapeConfig.val", "admin");
            selenium.type("tfPasswordLandscapeConfig.val", "admin");
            //			selenium.waitForPageToLoad("30000");
            selenium.click("css=input[type=\"button\"]");
            waitUntilTextPresent(selenium, tfTestConnection_SuccessMsg);
            assertTrue(selenium.isTextPresent(tfTestConnection_SuccessMsg));
            selenium.click("link=Save");
            selenium.waitForPageToLoad("30000");
            assertEquals(
                    "http://example.org/scrumworks-api/api2/scrumworks?wsdl",
                    selenium.getValue("participantUrlParticipantConfig.val"));
            assertEquals("invalid",
                    selenium.getValue("participantUserNameLandscapeConfig.val"));
            assertEquals(
                    "invalidresync",
                    selenium.getValue("participantResyncUserNameLandscapeConfig.val"));
        } catch (RuntimeException e) {
            final String msg = "create SWP landscape failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        } catch (AssertionError e) {
            final String msg = "create SWP landscape failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void deleteAllParticipants(Selenium selenium) {
        selenium.open("/CCFMaster/participants");
        while (!selenium.isTextPresent("No Participant found.")) {
            //			selenium.click("css=input[value='Delete Participant']");
            //			selenium.click("//input[@value='Delete Participant']");
            selenium.submit("//form[@id='command']");
            //			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
            selenium.waitForPageToLoad("300000");
        }
        assertFalse(selenium
                .isElementPresent("//input[@value='Delete Participant']"));
    }

    public static void login(Selenium selenium, String username, String password) {
        try {
            selenium.open("/CCFMaster/login");
            // selenium.click("j_username");
            selenium.type("j_username", username);
            selenium.type("j_password", password);
            selenium.click("proceed");
            selenium.waitForPageToLoad("30000");
        } catch (RuntimeException e) {
            log.error("Exception while logging in", e);
            final String msg = "Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void logout(Selenium selenium) {
        selenium.open("/CCFMaster/resources/j_spring_security_logout");
    }

    public static void logScreenshot(final String msg, Selenium selenium) {
        log.error(msg + "\ndata:image/png;base64,{}",
                selenium.captureScreenshotToString());
    }

    public static void testAssociateFieldMapping(Selenium selenium) {
        try {
            //To set active field mapping
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("name=fieldmappingid");
            selenium.chooseOkOnNextConfirmation();
            selenium.click("link=Set as Active Field Mapping");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Selected field mapping is activated successfully"));
            assertTrue(selenium.isElementPresent("css=img.trackerIcon"));

            //To delete active field mapping
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("name=fieldmappingid");
            selenium.chooseOkOnNextConfirmation();
            selenium.click("link=Delete");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Active field mapping cannot be deleted"));

            //To delete field mapping
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("xpath=(//input[@type='checkbox'])[last()]");
            selenium.chooseOkOnNextConfirmation();
            selenium.click("link=Delete");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Selected field mapping(s) deleted successfully"));

        } catch (AssertionError e) {
            final String msg = "test Associate FieldMapping failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testBackup(Selenium selenium) {
        selenium.open("/CCFMaster/admin/displayccfbackup");
        selenium.click("link=Connector Backup");
        selenium.waitForPageToLoad("30000");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("id=click-button");
        do {
            waitUntilTextPresent(selenium, tfBackup_SuccesMsg);
        } while (!selenium.isTextPresent(tfBackup_SuccesMsg));
        assertTrue(selenium.isTextPresent(tfBackup_SuccesMsg));
    }

    public static void testcreateFieldMapping(Selenium selenium) {
        try {
            //To create new field mapping to the template 
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("css=#disablebutton > div.Button > div.Middle > a");
            selenium.waitForPageToLoad("30000");
            selenium.select("id=fieldMappingLandscapeTemplate",
                    "label=Default MapForce TF Tracker Item to QC Defect");
            selenium.type("id=fieldmappingName",
                    "Modified Default MapForce TF Tracker Item to QC Defect");
            selenium.click("id=submitForm");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Field mapping created and associated to the repository mapping direction successfully"));
            assertTrue(selenium.isTextPresent("Repository Mapping Direction"));
            assertTrue(selenium.isTextPresent("MapForce"));

            //To create new field mapping to the already linked template
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("css=#disablebutton > div.Button > div.Middle > a");
            selenium.waitForPageToLoad("30000");
            selenium.select("id=fieldMappingLandscapeTemplate",
                    "label=Default MapForce TF Tracker Item to QC Defect");
            selenium.type("id=fieldmappingName",
                    "Modified Default MapForce TF Tracker Item to QC Defect");
            selenium.click("id=submitForm");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Field mapping template already exists with the same name"));
        } catch (AssertionError e) {
            final String msg = "test create FieldMapping failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testcreateLinkFieldMapping(Selenium selenium) {
        try {
            //To link new field mapping to the template 
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("css=#disablebutton > div.Button > div.Middle > a");
            selenium.waitForPageToLoad("30000");
            selenium.select("id=fieldMappingLandscapeTemplate",
                    "label=Default MapForce TF Tracker Item to QC Defect");
            selenium.click("id=linktemplate");
            selenium.click("id=submitForm");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("Field mapping created and associated to the repository mapping direction successfully"));
            assertTrue(selenium.isTextPresent("MapForce"));
            assertTrue(selenium.isTextPresent("Landscape"));

            //To link new field mapping to the already linked template
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("//img[@alt='Associated Field Mappings']");
            selenium.waitForPageToLoad("30000");
            selenium.click("css=#disablebutton > div.Button > div.Middle > a");
            selenium.waitForPageToLoad("30000");
            selenium.select("id=fieldMappingLandscapeTemplate",
                    "label=Default MapForce TF Tracker Item to QC Defect");
            selenium.click("id=linktemplate");
            selenium.click("id=submitForm");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium
                    .isTextPresent("A field mapping is already linked to the selected template"));
        } catch (AssertionError e) {
            final String msg = "test create Link FieldMapping failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testDeleteRepositoryMappings(Selenium selenium) {
        try {
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("name=rmdid");
            selenium.chooseOkOnNextConfirmation();
            selenium.click("link=Delete");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isElementPresent("css=div.greenText"));
        } catch (AssertionError e) {
            final String msg = "test DeleteRepositoryMappings failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testFailedShipmentCount(Selenium selenium) {
        try {
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("name=rmdid");
            int failedShipmentCount = Integer.valueOf(selenium
                    .getEval("link=1"));
            selenium.click("link=1");
            selenium.waitForPageToLoad("30000");
            //			selenium.click("name=hospitalid");
            //Xpath count always will be greater than 1 when compared with failedShipment List
            assertEquals((failedShipmentCount + 1),
                    selenium.getXpathCount("//input[@type='checkbox']")
                            .intValue());
        } catch (AssertionError e) {
            final String msg = "test FailedShipment Count failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testFailedShipmentDelete(Selenium selenium) {
        selenium.click("name=hospitalid");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("link=Delete");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("css=div.greenText"));
    }

    public static void testFailedShipmentFilter(Selenium selenium,
            String artifactId) {
        selenium.waitForPageToLoad("30000");
        selenium.type("id=sourcefilterartifactid", artifactId); //artf1785
        selenium.click("css=a > strong");
        selenium.waitForPageToLoad("30000");
        selenium.click("css=#removefilterbutton > table.AppButtonTable > tbody > tr > td.AppButtonText > a > strong");
        selenium.waitForPageToLoad("30000");
    }

    public static void testFailedShipmentOperations(Selenium selenium) {
        selenium.click("link=Failed Shipments");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=hospitalid");
        selenium.click("link=Replay");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("css=div.greenText"));
        selenium.click("name=hospitalid");
        selenium.click("link=Examine");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Payload32.xml"));
        selenium.click("link=Return");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=sample cause exception msg");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Return");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=hospitalid");
        selenium.click("link=Export");
        selenium.waitForPageToLoad("30000");
    }

    public static void testFailedShipmentRemoveFilter(Selenium selenium) {
        selenium.waitForPageToLoad("30000");
        selenium.click("css=#removefilterbutton > table.AppButtonTable > tbody > tr > td.AppButtonText > a > strong");
        selenium.waitForPageToLoad("30000");
    }

    public static void testFailedShipments(Selenium selenium) {
        try {
            testFailedShipmentOperations(selenium);
            testFailedShipmentFilter(selenium, "artf1785"); // need to give correct id to filter failed shipment (eg:artf 1788).
            testFailedShipmentRemoveFilter(selenium);
            testFailedShipmentDelete(selenium);

        } catch (AssertionError e) {
            final String msg = "testFailedShipments failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void testRepositoryMappings(Selenium selenium) {
        try {
            selenium.click("link=Repository Mappings");
            selenium.waitForPageToLoad("30000");
            selenium.click("name=rmdid");
            selenium.click("link=Resume");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isElementPresent("css=div.greenText"));
            selenium.click("name=rmdid");
            selenium.click("link=Pause");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isElementPresent("css=div.greenText"));
        } catch (AssertionError e) {
            final String msg = "test Repository Mappings failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static boolean testStatus(Selenium selenium) {
        boolean testStatusPassed = false;
        selenium.click("link=Status");
        selenium.waitForPageToLoad("30000");
        if (selenium.isElementPresent(xpath_Stopped_elemLocator)) {
            selenium.click("link=Start");
            testStatusPassed = checkStatusMsg(selenium,
                    xpath_Started_elemLocator, "STARTED");
        } else if (selenium.isElementPresent(xpath_Started_elemLocator)) {
            selenium.click("link=Stop");
            testStatusPassed = checkStatusMsg(selenium,
                    xpath_Stopped_elemLocator, "STOPPED");
        }
        return testStatusPassed;
    }

    public static void testTeamforgeSettings(Selenium selenium) {
        try {
            selenium.click("link=TeamForge");
            selenium.waitForPageToLoad("30000");
            updateTfUserCredentials(selenium, "", "");
            //			assertTrue(selenium.isElementPresent("css=span#tfUserNameLandscapeConfig.val.errors"));
            //			assertTrue(selenium.isElementPresent("css=span#tfPasswordLandscapeConfig.val.errors"));
            //validate incorrect user credentials
            updateTfUserCredentials(selenium, "abc", "abc");
            assertTrue(selenium.isElementPresent("css=div.greenText"));
            selenium.click("//input[@value='Test Connection']");
            waitUntilTextPresent(selenium,
                    tfTestConnection_InvalidUser_ErrorMsg);
            assertTrue(selenium
                    .isTextPresent(tfTestConnection_InvalidUser_ErrorMsg));
            //validate correct user credentials
            updateTfUserCredentials(selenium, "ccfuser", "ccfuser");
            assertTrue(selenium.isElementPresent("css=div.greenText"));
            selenium.click("//input[@value='Test Connection']");
            waitUntilTextPresent(selenium, tfTestConnection_SuccessMsg);
            assertTrue(selenium.isTextPresent(tfTestConnection_SuccessMsg));
        } catch (AssertionError e) {
            final String msg = "test Team forge Settings failed. Base64 screenshot:\n";
            logScreenshot(msg, selenium);
            throw e;
        }
    }

    public static void waitUntilTextPresent(final Selenium selenium,
            final String text) {
        Wait wait = new Wait() {
            public boolean until() {
                return selenium.isTextPresent(text);
            }
        };

        wait.wait("Couldn't find Text !", 60000l); // waits for 60 seconds
    }

    private static boolean checkStatusMsg(Selenium selenium, String xpathStr,
            String statusMsg) {
        do {
            selenium.click("link=Refresh");
            selenium.waitForPageToLoad("30000");
        } while (!selenium.isElementPresent(xpathStr));
        //		assertEquals(statusMsg, selenium.getValue(xpathStr));
        return statusMsg.equals(selenium.getValue(xpathStr));
    }

    private static void updateTfUserCredentials(Selenium selenium,
            String userName, String password) {
        selenium.type("id=tfUserNameLandscapeConfig", userName);
        selenium.type("id=tfPasswordLandscapeConfig", password);
        selenium.click("id=save");
        selenium.click("//button[2]");
        selenium.waitForPageToLoad("30000");
    }
}
