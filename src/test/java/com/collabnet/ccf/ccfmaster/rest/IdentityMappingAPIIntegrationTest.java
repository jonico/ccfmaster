package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;


public class IdentityMappingAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private IdentityMappingDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getRandomIdentityMapping();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"+ id, IdentityMapping.class);
        org.junit.Assert.assertNotNull("Find method for 'IdentityMapping' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'IdentityMapping' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", dod.getRandomIdentityMapping());
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping.countIdentityMappings();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", dod.getRandomIdentityMapping());
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl + "/identitymappings", IdentityMappingList.class);
        org.junit.Assert.assertTrue("Counter for 'IdentityMapping' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'IdentityMapping' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'IdentityMapping' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getRandomIdentityMapping();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping.countIdentityMappingsByLandscape(obj.getRepositoryMapping().getExternalApp().getLandscape());
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/identitymappings", IdentityMappingList.class);
        org.junit.Assert.assertTrue("Counter for 'IdentityMapping' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'IdentityMapping' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'IdentityMapping' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getRandomIdentityMapping();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"+ id, IdentityMapping.class);
        org.junit.Assert.assertNotNull("Find method for 'IdentityMapping' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyIdentityMapping(obj);
        restTemplate.put(ccfAPIUrl + "/identitymappings/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"+ id, IdentityMapping.class);
        org.junit.Assert.assertTrue("Version for 'IdentityMapping' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getRandomIdentityMapping();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"+ id, IdentityMapping.class);
        org.junit.Assert.assertNotNull("Find method for 'IdentityMapping' illegally returned null for id '" + id + "'", obj);
        restTemplate.put(ccfAPIUrl + "/identitymappings/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", dod.getRandomIdentityMapping());
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getNewTransientIdentityMapping(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'IdentityMapping' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/identitymappings/", obj, IdentityMapping.class);
        org.junit.Assert.assertNotNull("Expected 'IdentityMapping' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod.getRandomIdentityMapping();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'IdentityMapping' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/identitymappings/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"+ id, IdentityMapping.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
        	throw e;
        }
    }

}
