package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.Util;

public class ProjectScopeIdMappingRoleLogin extends ProjectScopeTestBase {

    public ProjectScopeIdMappingRoleLogin() {
        super();
        LoginInfo user = LoginInfo
                .projectIdentityMappingScopeFromSystemProperties();
        user.login(selenium);
    }

    @Test
    public void checkFieldMappingRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(
                roles,
                hasItems("ROLE_MAPPING_RULE_TEMPLATES", "ROLE_IAF_USER",
                        "ROLE_TF_USER"));
    }

    @Test
    public void checkFieldMappingTemplates() {
        driver.get(Util.baseUrl() + "/CCFMaster/project/fieldmappingtemplates");
        assertEquals("Field Mapping Templates", activeMenuEntry().getText());
    }

    @Test
    public void checkIdMappingRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(
                roles,
                hasItems("ROLE_IDENTITY_MAPPINGS", "ROLE_IAF_USER",
                        "ROLE_TF_USER"));
    }

    @Test
    public void checkIdMappings() {
        driver.get(Util.baseUrl() + "/CCFMaster/project/identitymappings");
        assertEquals("Identity Mappings", activeMenuEntry().getText());
    }
}
