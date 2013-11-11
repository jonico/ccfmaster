package com.collabnet.ccf.ccfmaster.selenium.project;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PaginatedPage {
    // defined in pagination.tagx
    private static final String  PAGE_NUMBERS    = "page_numbers";
    private static final String  LAST_PAGE_LINK  = "last_page_link";
    private static final String  NEXT_PAGE_LINK  = "next_page_link";
    private static final String  PREV_PAGE_LINK  = "prev_page_link";
    private static final String  FIRST_PAGE_LINK = "first_page_link";

    // depends on list_page in messages.properties
    private static final Pattern PAGE_PATTERN    = Pattern
                                                         .compile("(\\d+) of (\\d+)");
    private final WebDriver      driver;

    public PaginatedPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getFirstLink() {
        return findId(FIRST_PAGE_LINK);
    }

    public WebElement getLastLink() {
        return findId(LAST_PAGE_LINK);
    }

    public WebElement getNextLink() {
        return findId(NEXT_PAGE_LINK);
    }

    public int getNumberOfPages() {
        return Integer.parseInt(findPageNumbers().group(2));
    }

    public int getPageNumber() {
        return Integer.parseInt(findPageNumbers().group(1));
    }

    public WebElement getPrevLink() {
        return findId(PREV_PAGE_LINK);
    }

    public boolean hasFirstLink() {
        return hasId(FIRST_PAGE_LINK);
    }

    public boolean hasLastLink() {
        return hasId(LAST_PAGE_LINK);
    }

    public boolean hasNextLink() {
        return hasId(NEXT_PAGE_LINK);
    }

    public boolean hasPrevLink() {
        return hasId(PREV_PAGE_LINK);
    }

    public void openFirstPage() {
        getFirstLink().click();
    }

    public void openLastPage() {
        getLastLink().click();
    }

    public void openNextPage() {
        getNextLink().click();
    }

    public void openPrevPage() {
        getPrevLink().click();
    }

    /* utility methods */

    private WebElement findId(final String id) {
        return driver.findElement(By.id(id));
    }

    private MatchResult findPageNumbers() {
        String pageNumbers = findId(PAGE_NUMBERS).getText();
        final Matcher matcher = PAGE_PATTERN.matcher(pageNumbers);
        matcher.find();
        return matcher.toMatchResult();
    }

    private boolean hasId(final String id) {
        return !driver.findElements(By.id(id)).isEmpty();
    }
}
