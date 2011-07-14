package com.example.tests;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class DELETEME extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://localhost:8080/");
		selenium.start();
	}

	@Test
	public void testDELETEME() throws Exception {
		selenium.open("/CCFMaster/participants");
		while(selenium.isElementPresent("//input[@value='Delete Participant']")) {
			selenium.click("//input[@value='Delete Participant']");
			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
			selenium.open("/CCFMaster/participants");
		}
		assertTrue(selenium.isTextPresent("No Participant found."));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
