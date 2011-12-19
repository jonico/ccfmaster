package com.collabnet.ccf.ccfmaster.selenium;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;

public final class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);
	
	// prevent instantiation.
	private Util() {
	}

	
	/**
	 * retrieves the base url to use from the system property ccf.baseUrl. If
	 * not present, uses http://localhost:8080. The method removes all
	 * path/query components from the url.
	 * 
	 * @return the base url.
	 */
	public static String baseUrl() {
		String baseUrl = System.getProperty(
				"ccf.baseUrl",
				"http://localhost:8080/");
		// not interested in path.
		URI uri = URI.create(baseUrl);
		String port = uri.getPort() == -1 ? "" : ":" + uri.getPort();
		return String.format("%s://%s%s", uri.getScheme(), uri.getHost(), port);
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


	public static void logScreenshot(final String msg, Selenium selenium) {
		log.error(
				msg + "\ndata:image/png;base64,{}",
				selenium.captureScreenshotToString());
	}

	public static void logout(Selenium selenium) {
		selenium.open("/CCFMaster/resources/j_spring_security_logout");
	}
	
	public static void deleteAllParticipants(Selenium selenium) {
		selenium.open("/CCFMaster/participants");
		while(!selenium.isTextPresent("No Participant found.")) {
//			selenium.click("css=input[value='Delete Participant']");
//			selenium.click("//input[@value='Delete Participant']");
			selenium.submit("//form[@id='command']");
//			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
			selenium.waitForPageToLoad("300000");
		}
		assertFalse(selenium.isElementPresent("//input[@value='Delete Participant']"));
	}
	
	public static void createQCLandscape(Selenium selenium) {
		try {
			selenium.open("/CCFMaster/admin/ccfmaster");
			selenium.click("//input[@id='systemKind' and @value='QC']");
			selenium.click("link=Next");
			selenium.waitForPageToLoad("30000");
			selenium.type("landscape.name", "QC Landscape");
			selenium.type("participantUrlParticipantConfig.val", "http://example.org/qcbin/");
			selenium.type("participantUserNameLandscapeConfig.val", "invalid");
			selenium.type("participantPasswordLandscapeConfig.val", "invalid");
			selenium.type("tfUserNameLandscapeConfig.val", "admin");
			selenium.type("tfPasswordLandscapeConfig.val", "admin");
			selenium.click("link=Save");
			selenium.click("link=Save");
			selenium.waitForPageToLoad("30000");
			assertFalse("Found error ID on page.", selenium.isElementPresent("id=_title_ccferror_id"));
			assertTrue("Max Attachment Size not present on screen after create landscape", selenium.isTextPresent("Max Attachment Size"));
			assertThat(selenium.getValue("participantUrlParticipantConfig.val"), equalTo("http://example.org/qcbin/"));
			assertThat(selenium.getValue("participantUserNameLandscapeConfig.val"), equalTo("invalid") );
		} catch (RuntimeException e) {
			log.error("create QC landscape failed. Base64 screenshot:\n" +
					"data:image/png;base64,{}", selenium.captureScreenshotToString());
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
			selenium.type("landscape.name", "SWP Landscape");
			selenium.type("participantUrlParticipantConfig.val", "http://example.org");
			selenium.type("participantUserNameLandscapeConfig.val", "invalid");
			selenium.type("participantPasswordLandscapeConfig.val", "invalid");
			selenium.type("participantResyncUserNameLandscapeConfig.val", "invalidresync");
			selenium.type("participantResyncPasswordLandscapeConfig.val", "invalid");
			selenium.type("tfUserNameLandscapeConfig.val", "admin");
			selenium.type("tfPasswordLandscapeConfig.val", "admin");
			selenium.click("link=Save");
			selenium.waitForPageToLoad("30000");
			assertEquals("http://example.org/scrumworks-api/api2/scrumworks?wsdl", selenium.getValue("participantUrlParticipantConfig.val"));
			assertEquals("invalid", selenium.getValue("participantUserNameLandscapeConfig.val"));
			assertEquals("invalidresync", selenium.getValue("participantResyncUserNameLandscapeConfig.val"));
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
	
	public static void testRepositoryMappings(Selenium selenium){
		try{
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
		}catch (AssertionError e) {
			final String msg = "test Repository Mappings failed. Base64 screenshot:\n";
			logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	public static void testTeamforgeSettings(Selenium selenium){
		try{
			final String text = "Team Forge: Test Connection Success.";
			selenium.click("link=TeamForge");
			selenium.waitForPageToLoad("30000");
			selenium.type("tfUserNameLandscapeConfig.val", "ccfuser");
			selenium.type("tfPasswordLandscapeConfig.val", "ccfuser");
			selenium.click("link=Save");
			selenium.click("//button[2]");
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.isElementPresent("css=div.greenText"));
			selenium.click("//input[@value='Test Connection']");
			waitUntilTextPresent(selenium,text);
			assertTrue(selenium.isTextPresent(text)); 
		}catch (AssertionError e) {
			final String msg = "test Team forge Settings failed. Base64 screenshot:\n";
			logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	public static void testFailedShipments(Selenium selenium){
		try{
			testFailedShipmentOperations(selenium);
			testFailedShipmentFilter(selenium, "artf1785"); // need to give correct id to filter failed shipment (eg:artf 1788).
			testFailedShipmentRemoveFilter(selenium);
			testFailedShipmentDelete(selenium);
			
		}catch (AssertionError e) {
			final String msg = "testFailedShipments failed. Base64 screenshot:\n";
			logScreenshot(msg, selenium);
			throw e;
		}
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


	public static void testFailedShipmentDelete(Selenium selenium) {
		selenium.click("name=hospitalid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Delete");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("css=div.greenText"));
	}
	
	public static void testDeleteRepositoryMappings(Selenium selenium){
		try{
			selenium.click("link=Repository Mappings");
			selenium.waitForPageToLoad("30000");
			selenium.click("name=rmdid");
			selenium.chooseOkOnNextConfirmation();
			selenium.click("link=Delete");
			selenium.waitForPageToLoad("30000");
			assertTrue(selenium.isElementPresent("css=div.greenText"));
		}catch(AssertionError e) {
			final String msg = "test DeleteRepositoryMappings failed. Base64 screenshot:\n";
			logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	public static void testFailedShipmentCount(Selenium selenium){
		selenium.open("/CCFMaster/admin/displayrepositorymappingtftopart");
		selenium.click("link=Repository Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=rmdid");
		int failedShipmentCount =  Integer.valueOf(selenium.getEval("link=1"));
		selenium.click("link=1");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=hospitalid");
		//Xpath count always will be greater than 1 when compared with failedShipment List
		assertEquals( (failedShipmentCount+1),selenium.getXpathCount("//input[@type='checkbox']").intValue());
	}
	
	public static void testFailedShipmentFilter(Selenium selenium,String artifactId){
		selenium.waitForPageToLoad("30000");
		selenium.type("id=sourcefilterartifactid", artifactId); //artf1785
		selenium.click("css=a > strong");
		selenium.waitForPageToLoad("30000");
		selenium.click("css=#removefilterbutton > table.AppButtonTable > tbody > tr > td.AppButtonText > a > strong");
		selenium.waitForPageToLoad("30000");
	}
	
	public static void testFailedShipmentRemoveFilter(Selenium selenium){
		selenium.waitForPageToLoad("30000");
		selenium.click("css=#removefilterbutton > table.AppButtonTable > tbody > tr > td.AppButtonText > a > strong");
		selenium.waitForPageToLoad("30000");
	}
	
	public static void testStatus(Selenium selenium) {
		try {
			selenium.click("link=Status");
			selenium.waitForPageToLoad("30000");
			
				//assertEquals("STOPPED", selenium.getValue("id=currentStatus"));
				selenium.click("link=Start");
				for (int second = 0; second < 10; second++) {
					selenium.click("link=Refresh");
					selenium.waitForPageToLoad("30000");
					if (selenium.isTextPresent("STARTED")) {
						break;
					}
				}
				//assertEquals("STARTED", selenium.getValue("id=currentStatus"));
							selenium.click("link=Stop");
				for (int second = 0; second < 20; second++) {
					selenium.click("link=Refresh");
					selenium.waitForPageToLoad("30000");
					if (selenium.isTextPresent("STOPPED")) {
						break;
					}
				}
				//assertEquals("STOPPED", selenium.getValue("id=currentStatus"));
		} catch (AssertionError e) {
			final String msg = "testStatus failed. Base64 screenshot:\n";
			logScreenshot(msg, selenium);
			throw e;
		}
	}
	
	public static Wait waitUntilTextPresent(final Selenium selenium,final String text){
		return new Wait("Couldn't find Text !") {
		    public boolean until() {
		        return selenium.isTextPresent(text);
		    }
		};
	}
	
}
