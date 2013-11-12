package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;

import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.Util;

public class ProjectScopeHospitalRoleLogin extends ProjectScopeTestBase {

    public ProjectScopeHospitalRoleLogin() {
        super();
        LoginInfo user = LoginInfo.projectHospitalScopeFromSystemProperties();
        user.login(selenium);
    }

    @Test
    public void test01CheckRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(roles,
                hasItems("ROLE_HOSPITAL", "ROLE_IAF_USER", "ROLE_TF_USER"));
    }

    @Test
    public void test02CheckHospital() {
        driver.get(Util.baseUrl()
                + "/CCFMaster/project/hospitalentrys?direction=FORWARD&size=1");
        assertEquals("Failed Shipments", activeMenuEntry().getText());
        navigateFailedShipmentTabs();
        Util.testFailedShipmentOperations(selenium);
        Util.testFailedShipmentFilter(selenium, "artf1785");
        Util.testFailedShipmentRemoveFilter(selenium);
        Util.testFailedShipmentDelete(selenium);
    }

    private void navigateFailedShipmentTabs() {
        selenium.click("link=Failed Shipments");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=" + RepositoryMappingsPage.QC_2_TF_LABEL);
        selenium.waitForPageToLoad("30000");
        selenium.click("link=" + RepositoryMappingsPage.TF_2_QC_LABEL);
        selenium.waitForPageToLoad("30000");
    }
}
