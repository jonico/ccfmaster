package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigList;


public class DirectionConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private DirectionConfigDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getRandomDirectionConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"+ id, DirectionConfig.class);
        org.junit.Assert.assertNotNull("Find method for 'DirectionConfig' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'DirectionConfig' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", dod.getRandomDirectionConfig());
        long count = com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig.countDirectionConfigs();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", dod.getRandomDirectionConfig());
        List<DirectionConfig> result = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs", DirectionConfigList.class);
        org.junit.Assert.assertTrue("Counter for 'DirectionConfig' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'DirectionConfig' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'DirectionConfig' returned an incorrect number of entries", count, result.size());
    }
    
    
    @Test
    public void testCountDirectionConfigScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getRandomDirectionConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig.countDirectionConfigsByDirection(obj.getDirection());
        List<DirectionConfig> result = restTemplate.getForObject(ccfAPIUrl +  "/directions/" + obj.getDirection().getId() + "/directionconfigs/", DirectionConfigList.class);
        org.junit.Assert.assertTrue("Counter for 'DirectionConfig' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'DirectionConfig' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'DirectionConfig' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getRandomDirectionConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"+ id, DirectionConfig.class);
        org.junit.Assert.assertNotNull("Find method for 'DirectionConfig' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDirectionConfig(obj);
        restTemplate.put(ccfAPIUrl + "/directionconfigs/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"+ id, DirectionConfig.class);
        org.junit.Assert.assertTrue("Version for 'DirectionConfig' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
    	 com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getRandomDirectionConfig();
         org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", obj);
         java.lang.Long id = obj.getId();
         org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to provide an identifier", id);
         obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"+ id, DirectionConfig.class);
         org.junit.Assert.assertNotNull("Find method for 'DirectionConfig' illegally returned null for id '" + id + "'", obj);
         dod.modifyDirectionConfig(obj);
         //put to ressource with wrong id
         restTemplate.put(ccfAPIUrl + "/directionconfigs/" + id + 42, obj);
       
    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", dod.getRandomDirectionConfig());
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getNewTransientDirectionConfig(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'DirectionConfig' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/directionconfigs/", obj, DirectionConfig.class);
        org.junit.Assert.assertNotNull("Expected 'DirectionConfig' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod.getRandomDirectionConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'DirectionConfig' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/directionconfigs/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"+ id, DirectionConfig.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
