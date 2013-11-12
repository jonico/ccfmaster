package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.collabnet.ccf.ccfmaster.selenium.Util;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;

public class ProjectRepositoryMappingsPage extends ProjectScopeTestBase {

    private RepositoryMappingsPage page;

    public ProjectRepositoryMappingsPage() {
        super();
        user.login(selenium);
    }

    @Before
    public void openPage() {
        driver.get(Util.baseUrl()
                + "/CCFMaster/project/repositorymappings?direction=FORWARD&size=1");
        assertEquals("Repository Mappings", activeMenuEntry().getText());
        page = new RepositoryMappingsPage(driver);
    }

    @Test
    public void test01TabsPresent() {
        assertThat(page.getDirection(), is(Directions.FORWARD));

        List<WebElement> tabs = page.getTabs();
        assertThat(tabs.size(), equalTo(2));
        assertThat(tabs.get(0).getText(),
                equalTo(RepositoryMappingsPage.TF_2_QC_LABEL));
        assertThat(tabs.get(1).getText(),
                equalTo(RepositoryMappingsPage.QC_2_TF_LABEL));
    }

    @Test
    public void test02Pagination() {
        PaginatedPage pagination = new PaginatedPage(driver);
        assertThat(pagination.getPageNumber(), equalTo(1));
        validatePagination(pagination);
        if (pagination.hasNextLink()) {
            pagination.openNextPage();
            validatePagination(pagination);
        }
        if (pagination.hasPrevLink()) {
            pagination.openPrevPage();
            validatePagination(pagination);
        }
    }

    @Test
    public void test03RepositoryMappingOperation() {
        Util.testRepositoryMappings(selenium);
        Util.testFailedShipmentCount(selenium);
        Util.testcreateFieldMapping(selenium);
        Util.testcreateLinkFieldMapping(selenium);
        Util.testAssociateFieldMapping(selenium);
        Util.testDeleteRepositoryMappings(selenium);// only for admin roles
    }

    /**
     * @param pagination
     */
    private void validatePagination(PaginatedPage pagination) {
        final int myPage = pagination.getPageNumber();
        final int numPages = pagination.getNumberOfPages();
        if (myPage < numPages) {
            assertTrue(String.format("no next link on page %d/%d", myPage,
                    numPages), pagination.hasNextLink());
            assertTrue(String.format("no next last on page %d/%d", myPage,
                    numPages), pagination.hasLastLink());
        }
        if (myPage > 1) {
            assertTrue(String.format("no prev link on page %d/%d", myPage,
                    numPages), pagination.hasPrevLink());
            assertTrue(String.format("no first link on page %d/%d", myPage,
                    numPages), pagination.hasFirstLink());
        }
    }
}
