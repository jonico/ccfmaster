package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigList;


public class LandscapeConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private LandscapeConfigDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getRandomLandscapeConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"+ id, LandscapeConfig.class);
        org.junit.Assert.assertNotNull("Find method for 'LandscapeConfig' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'LandscapeConfig' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", dod.getRandomLandscapeConfig());
        long count = com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig.countLandscapeConfigs();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", dod.getRandomLandscapeConfig());
        List<LandscapeConfig> result = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs", LandscapeConfigList.class);
        org.junit.Assert.assertTrue("Counter for 'LandscapeConfig' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'LandscapeConfig' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'LandscapeConfig' returned an incorrect number of entries", count, result.size());
    }
    
    
    @Test
    public void testCountLandscapeConfigScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getRandomLandscapeConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig.countLandscapeConfigsByLandscape(obj.getLandscape());
        List<LandscapeConfig> result = restTemplate.getForObject(ccfAPIUrl +  "/landscapes/" + obj.getLandscape().getId() + "/landscapeconfigs/", LandscapeConfigList.class);
        org.junit.Assert.assertTrue("Counter for 'LandscapeConfig' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'LandscapeConfig' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'LandscapeConfig' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getRandomLandscapeConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"+ id, LandscapeConfig.class);
        org.junit.Assert.assertNotNull("Find method for 'LandscapeConfig' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLandscapeConfig(obj);
        restTemplate.put(ccfAPIUrl + "/landscapeconfigs/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"+ id, LandscapeConfig.class);
        org.junit.Assert.assertTrue("Version for 'LandscapeConfig' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getRandomLandscapeConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"+ id, LandscapeConfig.class);
        org.junit.Assert.assertNotNull("Find method for 'LandscapeConfig' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLandscapeConfig(obj);
        //Test with wrong id
        restTemplate.put(ccfAPIUrl + "/landscapeconfigs/"+ id + 42, obj);

    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", dod.getRandomLandscapeConfig());
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getNewTransientLandscapeConfig(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'LandscapeConfig' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/landscapeconfigs/", obj, LandscapeConfig.class);
        org.junit.Assert.assertNotNull("Expected 'LandscapeConfig' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod.getRandomLandscapeConfig();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'LandscapeConfig' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/landscapeconfigs/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"+ id, LandscapeConfig.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
