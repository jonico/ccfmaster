package com.collabnet.ccf.ccfmaster.selenium.project;

import org.junit.Before;
import org.junit.Test;

public class ProjectFieldMappingsPage extends ProjectScopeTestBase {

    public ProjectFieldMappingsPage() {
        super();
        user.login(selenium);
    }

    public void exportFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.waitForPageToLoad("30000");
        selenium.click("name=fmtid");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("link=Export");
    }

    @Before
    public void openPage() {
        selenium.open("/CCFMaster/project/fieldmappingtemplates?direction=FORWARD&size=1");

    }

    @Test
    public void test01ImportFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.open("/CCFMaster/project/fieldmappingtemplates/upload?direction=FORWARD");
        selenium.waitForPageToLoad("30000");
        //XML file should be stored on below path in the machine where the selenium tests is running.
        //TODO check if we can export the field mapping template to the below path 
        selenium.type("id=file",
                "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
        selenium.click("link=Next");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Import Selected");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Return");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium
                .isElementPresent("Default TF Planning Folder to QC Requirement"));
    }

    @Test
    public void test02mergeFieldMappingTemplates() {
        selenium.click("link=Field Mapping Templates");
        selenium.open("/CCFMaster/project/fieldmappingtemplates/upload?direction=FORWARD");
        selenium.waitForPageToLoad("30000");
        //XML file should be stored on below path in the machine where the selenium tests is running.
        //TODO check if we can export the field mapping template to the below path 
        selenium.type("id=file",
                "C:\\Selenium automated\\Default TF Planning Folder to QC Requirement.xml");
        selenium.click("link=Next");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=textbox",
                "Default TF Planning Folder to QC Requirement");
        selenium.click("link=Import Selected");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("Updated Successfully"));
    }

    @Test
    public void test03ExportAndDeleteFieldMappingTemplate() {
        exportFieldMappingTemplates();
        selenium.click("link=Field Mapping Templates");
        selenium.open("/CCFMaster/project/fieldmappingtemplates?direction=REVERSE");
        selenium.waitForPageToLoad("30000");
        selenium.open("/CCFMaster/project/fieldmappingtemplates?direction=FORWARD");
        selenium.waitForPageToLoad("30000");
        //selenium.check("name=fmtid value=new Default TF Planning Folder to QC Requirement");
        selenium.click("xpath=(//input[@type='checkbox'])[last()]");
        selenium.chooseOkOnNextConfirmation();
        selenium.click("link=Delete");
        selenium.waitForPageToLoad("30000");
        assertEquals("Selected Field Mapping Templates deleted successfully",
                selenium.getText("css=div.greenText"));
    }

}
