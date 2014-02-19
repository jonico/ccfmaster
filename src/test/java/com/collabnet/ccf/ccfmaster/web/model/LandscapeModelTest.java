package com.collabnet.ccf.ccfmaster.web.model;

import static com.collabnet.ccf.ccfmaster.web.model.LandscapeModel.normalizeQcUrl;
import static com.collabnet.ccf.ccfmaster.web.model.LandscapeModel.normalizeSwpUrl;
import static com.collabnet.ccf.ccfmaster.web.model.LandscapeModel.normalizeUrl;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
    public void qcUrlEmptyPath() {
        final String participantUrl = "http://example.com/";
        assertThat(
                normalizeQcUrl(participantUrl),
                both(containsString("qcbin/")).and(
                        containsString(participantUrl)));
    }

    @Test
    public void qcUrlNoPath() {
        final String participantUrl = "http://example.com";
        assertThat(
                normalizeQcUrl(participantUrl),
                both(containsString("qcbin/")).and(
                        containsString(participantUrl)));
    }

    @Test
    public void swpUrlEmptyPath() {
        final String participantUrl = "http://example.com/";
        assertThat(normalizeSwpUrl(participantUrl),
                both(containsString("/scrumworks-api/api2/scrumworks?wsdl"))
                        .and(containsString(participantUrl)));
    }

    @Test
    public void swpUrlNoPath() {
        final String participantUrl = "http://example.com";
        assertThat(normalizeSwpUrl(participantUrl),
                both(containsString("/scrumworks-api/api2/scrumworks?wsdl"))
                        .and(containsString(participantUrl)));
    }

    @Test
    public void urlWithPathReturnedUnmodified() {
        final String participantUrl = "http://example.com/bar?wsdl";
        assertReturnedUnmodified(participantUrl);
    }

    private void assertReturnedUnmodified(final String participantUrl) {
        assertThat(normalizeUrl(participantUrl, "/foo"),
                equalTo(participantUrl));
    }

}
