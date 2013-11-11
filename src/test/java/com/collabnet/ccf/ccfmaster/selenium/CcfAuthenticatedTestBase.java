package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.collabnet.ccf.ccfmaster.selenium.project.LoginInfo;

/**
 * This tests logs into a CCFMaster instance before the test and logs out
 * afterwards.
 */
public class CcfAuthenticatedTestBase extends CcfSeleneseTestBase {

    protected final String        username;
    protected final String        password;
    protected static final String staticUsername;
    protected static final String staticPassword;
    static {
        /*
         * Takes the username and password to use from the system properties
         * named {@link #USERNAME_SYSTEM_PROPERTY} and {@link
         * #PASSWORD_SYSTEM_PROPERTY}. If not set, uses "admin" for both.
         */
        staticUsername = System.getProperty(LoginInfo.USERNAME_SYSTEM_PROPERTY,
                "admin");
        staticPassword = System.getProperty(LoginInfo.PASSWORD_SYSTEM_PROPERTY,
                "admin");
    }

    /**
     * Default constructor. Takes the username and password to use from the
     * system properties named {@link LoginInfo#USERNAME_SYSTEM_PROPERTY} and
     * {@link LoginInfo#PASSWORD_SYSTEM_PROPERTY}. If not set, uses "admin" for
     * both.
     */
    public CcfAuthenticatedTestBase() {
        this(staticUsername, staticPassword);
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

    protected void login() {
        Util.login(selenium, username, password);
    }

    protected void logout() {
        Util.logout(selenium);
    }

    @BeforeClass
    public static void staticLogin() {
        Util.login(SeleniumSuite.getSelenium(), staticUsername, staticPassword);
    }

    @AfterClass
    public static void staticLogout() {
        Util.logout(SeleniumSuite.getSelenium());
    }
}