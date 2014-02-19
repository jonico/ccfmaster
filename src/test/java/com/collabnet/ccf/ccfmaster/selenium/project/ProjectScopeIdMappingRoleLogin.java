package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.collabnet.ccf.ccfmaster.selenium.Util;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectScopeIdMappingRoleLogin extends ProjectScopeTestBase {

    public ProjectScopeIdMappingRoleLogin() {
        super();
        LoginInfo user = LoginInfo
                .projectIdentityMappingScopeFromSystemProperties();
        user.login(selenium);
    }

    @Test
    public void test01CheckIdMappingRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(
                roles,
                hasItems("ROLE_IDENTITY_MAPPINGS", "ROLE_IAF_USER",
                        "ROLE_TF_USER"));
    }

    @Test
    public void test02CheckFieldMappingRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(
                roles,
                hasItems("ROLE_MAPPING_RULE_TEMPLATES", "ROLE_IAF_USER",
                        "ROLE_TF_USER"));
    }

    @Test
    public void test03CheckIdMappings() {
        driver.get(Util.baseUrl() + "/CCFMaster/project/identitymappings");
        assertEquals("Identity Mappings", activeMenuEntry().getText());
    }

    @Test
    public void test04CheckFieldMappingTemplates() {
        driver.get(Util.baseUrl() + "/CCFMaster/project/fieldmappingtemplates");
        assertEquals("Field Mapping Templates", activeMenuEntry().getText());
    }
}
