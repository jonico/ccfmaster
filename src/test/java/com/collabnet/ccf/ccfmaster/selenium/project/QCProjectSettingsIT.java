package com.collabnet.ccf.ccfmaster.selenium.project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.controller.web.project.ProjectIndexController;
import com.collabnet.ccf.ccfmaster.selenium.MockDataUtil;
import com.collabnet.ccf.ccfmaster.selenium.SeleniumSuite;
import com.collabnet.ccf.ccfmaster.selenium.Util;
import com.thoughtworks.selenium.Selenium;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

@RunWith(SeleniumSuite.class)
@SuiteClasses({
	ProjectIndexPage.class,
	ProjectRepositoryMappingsPage.class,
	ProjectScopeHospitalRoleLogin.class,
	ProjectScopeRMRoleLogin.class,
	ProjectScopeIdMappingRoleLogin.class,
})
public class QCProjectSettingsIT {
	private static final Logger log = LoggerFactory.getLogger(QCProjectSettingsIT.class);
	
	@BeforeClass
	public static void createLandscape() {
		try {
			Selenium selenium = SeleniumSuite.getSelenium();
			LoginInfo user = LoginInfo.siteAdminFromSystemProperties();
			user.login(selenium);
			Util.deleteAllParticipants(selenium);
			Util.createQCLandscape(selenium);
			final LoginInfo loginInfo = LoginInfo.projectScopeFromSystemProperties();
			// ProjectController called after login implicitly creates external app object
			loginInfo.login(selenium);
			assertThat(selenium.getLocation(), containsString(ProjectIndexController.PROJECT_INDEX_PATH));
			MockDataUtil.createRepositoryMappingAndRepositoryMappingDirection(selenium);
			MockDataUtil.createFailedShipments(selenium);
		} catch (RuntimeException e) {
			WebDriver driver = SeleniumSuite.getDriver();
			if (driver instanceof TakesScreenshot) {
				log.error("copy the following line to your driver address-bar to see the screenshot:\n" +
						 "data:image/png;base64,{}",
						((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));
			}
			throw e;
		}
	}
	
	@AfterClass
	public static void destroyLandscape() {
		Selenium selenium = SeleniumSuite.getSelenium();
		LoginInfo user = LoginInfo.siteAdminFromSystemProperties();
		user.login(selenium);
		Util.deleteAllParticipants(selenium);
	}

}
