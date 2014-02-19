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
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;

public class RepositoryMappingDirectionLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionDataOnDemand dod;

    @Autowired
    private RepositoryMappingDataOnDemand          dodRM;

    @Autowired
    private ExternalAppDataOnDemand                dodEA;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByExternalApp(obj
                        .getRepositoryMapping().getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + linkIdPathSegment,
                RepositoryMappingDirectionList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMappingDirection' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByExternalAppAndDirection(obj
                        .getRepositoryMapping().getExternalApp(), obj
                        .getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + linkIdPathSegment + obj.getDirection() + "/",
                RepositoryMappingDirectionList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMappingDirection' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingAndDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByRepositoryMappingAndDirection(
                        obj.getRepositoryMapping(), obj.getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappings/" + obj.getRepositoryMapping().getId()
                + "/repositorymappingdirections/";
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + linkIdPathSegment + obj.getDirection(),
                RepositoryMappingDirectionList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMappingDirection' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByRepositoryMapping(obj
                        .getRepositoryMapping());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappings/" + obj.getRepositoryMapping().getId()
                + "/repositorymappingdirections/";
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + linkIdPathSegment,
                RepositoryMappingDirectionList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMappingDirection' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping randomObject = dodRM
                .getRandomRepositoryMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        randomObject);
        String linkIdPathSegment = "/linkid/"
                + randomObject.getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getNewTransientRepositoryMappingDirection(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'RepositoryMappingDirection' identifier to be null",
                obj.getId());
        obj.setRepositoryMapping(randomObject);
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                RepositoryMappingDirection.class);
        org.junit.Assert
                .assertNotNull(
                        "Expected 'RepositoryMappingDirection' identifier to no longer be null",
                        obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'RepositoryMappingDirection' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    RepositoryMappingDirection.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testReparenting() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'", obj);
        RepositoryMapping rm = dodRM.getNewTransientRepositoryMapping(42);
        rm.persist();
        obj.setRepositoryMapping(rm);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyRepositoryMappingDirection(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMappingDirection.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'RepositoryMappingDirection' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWithWrongGrandparentIdInPath() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'", obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                + "/repositorymappingdirections/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'", obj);
        dod.modifyRepositoryMappingDirection(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirections/";
        //put wrong id
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id + 42, obj);
    }

}
