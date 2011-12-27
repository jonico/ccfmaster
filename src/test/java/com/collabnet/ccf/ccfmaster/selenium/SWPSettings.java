package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class SWPSettings extends CcfAuthenticatedTestBase {
	
	@BeforeClass
	public static void createLandscape() {
		Selenium selenium = SeleniumSuite.getSelenium();
		try {
			Util.deleteAllParticipants(selenium);
			Util.createSWPLandscape(selenium);
			
			MockDataUtil.createExternalApp(selenium); // check external app is implicitly created
			MockDataUtil.createRepositoryMappingAndRepositoryMappingDirection(selenium);
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

	@Before
	public void goToAdminUrl() {
		selenium.open("/CCFMaster/admin/ccfmaster");
	}
	
	@Test
	public void swpSettings() {
		selenium.type("participantUserNameLandscapeConfig.val", "");
		selenium.type("participantPasswordLandscapeConfig.val", "");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("id=participantUserNameLandscapeConfig.val.errors"));
		verifyTrue(selenium.isElementPresent("id=participantPasswordLandscapeConfig.val.errors"));
		selenium.type("participantUserNameLandscapeConfig.val", "ccfuser");
		selenium.type("participantPasswordLandscapeConfig.val", "ccfuser");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		
		validateCcfUserCredentials();
		validateCcfResyncUserCredentials();
	}
	
	@Test
	public void tfSettings() {
		Util.testTeamforgeSettings(selenium);
		
	}
	
	@Test
	public void ccfProperties() {
		selenium.click("link=Connector Properties");
		selenium.waitForPageToLoad("30000");
		navigateCcfPropertiesTab();
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> updated");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Log Template SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> added");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		
		resumeConnectorTftoSwpService();
		resumeConnectorSwptoTfService();
	}

	@Test
	public void repositoryMappings() {
		navigateSwpRespositoryMappingTab();		
		Util.testRepositoryMappings(selenium);
		Util.testFailedShipmentCount(selenium);
		
	}

	@Test
	public void failedShipments() {
		Util.testFailedShipments(selenium);
		Util.testDeleteRepositoryMappings(selenium);
		
	}

	@Test
	public void connectorStatusAndLogs() {
		try {
			boolean needRerun = false;
			selenium.click("link=Status");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=Status SWP to TF");
			selenium.waitForPageToLoad("30000");
			needRerun = Util.testStatus(selenium);
			if (needRerun) {
				Util.testStatus(selenium); // need to validate the status again(i.e if STARTED -> need to check STOPPED status and Vice versa)
			}
			testSwpLogs();
		} catch (AssertionError e) {
			final String msg = "testStatus failed. Base64 screenshot:\n";
			Util.logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	
	public void testSwpLogs() {
		selenium.click("link=Logs");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Logs SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Logs TF to SWP");
		selenium.waitForPageToLoad("30000");
		selenium.open("/CCFMaster/admin/downloadlogfile?filename=ccf-info.log&direction=forward");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=ccf-info.log");
		selenium.waitForPageToLoad("30000");
	}
	
	private void validateCcfUserCredentials(){
		try{
			final String text = "ScrumWorks Pro : Test Connection Success.";
			selenium.waitForPageToLoad("30000");
			selenium.type("id=participantUrlParticipantConfig", "http://cu137.cloud.sp.collab.net:8080/scrumworks-api/api2/scrumworks?wsdl"); // need to externalize the input values
			selenium.type("id=participantUserNameLandscapeConfig", "CCFUser");
			selenium.type("id=participantPasswordLandscapeConfig", "CCFUser");
			selenium.click("css=input[type=\"button\"]");
			Util.waitUntilTextPresent(selenium, text);
			verifyTrue(selenium.isTextPresent(text));
		}catch(WaitTimedOutException e){
			final String msg = "Timed out exception occured while validating CCF user credentials";
			Util.logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	private void validateCcfResyncUserCredentials(){
		try{
			final String text = "ScrumWorks Pro : Test Connection Success.";
			selenium.refresh();
			selenium.type("id=participantUrlParticipantConfig", "http://cu137.cloud.sp.collab.net:8080/scrumworks-api/api2/scrumworks?wsdl"); // need to externalize the input values
			selenium.type("id=participantResyncUserNameLandscapeConfig", "CCFResync");
			selenium.type("id=participantResyncPasswordLandscapeConfig", "CCFResync");
			selenium.click("//input[@value='Test Connection' and @value='Test Connection' and @type='button' and @onclick=\"doAjaxResyncPost('/CCFMaster/admin/swptestconnection?resync=true')\"]");
			Util.waitUntilTextPresent(selenium, text);
			verifyTrue(selenium.isTextPresent(text));
		}catch(WaitTimedOutException e){
			final String msg = "Timed out exception occured while validating CCF resync user credentials";
			Util.logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	private void navigateCcfPropertiesTab(){
		selenium.click("link=Log Template TF to SWP");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Log Template SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Connector Behavior TF to SWP");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Connector Behavior SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Log Template TF to SWP");
		selenium.waitForPageToLoad("30000");
	}
	
	private void resumeConnectorTftoSwpService(){
		selenium.click("link=Connector Properties");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Connector Behavior TF to SWP");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=shouldStartAutomatically1");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Connector Behavior status saved successfully. CCF Core for TF => SWP will be started automatically next time TeamForge Connector Server starts up."));
		selenium.click("id=shouldStartAutomatically1");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Connector Behavior status saved successfully"));
	}
	
	private void resumeConnectorSwptoTfService(){
		selenium.click("link=Connector Properties");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Connector Behavior SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("id=shouldStartAutomatically1");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Connector Behavior status saved successfully. CCF Core for SWP => TF will be started automatically next time TeamForge Connector Server starts up."));
		selenium.click("id=shouldStartAutomatically1");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Connector Behavior status saved successfully"));
	}
	
	private void navigateSwpRespositoryMappingTab(){
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Repository Mappings SWP to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Repository Mappings TF to SWP");
		selenium.waitForPageToLoad("30000");
	}

}
