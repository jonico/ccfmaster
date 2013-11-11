package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;

public class HospitalEntryLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private HospitalEntryDataOnDemand dod;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByExternalApp(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, HospitalEntryList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByExternalAppAndDirection(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp(), obj.getRepositoryMappingDirection()
                        .getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment
                + obj.getRepositoryMappingDirection().getDirection() + "/",
                HospitalEntryList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountDirectionScopeWithCount() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByExternalAppAndDirection(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp(), obj.getRepositoryMappingDirection()
                        .getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        String result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment
                + obj.getRepositoryMappingDirection().getDirection()
                + "/count/", String.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, Long.parseLong(result));
    }

    @Test
    public void testCountRepositoryMappingAndDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMappingAndDirection(
                        obj.getRepositoryMappingDirection()
                                .getRepositoryMapping(), obj
                                .getRepositoryMappingDirection().getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappings/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getId() + "/hospitalentrys/";
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment
                + obj.getRepositoryMappingDirection().getDirection(),
                HospitalEntryList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingAndDirectionScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMappingAndDirection(
                        obj.getRepositoryMappingDirection()
                                .getRepositoryMapping(), obj
                                .getRepositoryMappingDirection().getDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappings/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getId() + "/hospitalentrys/";
        String result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment
                + obj.getRepositoryMappingDirection().getDirection()
                + "/count/", String.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, Long.parseLong(result));
    }

    @Test
    public void testCountRepositoryMappingDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMappingDirection(obj
                        .getRepositoryMappingDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirections/"
                + obj.getRepositoryMappingDirection().getId()
                + "/hospitalentrys/";
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, HospitalEntryList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingDirectionScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMappingDirection(obj
                        .getRepositoryMappingDirection());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirections/"
                + obj.getRepositoryMappingDirection().getId()
                + "/hospitalentrys/";
        String result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment
                + "count", String.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, Long.parseLong(result));
    }

    @Test
    public void testCountRepositoryMappingScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMapping(obj
                        .getRepositoryMappingDirection().getRepositoryMapping());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappings/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getId() + "/hospitalentrys/";
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, HospitalEntryList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByRepositoryMapping(obj
                        .getRepositoryMappingDirection().getRepositoryMapping());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappings/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getId() + "/hospitalentrys/";
        String result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment
                + "count/", String.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, Long.parseLong(result));
    }

    @Test
    public void testCountWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByExternalApp(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        String result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment
                + "count/", String.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'HospitalEntry' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'HospitalEntry' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'HospitalEntry' returned an incorrect number of entries",
                        count, Long.parseLong(result));
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry randomObject = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        randomObject);
        String linkIdPathSegment = "/linkid/"
                + randomObject.getRepositoryMappingDirection()
                        .getRepositoryMapping().getExternalApp().getLinkId()
                + "/hospitalentrys/";
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getNewTransientHospitalEntry(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'HospitalEntry' identifier to be null", obj.getId());
        obj.setRepositoryMappingDirection(randomObject
                .getRepositoryMappingDirection());
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                HospitalEntry.class);
        org.junit.Assert.assertNotNull(
                "Expected 'HospitalEntry' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                HospitalEntry.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'HospitalEntry' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'HospitalEntry' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    HospitalEntry.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'HospitalEntry' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/" + id,
                HospitalEntry.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'HospitalEntry' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyHospitalEntry(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId() + "/hospitalentrys/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                HospitalEntry.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'HospitalEntry' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
        // now let's try to update attributes that will not be updated
        obj.setAdaptorName("foo");
        obj.setSourceArtifactId("bar");
        obj.setErrorCode("foobar");
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                HospitalEntry.class);
        org.junit.Assert.assertFalse("adaptor name should be immutable",
                "foo".equals(obj.getAdaptorName()));
        org.junit.Assert.assertFalse("artifact id should be immutable",
                "bar".equals(obj.getSourceArtifactId()));
        org.junit.Assert.assertTrue("error code should be changeable",
                "foobar".equals(obj.getErrorCode()));
    }

}
