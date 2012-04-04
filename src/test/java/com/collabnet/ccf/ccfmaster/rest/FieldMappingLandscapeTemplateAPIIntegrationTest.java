package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplateDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplateList;


public class FieldMappingLandscapeTemplateAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private FieldMappingLandscapeTemplateDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod.getRandomFieldMappingLandscapeTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, FieldMappingLandscapeTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'FieldMappingLandscapeTemplate' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", dod.getRandomFieldMappingLandscapeTemplate());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate.countFieldMappingLandscapeTemplates();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", dod.getRandomFieldMappingLandscapeTemplate());
        List<FieldMappingLandscapeTemplate> result = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates", FieldMappingLandscapeTemplateList.class);
        org.junit.Assert.assertTrue("Counter for 'FieldMappingLandscapeTemplate' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'FieldMappingLandscapeTemplate' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'FieldMappingLandscapeTemplate' returned an incorrect number of entries", count, result.size());
    }
        
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod.getRandomFieldMappingLandscapeTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, FieldMappingLandscapeTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFieldMappingLandscapeTemplate(obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, FieldMappingLandscapeTemplate.class);
        if (obj.getKind() != FieldMappingKind.MAPPING_RULES) {
        	Assert.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.Assert.assertTrue("Version for 'FieldMappingLandscapeTemplate' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod.getRandomFieldMappingLandscapeTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, FieldMappingLandscapeTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '" + id + "'", obj);
        //test with wrong id
        restTemplate.put(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod.getNewTransientFieldMappingLandscapeTemplate(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'FieldMappingLandscapeTemplate' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/", obj, FieldMappingLandscapeTemplate.class);
        org.junit.Assert.assertNotNull("Expected 'FieldMappingLandscapeTemplate' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod.getRandomFieldMappingLandscapeTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappinglandscapetemplates/"+ id, FieldMappingLandscapeTemplate.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
        	throw e;
        }
    }

}
