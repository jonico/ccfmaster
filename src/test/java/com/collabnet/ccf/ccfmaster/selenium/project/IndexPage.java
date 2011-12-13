package com.collabnet.ccf.ccfmaster.selenium.project;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class IndexPage {
	private final WebDriver driver;
	private static final Splitter roleSplitter = Splitter.on(',').trimResults();
	
	public IndexPage(WebDriver driver) {
		final String currentUrl = driver.getCurrentUrl();
		Preconditions.checkState(
				currentUrl.endsWith("/CCFMaster/project/"),
				"Current page is not index page",
				currentUrl);
		this.driver = driver;
	}
	
	public String getWelcomeMessage() {
		return driver.findElement(By.id("welcome")).getText();
	}
	
	public String getRoleString() {
		return driver.findElement(By.id("roles")).getText();
	}
	
	public List<String> getRoles() {
		String roleString = getRoleString();
		// trim brackets (first and last char)
		roleString = roleString.substring(1, roleString.length() - 1);
		return Lists.newArrayList(roleSplitter.split(roleString));
	}
	
}
