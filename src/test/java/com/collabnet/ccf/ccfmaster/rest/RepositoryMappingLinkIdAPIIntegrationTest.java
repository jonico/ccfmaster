package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingList;

public class RepositoryMappingLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDataOnDemand dod;

    @Autowired
    private ExternalAppDataOnDemand       dodEA;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping
                .countRepositoryMappingsByExternalApp(obj.getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        List<RepositoryMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, RepositoryMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping randomObject = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        randomObject);
        String linkIdPathSegment = "/linkid/"
                + randomObject.getExternalApp().getLinkId()
                + "/repositorymappings/";
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getNewTransientRepositoryMapping(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'RepositoryMapping' identifier to be null",
                obj.getId());
        obj.setExternalApp(randomObject.getExternalApp());
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Expected 'RepositoryMapping' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'RepositoryMapping' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    RepositoryMapping.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testReparenting() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappings/" + id,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'", obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        obj.setExternalApp(ea);
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappings/" + id,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyRepositoryMapping(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMapping.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'RepositoryMapping' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWithWrongParentIdInPath() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappings/" + id,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'", obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                + "/repositorymappings/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappings/" + id,
                RepositoryMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'", obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        //put wrong id
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id + 42, obj);
    }

}
