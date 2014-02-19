package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class CcfSeleneseTestBase extends SeleneseTestBase {

    private static final String   SCREENSHOT_MESSAGE = "copy the following line to your driver address-bar to see the screenshot:";
    protected static final Logger log                = LoggerFactory
                                                             .getLogger(CcfSeleneseTestBase.class);

    protected final WebDriver     driver             = SeleniumSuite
                                                             .getDriver();

    protected final Selenium      selenium           = SeleniumSuite
                                                             .getSelenium();
    @Rule
    public TestWatcher            logScreens         = new TestWatcher() {
                                                         @Override
                                                         protected void failed(
                                                                 Throwable e,
                                                                 Description description) {
                                                             log.error(
                                                                     "Test {} failed.",
                                                                     description
                                                                             .getMethodName());
                                                             if (driver instanceof TakesScreenshot) {
                                                                 log.error(
                                                                         SCREENSHOT_MESSAGE
                                                                                 + "\ndata:image/png;base64,{}",
                                                                         ((TakesScreenshot) driver)
                                                                                 .getScreenshotAs(OutputType.BASE64));
                                                             }
                                                         }
                                                     };

    /**
     * @param selenium
     */
    protected static void logScreenshot(Selenium selenium) {
        Util.logScreenshot(SCREENSHOT_MESSAGE, selenium);
    }
}