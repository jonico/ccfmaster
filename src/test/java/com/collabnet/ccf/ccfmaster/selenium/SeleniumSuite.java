package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * based on selenium4junit: http://code.google.com/p/selenium4junit/
 */
public class SeleniumSuite extends Suite {
    private static final ThreadLocal<WebDriverBackedSelenium> seleniumHolder = new ThreadLocal<WebDriverBackedSelenium>();
    private static final ThreadLocal<WebDriver>               driverHolder   = new ThreadLocal<WebDriver>();

    public SeleniumSuite(Class<?> clazz, RunnerBuilder builder)
            throws InitializationError {
        super(clazz, builder);
    }

    @Override
    public void run(final RunNotifier notifier) {
        String baseUrl = Util.baseUrl();
        WebDriver driver = new FirefoxDriver();
        WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver,
                baseUrl);
        driverHolder.set(driver);
        seleniumHolder.set(selenium);
        try {
            super.run(notifier);
        } finally {
            seleniumHolder.set(null);
            driverHolder.set(null);
            selenium.stop(); // calls close on driver
        }
    }

    /**
     * Returns the reference to the current {@link WebDriver}.
     * <p>
     * Test classes can safely call this method in non-static blocks since the
     * session is started before the actual test class gets instantiated by the
     * runner.
     * 
     * @return Selenium API for executing commands on the current driver.
     */
    public static WebDriver getDriver() {
        return driverHolder.get();
    }

    /**
     * Returns the reference to the current {@link Selenium} session.
     * <p>
     * Test classes can safely call this method in non-static blocks since the
     * session is started before the actual test class gets instantiated by the
     * runner.
     * 
     * @return Selenium API for executing commands on the current session.
     */
    public static WebDriverBackedSelenium getSelenium() {
        return seleniumHolder.get();
    }
}
