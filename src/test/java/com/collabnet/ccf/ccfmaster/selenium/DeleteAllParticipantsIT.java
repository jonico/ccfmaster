package com.collabnet.ccf.ccfmaster.selenium;


import org.junit.Test;


public class DeleteAllParticipantsIT extends CcfAuthenticatedTestBase {

	@Test
	public void testDeleteAllParticipantsIT() throws Exception {
		selenium.open("/CCFMaster/participants");
		while(!selenium.isTextPresent("No Participant found.")) {
			selenium.click("//input[@value='Delete Participant']");
			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
		}
		verifyFalse(selenium.isElementPresent("//input[@value='Delete Participant']"));
	}
}
