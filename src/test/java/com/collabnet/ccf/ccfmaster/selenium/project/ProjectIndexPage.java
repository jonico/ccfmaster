package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectIndexPage extends ProjectScopeTestBase {

    public ProjectIndexPage() {
        super();
        user.login(selenium);
    }

    @Test
    public void test01WelcomeMessage() {
        IndexPage index = new IndexPage(driver);
        assertThat(index.getWelcomeMessage(),
                containsString("Welcome to TeamForge Connector Server"));
    }

    @Test
    public void test02CheckRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(roles, hasItems("ROLE_TF_USER", "ROLE_IAF_USER"));
    }
}
