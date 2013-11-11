package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigList;

public class ParticipantConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private ParticipantConfigDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        dod.getRandomParticipantConfig());
        long count = com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig
                .countParticipantConfigs();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        dod.getRandomParticipantConfig());
        List<ParticipantConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/participantconfigs", ParticipantConfigList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'ParticipantConfig' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'ParticipantConfig' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'ParticipantConfig' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCountParticipantConfigScope() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig
                .countParticipantConfigsByParticipant(obj.getParticipant());
        List<ParticipantConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/participants/" + obj.getParticipant().getId()
                + "/participantconfigs/", ParticipantConfigList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'ParticipantConfig' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'ParticipantConfig' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'ParticipantConfig' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        dod.getRandomParticipantConfig());
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getNewTransientParticipantConfig(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'ParticipantConfig' identifier to be null",
                obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/participantconfigs/",
                obj, ParticipantConfig.class);
        org.junit.Assert.assertNotNull(
                "Expected 'ParticipantConfig' identifier to no longer be null",
                obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'ParticipantConfig' illegally returned null for id '"
                        + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'ParticipantConfig' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to provide an identifier",
                        id);
        restTemplate.delete(ccfAPIUrl + "/participantconfigs/" + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl + "/participantconfigs/"
                    + id, ParticipantConfig.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'ParticipantConfig' illegally returned null for id '"
                        + id + "'", obj);
        boolean modified = dod.modifyParticipantConfig(obj);
        restTemplate.put(ccfAPIUrl + "/participantconfigs/" + id, obj);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'ParticipantConfig' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'ParticipantConfig' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.Assert.assertNotNull(
                "Find method for 'ParticipantConfig' illegally returned null for id '"
                        + id + "'", obj);
        dod.modifyParticipantConfig(obj);
        //put to ressource with wrong id
        restTemplate.put(ccfAPIUrl + "/participantconfigs/" + id + 42, obj);

    }

}
