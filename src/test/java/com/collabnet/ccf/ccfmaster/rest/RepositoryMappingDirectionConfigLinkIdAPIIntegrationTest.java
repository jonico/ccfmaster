package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

public class RepositoryMappingDirectionConfigLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionConfigDataOnDemand dodRMDC;

    @Autowired
    private ExternalAppDataOnDemand                      dodEA;

    @Test
    public void testCountByRepositoryMappingDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig
                .countRepositoryMappingDirectionConfigsByExternalAppAndRepositoryMappingDirection(
                        obj.getRepositoryMappingDirection()
                                .getRepositoryMapping().getExternalApp(),
                        obj.getRepositoryMappingDirection());

        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirections/"
                + obj.getRepositoryMappingDirection().getId()
                + "/repositorymappingdirectionconfigs/";
        List<RepositoryMappingDirectionConfig> result = restTemplate
                .getForObject(ccfAPIUrl + linkIdPathSegment,
                        RepositoryMappingDirectionConfigList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'RepositoryMappingDirectionConfig' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'RepositoryMappingDirectionConfig' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'RepositoryMappingDirectionConfig' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig randomconfigObject = dodRMDC
                .getNewTransientRepositoryMappingDirectionConfig(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        randomconfigObject);
        String linkIdPathSegment = "/linkid/"
                + randomconfigObject.getRepositoryMappingDirection()
                        .getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";

        randomconfigObject = restTemplate.postForObject(ccfAPIUrl
                + linkIdPathSegment, randomconfigObject,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertNotNull(
                        "Expected 'RepositoryMappingDirectionConfig' identifier to no longer be null",
                        randomconfigObject.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier",
                        id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'RepositoryMappingDirectionConfig' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier",
                        id);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";
        restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                    RepositoryMappingDirectionConfig.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                + id + "'", obj);
        boolean modified = dodRMDC.modifyRepositoryMappingDirectionConfig(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'RepositoryMappingDirectionConfig' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWithWrongGrandparentIdInPath() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                + id + "'", obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                + "/repositorymappingdirectionconfigs/";

        try {
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
            throw e;
        }
    }

}
