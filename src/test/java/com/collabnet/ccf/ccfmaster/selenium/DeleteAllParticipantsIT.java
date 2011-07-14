package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.SeleneseTestBase;

public class DeleteAllParticipantsIT extends SeleneseTestBase {
//	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://localhost:8080/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	@Test
	public void testDeleteAllParticipantsIT() throws Exception {
		selenium.open("/CCFMaster/participants");
		while(!selenium.isTextPresent("No Participant found.")) {
			selenium.click("//input[@value='Delete Participant']");
			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
		}
		verifyFalse(selenium.isElementPresent("//input[@value='Delete Participant']"));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
