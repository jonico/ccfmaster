package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;


public class FieldMappingExternalAppTemplateLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private FieldMappingExternalAppTemplateDataOnDemand dod;
	
	@Autowired
    private ExternalAppDataOnDemand dodEA;
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";  
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment +  id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'FieldMappingExternalAppTemplate' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
    	com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
    	org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate.countFieldMappingExternalAppTemplatesByParent(obj.getParent());
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        List<FieldMappingExternalAppTemplate> result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment, FieldMappingExternalAppTemplateList.class);
        org.junit.Assert.assertTrue("Counter for 'FieldMappingExternalAppTemplate' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'FieldMappingExternalAppTemplate' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'FieldMappingExternalAppTemplate' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
    	org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate.countFieldMappingExternalAppTemplatesByParentAndDirection(obj.getParent(), obj.getDirection());
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        List<FieldMappingExternalAppTemplate> result = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + obj.getDirection() + "/", FieldMappingExternalAppTemplateList.class);
        org.junit.Assert.assertTrue("Counter for 'FieldMappingExternalAppTemplate' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'FieldMappingExternalAppTemplate' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'FieldMappingExternalAppTemplate' returned an incorrect number of entries", count, result.size());
    }
            
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates/"+ id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFieldMappingExternalAppTemplate(obj);
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        restTemplate.put(ccfAPIUrl +  linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertTrue("Version for 'FieldMappingExternalAppTemplate' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates/"+ id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '" + id + "'", obj);
        dod.modifyFieldMappingExternalAppTemplate(obj);
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        restTemplate.put(ccfAPIUrl +  linkIdPathSegment + id + 42, obj);
      
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testReparenting() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates/"+ id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '" + id + "'", obj);
        dod.modifyFieldMappingExternalAppTemplate(obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        obj.setParent(ea);
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        
        try {
        	restTemplate.put(ccfAPIUrl +  linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
        	throw e;
        }
     }
    
    
    @Test(expected=HttpClientErrorException.class)
    public void testWithWrongParentIdInPath() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates/"+ id, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '" + id + "'", obj);
        dod.modifyFieldMappingExternalAppTemplate(obj);
        ExternalApp ea = dodEA.getNewTransientExternalApp(42);
        ea.persist();
        String linkIdPathSegment = "/linkid/" + ea.getLinkId() + "/fieldmappingexternalapptemplates/";
        
        try {
        	restTemplate.put(ccfAPIUrl +  linkIdPathSegment + id, obj);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 403", 403, e.getStatusCode().value());
        	throw e;
        }
     }
       
    
    
    @Test
    public void testCreate() {
    	com.collabnet.ccf.ccfmaster.server.domain.ExternalApp randomObject = dodEA.getRandomExternalApp();
    	org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", randomObject);
    	String linkIdPathSegment = "/linkid/" + randomObject.getLinkId() + "/fieldmappingexternalapptemplates/";
    	com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getNewTransientFieldMappingExternalAppTemplate(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'FieldMappingExternalAppTemplate' identifier to be null", obj.getId());
        obj.setParent(randomObject);
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj, FieldMappingExternalAppTemplate.class);
        org.junit.Assert.assertNotNull("Expected 'FieldMappingExternalAppTemplate' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod.getRandomFieldMappingExternalAppTemplate();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier", id);
        String linkIdPathSegment = "/linkid/" + obj.getParent().getLinkId() + "/fieldmappingexternalapptemplates/";
        restTemplate.delete(ccfAPIUrl +  linkIdPathSegment + id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id, FieldMappingExternalAppTemplate.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
        	throw e;
        }
    }

}
