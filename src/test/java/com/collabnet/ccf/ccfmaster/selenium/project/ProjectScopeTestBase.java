package com.collabnet.ccf.ccfmaster.selenium.project;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.selenium.CcfSeleneseTestBase;


public class ProjectScopeTestBase extends CcfSeleneseTestBase {
	
	protected final Logger log = LoggerFactory.getLogger(ProjectScopeTestBase.class);
	protected final LoginInfo user;

	public ProjectScopeTestBase(LoginInfo user) {
		this.user = user;
		log.debug("using user: {}", user);
	}
	
	public ProjectScopeTestBase() {
		this(LoginInfo.projectScopeFromSystemProperties());
	}

	/**
	 * @return the WebElement that corresponds to the selected menu entry in the left menu bar
	 */
	protected WebElement activeMenuEntry() {
		return driver.findElement(By.cssSelector("div.ImageListParentSelectedNoTop"));
	}
}
