package com.collabnet.ccf.ccfmaster.selenium.project;

import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class RepositoryMappingsPage {

	private final WebDriver driver;
	public static final String TF_2_QC_LABEL = "TF \u21D2 QC";
	public static final String QC_2_TF_LABEL = "QC \u21D2 TF";

	public RepositoryMappingsPage(WebDriver driver) {
		String currentUrl = driver.getCurrentUrl();
		Preconditions.checkState(
				currentUrl.indexOf("/CCFMaster/project/repositorymappings") != -1,
				"Current page is not index page",
				currentUrl);
		this.driver = driver;
	}
	
	public Directions getDirection() {
		final URI currentUri = URI.create(driver.getCurrentUrl());
		String dir = Iterables.getFirst(queryParams(currentUri).get("direction"), null);
		return (dir != null) ? Directions.valueOf(dir) : null;
	}
	
	static Multimap<String, String> queryParams(URI uri) {
		ImmutableMultimap.Builder<String, String> builder = ImmutableMultimap.builder();
		List<NameValuePair> params = URLEncodedUtils.parse(uri, "UTF-8");
		for (NameValuePair nameValuePair : params) {
			builder.put(nameValuePair.getName(), nameValuePair.getValue());
		}
		return builder.build();
	}
	
	public List<WebElement> getTabs() {
		return driver.findElements(By.cssSelector("td.TabBody > a"));
	}
	
	public WebElement noMappingsText() {
		return driver.findElement(By.className("ItemListNoData"));
	}
	
	public WebElement getQc2TfTab() {
		return driver.findElement(By.linkText(QC_2_TF_LABEL));
	}

	public WebElement getTf2QcTab() {
		return driver.findElement(By.linkText(TF_2_QC_LABEL));
	}
}
