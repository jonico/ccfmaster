package com.collabnet.ccf.ccfmaster.rest;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;

public class IdentityMappingLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private IdentityMappingDataOnDemand   dod;

    @Autowired
    private ExternalAppDataOnDemand       dodEA;

    @Autowired
    private RepositoryMappingDataOnDemand dodRM;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappingsByExternalApp(obj.getRepositoryMapping()
                        .getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, IdentityMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'IdentityMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'IdentityMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'IdentityMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountRepositoryMappingScope() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappingsByRepositoryMapping(obj
                        .getRepositoryMapping());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappings/" + obj.getRepositoryMapping().getId()
                + "/identitymappings/";
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, IdentityMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'IdentityMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'IdentityMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'IdentityMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping randomObject = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        randomObject);
        String linkIdPathSegment = "/linkid/"
                + randomObject.getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/identitymappings/";
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getNewTransientIdentityMapping(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide a new transient entity",
                        obj);
        org.junit.Assert
                .assertNull("Expected 'IdentityMapping' identifier to be null",
                        obj.getId());
        obj.setRepositoryMapping(randomObject.getRepositoryMapping());
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                IdentityMapping.class);
        org.junit.Assert.assertNotNull(
                "Expected 'IdentityMapping' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'IdentityMapping' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testReGrandparenting() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();

        org.junit.Assert.assertNotNull(
                "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'", obj);
        String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                + "/identitymappings/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    IdentityMapping.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testReparenting() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        RepositoryMapping rm = dodRM.getNewTransientRepositoryMapping(42);
        rm.persist();
        obj.setRepositoryMapping(rm);
        org.junit.Assert.assertNotNull(
                "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'", obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyIdentityMapping(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'IdentityMapping' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
        // now let's test whether immutable values can be updated as well
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        obj.setSourceArtifactId("foo");
        obj.setTargetArtifactId("bar");
        Date currentDate = new Date();
        obj.setTargetLastModificationTime(currentDate);
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.Assert.assertFalse("source artifact id should be immutable",
                "foo".equals(obj.getSourceArtifactId()));
        org.junit.Assert.assertFalse("target artifact id should be immutable",
                "bar".equals(obj.getTargetArtifactId()));
        org.junit.Assert.assertTrue(
                "last modification time should be changeable",
                currentDate.equals(obj.getTargetLastModificationTime()));
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'IdentityMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'", obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        //test with wrong id
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id + 42, obj);
    }

}
