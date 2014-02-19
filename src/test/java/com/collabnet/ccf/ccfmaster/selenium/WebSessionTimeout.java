package com.collabnet.ccf.ccfmaster.selenium;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebSessionTimeout extends CcfSeleneseTestBase {

    @Test
    public void test01ErrorPageOnTimeout() {
        Util.login(selenium, "admin", "admin");
        driver.get(Util.baseUrl() + "/CCFMaster/chuck/killSession");
        WebElement h3 = driver.findElement(By.tagName("h3"));
        assertThat(
                h3.getText(),
                containsString("You just logged out or your previous login expired."));
    }
}
