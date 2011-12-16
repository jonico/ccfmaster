package com.collabnet.ccf.ccfmaster.selenium.project;

import org.junit.Before;
import org.junit.Test;

public class ProjectIdentityMappingsPage extends ProjectScopeTestBase {

	
	public ProjectIdentityMappingsPage() {
		super();
		user.login(selenium);
	}
	
	@Before
	public void openPage() {
		selenium.open("/CCFMaster/project/identitymappings?direction=FORWARD&size=1");
		
	}
	
	@Test
	public void editIdentityMappings() {
		selenium.click("css=img[alt=\"Details\"]");
		selenium.waitForPageToLoad("30000");
		selenium.type("id=targetArtifactVersion", "125");
		selenium.click("id=save");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isElementPresent("Identity Mapping details saved successfully"));	
		selenium.click("link=Return");
		selenium.waitForPageToLoad("30000");
	}
	
	@Test
	public void filterIdentityMappings(){
		final String sourceArtifactId_id="artf1053";
		selenium.click("link=Identity Mappings");
		selenium.type("id=sourcefilterartifactid", sourceArtifactId_id);
		selenium.click("link=Apply");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Remove");
		selenium.waitForPageToLoad("30000");
	}
	
	@Test
	public void deleteIdentityMappings(){
		selenium.click("link=Identity Mappings");
		selenium.waitForPageToLoad("30000");
		selenium.click("name=mappingid");
		selenium.chooseOkOnNextConfirmation();
		selenium.click("link=Delete");
		selenium.waitForPageToLoad("30000");
		assertEquals("Selected Identity Mapping deleted successfully", selenium.getText("css=div.greenText"));
	}	
		
}
