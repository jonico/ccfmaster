package com.collabnet.ccf.ccfmaster.web.model;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static com.collabnet.ccf.ccfmaster.web.model.LandscapeModel.*;

import org.junit.Test;

public class LandscapeModelTest {

	@Test
	public void emptyUrlReturnedUnmodified() {
		final String participantUrl = "";
		assertReturnedUnmodified(participantUrl);
	}

	@Test
	public void invalidUrlReturnedUnmodified() {
		final String participantUrl = "asdf";
		assertReturnedUnmodified(participantUrl);
	}

	@Test
	public void urlWithPathReturnedUnmodified() {
		final String participantUrl = "http://example.com/bar?wsdl";
		assertReturnedUnmodified(participantUrl);
	}
	
	@Test
	public void swpUrlNoPath() {
		final String participantUrl = "http://example.com";
		assertThat(normalizeSwpUrl(participantUrl), both(containsString("/scrumworks-api/api2/scrumworks?wsdl")).and(containsString(participantUrl)));
	}
	
	@Test
	public void swpUrlEmptyPath() {
		final String participantUrl = "http://example.com/";
		assertThat(normalizeSwpUrl(participantUrl), both(containsString("/scrumworks-api/api2/scrumworks?wsdl")).and(containsString(participantUrl)));
	}

	@Test
	public void qcUrlNoPath() {
		final String participantUrl = "http://example.com";
		assertThat(normalizeQcUrl(participantUrl), both(containsString("qcbin/")).and(containsString(participantUrl)));
	}

	@Test
	public void qcUrlEmptyPath() {
		final String participantUrl = "http://example.com/";
		assertThat(normalizeQcUrl(participantUrl), both(containsString("qcbin/")).and(containsString(participantUrl)));
	}

	private void assertReturnedUnmodified(final String participantUrl) {
		assertThat(normalizeUrl(participantUrl, "/foo"), equalTo(participantUrl));
	}

}
