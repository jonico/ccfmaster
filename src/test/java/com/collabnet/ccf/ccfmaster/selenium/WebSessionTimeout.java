package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

public class WebSessionTimeout extends CcfSeleneseTestBase {

    @Test
    public void errorPageOnTimeout() {
        Util.login(selenium, "admin", "admin");
        driver.get(Util.baseUrl() + "/CCFMaster/chuck/killSession");
        WebElement h3 = driver.findElement(By.tagName("h3"));
        assertThat(
                h3.getText(),
                containsString("You just logged out or your previous login expired."));
    }
}
