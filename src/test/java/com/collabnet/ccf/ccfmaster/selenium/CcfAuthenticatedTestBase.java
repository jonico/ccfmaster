package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.After;
import org.junit.Before;

/**
 * This tests logs into a CCFMaster instance before the test and logs out
 * afterwards.
 */
public class CcfAuthenticatedTestBase extends CcfSeleneseTestBase {

	public static final String USERNAME_SYSTEM_PROPERTY = "ccf.selenium.username";
	public static final String PASSWORD_SYSTEM_PROPERTY = "ccf.selenium.password";

	protected final String username;
	protected final String password;

	/**
	 * Default constructor. Takes the username and password to use from the
	 * system properties named {@link #USERNAME_SYSTEM_PROPERTY} and
	 * {@link #PASSWORD_SYSTEM_PROPERTY}. If not set, uses "admin" for both.
	 */
	public CcfAuthenticatedTestBase() {
		this(System.getProperty(USERNAME_SYSTEM_PROPERTY, "admin"),
			 System.getProperty(PASSWORD_SYSTEM_PROPERTY, "admin"));
	}

	/**
	 * This constructor can be used to specify the username and password.
	 * 
	 * @param username
	 * @param password
	 */
	public CcfAuthenticatedTestBase(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Before
	public void login() {
		try {
			selenium.open("/CCFMaster/login");
			// selenium.click("j_username");
			selenium.type("j_username", username);
			selenium.type("j_password", password);
			selenium.click("proceed");
			selenium.waitForPageToLoad("30000");
		} catch (RuntimeException e) {
			log.error("Exception while logging in", e);
			log.error("Base64 screenshot\n:{}",
					selenium.captureScreenshotToString());
			throw e;
		}
	}

	@After
	public void logout() {
		selenium.open("/CCFMaster/resources/j_spring_security_logout");
	}

}