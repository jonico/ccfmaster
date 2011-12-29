package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class QCSettings extends CcfAuthenticatedTestBase {

	@BeforeClass
	public static void createLandscape() {
		Selenium selenium = SeleniumSuite.getSelenium();
		try{
			Util.deleteAllParticipants(selenium);
			Util.createQCLandscape(selenium);
			
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
	public void qcSettings() {
		selenium.type("participantUrlParticipantConfig.val", "");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("id=participantUrlParticipantConfig.val.errors"));
		verifyTrue(selenium.isTextPresent("Enter a valid HPQC url ends with qcbin/"));
		selenium.type("participantUrlParticipantConfig.val", "www.hpurl.com.qcbin/");
		selenium.type("participantPasswordLandscapeConfig.val", "admin123");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		Util.applyParticipantSaveAndRestartOptions(selenium);
		validateQcUserCredentials();
	}
	
	@Test
	public void tfSettings() {
		Util.testTeamforgeSettings(selenium);
		
	}
	
	@Test
	public void ccfProperties() {
		selenium.click("link=Connector Properties");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> added");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Log Template QC to TF");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>");
		selenium.click("link=Cancel");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> qdded");
		selenium.click("link=Save");
		selenium.click("//button[2]");// clicks saveonly button
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Connector Behavior TF to QC");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Should Start Automatically"));
		selenium.click("link=Connector Behavior QC to TF");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Should Start Automatically"));
	}
	
	@Test
	public void repositoryMappings() {
		navigateRespositoryMappingTabs();
		Util.testRepositoryMappings(selenium);
		Util.testFailedShipmentCount(selenium);
		
	}
	
	@Test
	public void exportFieldMappingTemplates(){
		selenium.click("link=Field Mapping Templates");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=fmtid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Export");
	}
	
		
	@Test
	public void mergeFieldMappingTemplates(){
		selenium.click("link=Field Mapping Templates");
		selenium.open("/CCFMaster/admin/uploadfieldmappingtemplate?direction=forward");
		selenium.waitForPageToLoad("30000");
		//XML file should be stored on below path in the machine where the selenium tests is running.
		//TODO check if we can export the field mapping template to the below path 
		selenium.type("id=file", "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
		selenium.click("link=Next");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=textbox", "Default TF Planning Folder to QC Requirement");
		selenium.click("link=Import Selected");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("Updated Successfully"));	
	} 
	
	
	@Test
	public void importFieldMappingTemplates(){
		selenium.click("link=Field Mapping Templates");
		selenium.open("/CCFMaster/admin/uploadfieldmappingtemplate?direction=forward");
		selenium.waitForPageToLoad("30000");
		//XML file should be stored on below path in the machine where the selenium tests is running.
		//TODO check if we can export the field mapping template to the below path 
		selenium.type("id=file", "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
		selenium.click("link=Next");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=textbox", "new Default TF Planning Folder to QC Requirement");
		selenium.click("link=Import Selected");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Return");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("new Default TF Planning Folder to QC Requirement"));	
	} 
	
	
	@Test
	public void displayAndDeleteFieldMappingTemplate(){
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
		assertEquals("Selected Field Mapping Templates deleted successfully", selenium.getText("css=div.greenText"));
	}
	
	
	@Test
	public void failedShipments(){
		navigateFailedShipmentTabs();
		Util.testFailedShipments(selenium);
		Util.testDeleteRepositoryMappings(selenium);
		
	}
	
	@Test
	public void connectorStatusAndLogs() {
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
			testQcLogs();
		} catch (AssertionError e) {
			final String msg = "testStatus failed. Base64 screenshot:\n";
			Util.logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	
	public void testQcLogs() {
		selenium.click("link=Logs");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Logs QC to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Logs TF to QC");
		selenium.waitForPageToLoad("30000");
		selenium.open("/CCFMaster/admin/downloadlogfile?filename=ccf-info.log&direction=forward");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=ccf-info.log");
		selenium.waitForPageToLoad("30000");
	}
	
	private void validateQcUserCredentials(){
		try{
			final String text ="HP Quality Center : Test Connection Success.";
			selenium.click("link=HP Quality Center");
			selenium.waitForPageToLoad("30000");
			selenium.type("id=participantUrlParticipantConfig", "http://cu117.cloud.sp.collab.net:8080/qcbin");
			selenium.type("id=participantUserNameLandscapeConfig", "CCFUser");
			selenium.type("id=participantPasswordLandscapeConfig", "CCFUser");
			selenium.click("css=input[type=\"button\"]");
			Util.waitUntilTextPresent(selenium, text);
			verifyTrue(selenium.isTextPresent(text));
		}catch(WaitTimedOutException e){
			final String msg = "Timed out exception occured while validating QC user credentials";
			Util.logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	private void navigateRespositoryMappingTabs(){
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Repository Mappings QC to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Repository Mappings TF to QC");
		selenium.waitForPageToLoad("30000");
	}
	
	private void navigateFailedShipmentTabs(){
		selenium.click("link=Failed Shipments");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Failed Shipments");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Failed Shipment QC to TF");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Failed Shipment TF to QC");
		selenium.waitForPageToLoad("30000");
	}
}
