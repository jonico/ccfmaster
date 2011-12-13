package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.project.LoginInfo;
import com.thoughtworks.selenium.Selenium;

public class QCSettings extends CcfAuthenticatedTestBase {

	@BeforeClass
	public static void createLandscape() {
		
		Selenium selenium = SeleniumSuite.getSelenium();
		try{
			Util.deleteAllParticipants(selenium);
			Util.createQCLandscape(selenium);
			
			final LoginInfo loginInfo = LoginInfo.projectScopeFromSystemProperties();
			// ProjectController called after login implicitly creates external app object
			loginInfo.login(selenium);
			selenium.open("/CCFMaster/externalapps/");
			selenium.waitForPageToLoad("30000");
			Assert.assertFalse(selenium.isTextPresent("No External App found."));
			
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
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("id=participantUrlParticipantConfig.val.errors"));
		verifyTrue(selenium.isTextPresent("Enter a valid HPQC url ends with qcbin/"));
		selenium.type("participantUrlParticipantConfig.val", "www.hpurl.com.qcbin/");
		selenium.type("participantPasswordLandscapeConfig.val", "admin123");
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
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> added");
		selenium.click("link=Save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("link=Log Template QC to TF");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>");
		selenium.click("link=Cancel");
		selenium.waitForPageToLoad("30000");
		selenium.type("val", "An Artifact has been quarantined.\n\nSOURCE_SYSTEM_ID: <SOURCE_SYSTEM_ID>\n\nSOURCE_REPOSITORY_ID: <SOURCE_REPOSITORY_ID>\n\nSOURCE_ARTIFACT_ID: <SOURCE_ARTIFACT_ID>\n\nTARGET_SYSTEM_ID: <TARGET_SYSTEM_ID>\n\nTARGET_REPOSITORY_ID: <TARGET_REPOSITORY_ID>\n\nTARGET_ARTIFACT_ID: <TARGET_ARTIFACT_ID>\n\nERROR_CODE: <ERROR_CODE>\n\nTIMESTAMP: <TIMESTAMP>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME>\n\nEXCEPTION_CLASS_NAME: <EXCEPTION_CLASS_NAME> qdded");
		selenium.click("link=Save");
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
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=rmdid");
		selenium.click("link=Pause");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("name=rmdid");
		selenium.click("link=Resume");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));			
	}

	
	
	@Test
	public void fieldMappingTemplates(){
		selenium.click("link=Field Mapping Templates");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=fmtid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Delete");
		assertEquals("Default Field Mapping Template(s) cannot be deleted", selenium.getAlert());
		
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
	public void importFieldMappingTemplates(){
		selenium.click("link=Field Mapping Templates");
		selenium.open("/CCFMaster/admin/uploadfieldmappingtemplate?direction=forward");
		selenium.waitForPageToLoad("30000");
		//selenium.type("id=file", "C:\\Users\\selvakumar\\Desktop\\Default TF Planning Folder to QC Requirement.xml");
		//XML  file stored on below path in cu116
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
	public void failedShipments(){
		selenium.click("link=Failed Shipments");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=hospitalid");
		selenium.click("link=Replay");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
		selenium.click("name=hospitalid");
		selenium.click("link=Examine");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Return");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=hospitalid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Delete");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
	}
	
	/**
	 * Delete repository mappings after executing hospital and identity mappings tests
	 */
	/*@Test
	public void deleteRepositoryMappings() {
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=rmdid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Delete");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("css=div.greenText"));
	}*/
	
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
		selenium.click("link=Logs QC to TF");
		selenium.waitForPageToLoad("30000");

	}
}
