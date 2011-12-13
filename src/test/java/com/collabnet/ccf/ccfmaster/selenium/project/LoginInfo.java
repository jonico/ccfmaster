package com.collabnet.ccf.ccfmaster.selenium.project;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.selenium.Util;
import com.thoughtworks.selenium.Selenium;

public class LoginInfo {
	
	private final String userName;
	private final String password;
	private final String linkId;
	public static final String PASSWORD_SYSTEM_PROPERTY = "ccf.selenium.password";
	public static final String USERNAME_SYSTEM_PROPERTY = "ccf.selenium.username";
	
	public static final String HOSPITAL_PASSWORD_SYSTEM_PROPERTY = "ccf.selenium.password.hospital";
	public static final String HOSPITAL_USERNAME_SYSTEM_PROPERTY = "ccf.selenium.username.hospital";
	
	public static final String IDMAPPING_PASSWORD_SYSTEM_PROPERTY = "ccf.selenium.password.idmapping";
	public static final String IDMAPPING_USERNAME_SYSTEM_PROPERTY = "ccf.selenium.username.idmapping";
	
	public static final String REPO_MAPPING_PASSWORD_SYSTEM_PROPERTY = "ccf.selenium.password.repomapping";
	public static final String REPO_MAPPING_USERNAME_SYSTEM_PROPERTY = "ccf.selenium.username.repomapping";
	
	public static final String LINKID_SYSTEM_PROPERTY = "ccf.selenium.linkId";

	public LoginInfo(String userName, String password, String linkId) {
		Assert.hasText(userName);
		Assert.isTrue(userName.indexOf('/') == -1, "username must not contain slashes, was: " + userName);
		Assert.hasText(password);
		this.userName = userName;
		this.password = password;
		this.linkId = linkId;
	}
	
	public static LoginInfo fromSystemProperties() {
		String user = System.getProperty(USERNAME_SYSTEM_PROPERTY, "admin");
		String pass = System.getProperty(PASSWORD_SYSTEM_PROPERTY, "admin");
		String linkId = System.getProperty(LINKID_SYSTEM_PROPERTY, null);
		return new LoginInfo(user, pass, linkId);
	}
	
	public static LoginInfo projectScopeFromSystemProperties() {
		String user = System.getProperty(USERNAME_SYSTEM_PROPERTY, "admin");
		String pass = System.getProperty(PASSWORD_SYSTEM_PROPERTY, "admin");
		String linkId = System.getProperty(LINKID_SYSTEM_PROPERTY, null);
		Assert.notNull(linkId);
		return new LoginInfo(user, pass, linkId);
	}

	public static LoginInfo siteAdminFromSystemProperties() {
		String user = System.getProperty(USERNAME_SYSTEM_PROPERTY, "admin");
		String pass = System.getProperty(PASSWORD_SYSTEM_PROPERTY, "admin");
		return new LoginInfo(user, pass, null);
	}

	public static LoginInfo projectHospitalScopeFromSystemProperties() {
		String user = System.getProperty(HOSPITAL_USERNAME_SYSTEM_PROPERTY, "seleniumhospital");
		String pass = System.getProperty(HOSPITAL_PASSWORD_SYSTEM_PROPERTY, "seleniumhospital");
		String linkId = System.getProperty(LINKID_SYSTEM_PROPERTY, null);
		Assert.notNull(linkId);
		return new LoginInfo(user, pass, linkId);
	}
	
	public static LoginInfo projectRepositoryMappingScopeFromSystemProperties() {
		String user = System.getProperty(REPO_MAPPING_USERNAME_SYSTEM_PROPERTY, "seleniumrepomapping");
		String pass = System.getProperty(REPO_MAPPING_PASSWORD_SYSTEM_PROPERTY, "seleniumrepomapping");
		String linkId = System.getProperty(LINKID_SYSTEM_PROPERTY, null);
		Assert.notNull(linkId);
		return new LoginInfo(user, pass, linkId);
	}
	
	public static LoginInfo projectIdentityMappingScopeFromSystemProperties() {
		String user = System.getProperty(IDMAPPING_USERNAME_SYSTEM_PROPERTY, "seleniumidmapping");
		String pass = System.getProperty(IDMAPPING_PASSWORD_SYSTEM_PROPERTY, "seleniumidmapping");
		String linkId = System.getProperty(LINKID_SYSTEM_PROPERTY,null);
		Assert.notNull(linkId);
		return new LoginInfo(user, pass, linkId);
	}
	
	public void login(Selenium selenium) {
		String user = linkId == null ? userName : String.format("%s/%s", userName, linkId);
		Util.login(selenium, user, password);
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getLinkId() {
		return linkId;
	}
	
	@Override
	public String toString() {
		return String.format("LoginInfo(%s, %s, %s)", userName, password, linkId);
	}
}