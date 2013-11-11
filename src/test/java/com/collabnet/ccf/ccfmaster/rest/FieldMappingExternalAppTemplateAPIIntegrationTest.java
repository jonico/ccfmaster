package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;

public class FieldMappingExternalAppTemplateAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private FieldMappingExternalAppTemplateDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        dod.getRandomFieldMappingExternalAppTemplate());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate
                .countFieldMappingExternalAppTemplates();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        dod.getRandomFieldMappingExternalAppTemplate());
        List<FieldMappingExternalAppTemplate> result = restTemplate
                .getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates",
                        FieldMappingExternalAppTemplateList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'FieldMappingExternalAppTemplate' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'FieldMappingExternalAppTemplate' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'FieldMappingExternalAppTemplate' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getNewTransientFieldMappingExternalAppTemplate(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide a new transient entity",
                        obj);
        org.junit.Assert
                .assertNull(
                        "Expected 'FieldMappingExternalAppTemplate' identifier to be null",
                        obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/", obj,
                FieldMappingExternalAppTemplate.class);
        org.junit.Assert
                .assertNotNull(
                        "Expected 'FieldMappingExternalAppTemplate' identifier to no longer be null",
                        obj.getId());
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                + id + "'", obj);
        org.junit.Assert
                .assertEquals(
                        "Find method for 'FieldMappingExternalAppTemplate' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier",
                        id);
        restTemplate.delete(ccfAPIUrl + "/fieldmappingexternalapptemplates/"
                + id);
        try {
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/fieldmappingexternalapptemplates/" + id,
                    FieldMappingExternalAppTemplate.class);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                + id + "'", obj);
        boolean modified = dod.modifyFieldMappingExternalAppTemplate(obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappingexternalapptemplates/" + id,
                obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        if (obj.getKind() != FieldMappingKind.MAPPING_RULES) {
            Assert.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.Assert
                .assertTrue(
                        "Version for 'FieldMappingExternalAppTemplate' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        org.junit.Assert
                .assertNotNull(
                        "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                + id + "'", obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappingexternalapptemplates/" + id,
                obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id + 42,
                FieldMappingExternalAppTemplate.class);

    }

}
