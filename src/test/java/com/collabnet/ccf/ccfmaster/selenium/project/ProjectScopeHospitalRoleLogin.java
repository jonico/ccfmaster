package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.junit.Assert.assertThat;

import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.Util;


public class ProjectScopeHospitalRoleLogin extends ProjectScopeTestBase{

	public ProjectScopeHospitalRoleLogin() {
		super();
		LoginInfo user = LoginInfo.projectHospitalScopeFromSystemProperties();
		user.login(selenium);
	}
	
	
	@Test
	public void checkRoles() {
		IndexPage index = new IndexPage(driver);
		final List<String> roles = index.getRoles();
		assertThat(roles, hasItems("ROLE_HOSPITAL", "ROLE_IAF_USER","ROLE_TF_USER"));
	}
	
	@Test
	public void checkHospital() {
		driver.get(Util.baseUrl() + "/CCFMaster/project/hospitalentrys?direction=FORWARD&size=1");
		assertEquals("Failed Shipments", activeMenuEntry().getText());
		Util.testFailedShipmentOperations(selenium);
		Util.testFailedShipmentFilter(selenium, "artf1785");
		Util.testFailedShipmentRemoveFilter(selenium);
		Util.testFailedShipmentDelete(selenium);
	}
}
