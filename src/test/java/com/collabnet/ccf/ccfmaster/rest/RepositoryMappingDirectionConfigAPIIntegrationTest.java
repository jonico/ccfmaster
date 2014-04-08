package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

public class RepositoryMappingDirectionConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionConfigDataOnDemand rdod;

    @Test
    public void testCount() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        rdod.getRandomRepositoryMappingDirectionConfig());
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig
                .countRepositoryMappingDirectionConfigs();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        rdod.getRandomRepositoryMappingDirectionConfig());
        List<RepositoryMappingDirectionConfig> result = restTemplate
                .getForObject(ccfAPIUrl + "/repositorymappingdirectionconfigs",
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
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly",
                        rdod.getRandomRepositoryMappingDirectionConfig());
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                .getNewTransientRepositoryMappingDirectionConfig(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide a new transient entity",
                        obj);
        org.junit.Assert
                .assertNull(
                        "Expected 'RepositoryMappingDirectionConfig' identifier to be null",
                        obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/", obj,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertNotNull(
                        "Expected 'RepositoryMappingDirectionConfig' identifier to no longer be null",
                        obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
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
        org.junit.Assert
                .assertEquals(
                        "Find method for 'RepositoryMappingDirectionConfig' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
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
        restTemplate.delete(ccfAPIUrl + "/repositorymappingdirectionconfigs/"
                + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/repositorymappingdirectionconfigs/" + id,
                    RepositoryMappingDirectionConfig.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
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
        boolean modified = rdod.modifyRepositoryMappingDirectionConfig(obj);
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirectionconfigs/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'RepositoryMappingDirectionConfig' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
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
        rdod.modifyRepositoryMappingDirectionConfig(obj);
        //put to ressource with wrong id
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirectionconfigs/" + id
                + 42, obj);

    }
}
