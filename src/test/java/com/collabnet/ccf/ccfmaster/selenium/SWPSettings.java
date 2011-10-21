package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;

public class SWPSettings extends CcfAuthenticatedTestBase {

	@BeforeClass
	public static void createLandscape() {
		Selenium selenium = SeleniumSuite.getSelenium();
		Util.deleteAllParticipants(selenium);
		Util.createSWPLandscape(selenium);
	}
	
	@AfterClass
	public static void destroyLandscape() {
		Selenium selenium = SeleniumSuite.getSelenium();
		Util.deleteAllParticipants(selenium);
	}

	@Before
	public void goToAdminUrl() {
		selenium.open("/CCFMaster/admin/ccfmaster");
	}
	
	@Test
	public void swpSettings() {
		selenium.type("participantUserNameLandscapeConfig.val", "");
		selenium.type("participantPasswordLandscapeConfig.val", "");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("id=participantUserNameLandscapeConfig.val.errors"));
		verifyTrue(selenium.isElementPresent("id=participantPasswordLandscapeConfig.val.errors"));
		selenium.type("participantUserNameLandscapeConfig.val", "ccfuser");
		selenium.type("participantPasswordLandscapeConfig.val", "ccfuser");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
	}
	
	@Test
	public void tfSettings() {
		selenium.click("link=TeamForge");
		selenium.waitForPageToLoad("30000");
		selenium.type("tfUserNameLandscapeConfig.val", "ccfuser");
		selenium.type("tfPasswordLandscapeConfig.val", "ccfuser");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("//input[@value='Test Connection']");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
	}
	
	@Test
	public void ccfProperties() {
		selenium.click("link=Connector Properties");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> updated");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Log Template SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> added");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Connector Behavior TF to SWP");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Connector Behavior SWP to TF");
		selenium.waitForPageToLoad("30000");
	}
	
	@Test
	public void repositoryMappings() {
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
	}
	
	@Test
	public void status() {
		selenium.click("link=Status");
		selenium.waitForPageToLoad("30000");
		verifyEquals("STOPPED", selenium.getValue("id=currentStatus"));
	}
	
	@Test
	public void logs() {
		selenium.click("link=Logs");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Logs SWP to TF");
		selenium.waitForPageToLoad("30000");
	}
}
