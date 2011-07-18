package com.collabnet.ccf.ccfmaster.selenium;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class CcfSeleneseTestBase extends SeleneseTestBase {

	protected static Selenium staticSelenium;
	protected static final Logger log = LoggerFactory.getLogger(CcfSeleneseTestBase.class);
	protected static WebDriver browser;
	protected Selenium selenium;
	
	public CcfSeleneseTestBase() {
		this(staticSelenium);
	}
	
	protected CcfSeleneseTestBase(Selenium selenium) {
		super();
		this.selenium = selenium;
	}

	@BeforeClass
	public static void launchBrowser() throws URISyntaxException {
		String baseUrl = System.getProperty("ccf.baseUrl","http://localhost:8080/");
		// not interested in path.
		URI uri = new URI(baseUrl);
		String port = uri.getPort() == -1 ? "" : ":" + uri.getPort();
		baseUrl = String.format("%s://%s%s/", uri.getScheme(), uri.getHost(), port);
		log.info("Launching Firefox with baseUrl: {}", baseUrl);
		browser = new FirefoxDriver();
		staticSelenium = new WebDriverBackedSelenium(browser, baseUrl);
	}

	@AfterClass
	public static void stopBrowser() throws Exception {
		log.info("Stopping browser.");
		staticSelenium.stop();
	}

}