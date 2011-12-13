package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(SeleniumSuite.class)
@SuiteClasses({
	TFSessionTimeout.class,
	WebSessionTimeout.class,
})
public class SessionTimeoutIT {
}
