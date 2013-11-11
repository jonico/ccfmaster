package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.project.LoginInfo;
import com.thoughtworks.selenium.Selenium;

public class TFRespositoryIdBelongstoExternalAppVerify extends CcfAuthenticatedTestBase {

    @Test
    public void verifyTFRepositoryIdBelongsToExternalApp() {
        final String description = "Repo 2";
        final String tfRepoId = "tracker1032";
        final String partRepoId = "12";
        selenium.open("/CCFMaster/repositorymappings/?form");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=_description_id");
        selenium.type("id=_description_id", description);
        selenium.type("id=_teamForgeRepositoryId_id", tfRepoId);
        selenium.type("id=_participantRepositoryId_id", partRepoId);
        selenium.click("id=proceed");
        selenium.waitForPageToLoad("30000");
        selenium.click("//div[@id='_title__message_id']/div/div");
        Assert.assertTrue(selenium
                .isTextPresent("TeamForge Repository Id does not belong to the External App."));
    }

    @BeforeClass
    public static void createLandscape() {

        Selenium selenium = SeleniumSuite.getSelenium();
        try {
            Util.deleteAllParticipants(selenium);
            Util.createQCLandscape(selenium);

            final LoginInfo loginInfo = LoginInfo
                    .projectScopeFromSystemProperties();
            // ProjectController called after login implicitly creates external app object
            loginInfo.login(selenium);
            selenium.open("/CCFMaster/externalapps/");
            selenium.waitForPageToLoad("30000");
            Assert.assertFalse(selenium.isTextPresent("No External App found."));
        } catch (RuntimeException e) {
            logScreenshot(selenium);
            throw e;
        }
    }

    @AfterClass
    public static void destroyLandscape() {
        Selenium selenium = SeleniumSuite.getSelenium();
        Util.deleteAllParticipants(selenium);
    }
}
