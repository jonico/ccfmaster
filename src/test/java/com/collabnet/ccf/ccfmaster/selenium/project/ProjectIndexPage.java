package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;

import org.junit.Test;

public class ProjectIndexPage extends ProjectScopeTestBase {

    public ProjectIndexPage() {
        super();
        user.login(selenium);
    }

    @Test
    public void checkRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(roles, hasItems("ROLE_TF_USER", "ROLE_IAF_USER"));
    }

    @Test
    public void welcomeMessage() {
        IndexPage index = new IndexPage(driver);
        assertThat(index.getWelcomeMessage(),
                containsString("Welcome to TeamForge Connector Server"));
    }
}
