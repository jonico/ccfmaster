package com.collabnet.ccf.ccfmaster.selenium;

import org.junit.Test;
import org.openqa.selenium.By;

public class TFSessionTimeout extends CcfSeleneseTestBase {

    @Test
    public void errorPageOnTimeout() {
        Util.login(selenium, "admin", "admin");
        driver.get(Util.baseUrl() + "/CCFMaster/chuck/timeout");
        //WebElement h3 = driver.findElement(By.tagName("h3"));
        //assertThat(h3.getText(), containsString("TeamForge session timeout"));
        assertEquals("TeamForge session timeout",
                driver.findElement(By.cssSelector("tr.ContainerHeader > td"))
                        .getText());
    }
}
