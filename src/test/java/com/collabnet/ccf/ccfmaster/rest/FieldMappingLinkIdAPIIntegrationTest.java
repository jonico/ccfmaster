package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionDataOnDemand;

public class FieldMappingLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private FieldMappingDataOnDemand               dod;

    @Autowired
    private RepositoryMappingDirectionDataOnDemand dodRM;

    @Test
    public void testCount() {
        FieldMapping obj = dod.getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        long count = FieldMapping.countFieldMappingsByExternalApp(obj
                .getParent().getRepositoryMapping().getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getParent().getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/fieldmappings/";
        List<FieldMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, FieldMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'FieldMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'FieldMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'FieldMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingDirectionScope() {
        FieldMapping obj = dod.getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        long count = FieldMapping.countFieldMappingsByParent(obj.getParent());
        String linkIdPathSegment = "/linkid/"
                + obj.getParent().getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/repositorymappingdirections/"
                + obj.getParent().getId() + "/fieldmappings/";
        List<FieldMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, FieldMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'FieldMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'FieldMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'FieldMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        RepositoryMappingDirection randomObject = dodRM
                .getRandomRepositoryMappingDirection();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        randomObject);
        String linkIdPathSegment = "/linkid/"
                + randomObject.getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/fieldmappings/";
        FieldMapping obj = dod.getNewTransientFieldMapping(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'FieldMapping' identifier to be null", obj.getId());
        obj.setParent(randomObject);
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                FieldMapping.class);
        org.junit.Assert.assertNotNull(
                "Expected 'FieldMapping' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        FieldMapping obj = dod.getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getParent().getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/fieldmappings/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                FieldMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'FieldMapping' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'FieldMapping' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        FieldMapping obj = dod.getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getParent().getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/fieldmappings/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    FieldMapping.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        FieldMapping obj = dod.getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                FieldMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'FieldMapping' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyFieldMapping(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getParent().getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/fieldmappings/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                FieldMapping.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'FieldMapping' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

}
