package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.Rule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class CcfSeleneseTestBase extends SeleneseTestBase {

	protected static final Logger log = LoggerFactory.getLogger(CcfSeleneseTestBase.class);

	protected final WebDriver driver = SeleniumSuite.getDriver();
	protected final Selenium selenium = SeleniumSuite.getSelenium();
	
	@Rule
	public TestWatchman logScreens = new TestWatchman() {
		@Override
		public void failed(Throwable e, FrameworkMethod method) {
			log.info("Test {} failed.", method.getName());
			if (driver instanceof TakesScreenshot) {
				log.info("copy the following line to your driver address-bar to see the screenshot:\n" +
						 "data:image/png;base64,{}",
						((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));
			}
		}
	};
}