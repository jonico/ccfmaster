package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeList;

public class LandscapeAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private LandscapeDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        dod.getRandomLandscape());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Landscape
                .countLandscapes();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        dod.getRandomLandscape());
        List<Landscape> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes", LandscapeList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'Landscape' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert.assertNotNull(
                "Find entries method for 'Landscape' illegally returned null",
                result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'Landscape' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        dod.getRandomLandscape());
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getNewTransientLandscape(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'Landscape' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/landscapes/", obj,
                Landscape.class);
        org.junit.Assert.assertNotNull(
                "Expected 'Landscape' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'Landscape' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test
    public void testFindWithPlugId() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/landscapes/" + obj.getPlugId(), Landscape.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'Landscape' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide an identifier",
                        id);
        restTemplate.delete(ccfAPIUrl + "/landscapes/" + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                    Landscape.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyLandscape(obj);
        restTemplate.put(ccfAPIUrl + "/landscapes/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'Landscape' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'Landscape' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'", obj);
        restTemplate.put(ccfAPIUrl + "/landscapes/" + id + 42, obj);
    }

}
