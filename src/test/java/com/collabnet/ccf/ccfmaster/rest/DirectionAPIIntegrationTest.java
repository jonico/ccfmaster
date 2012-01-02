package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionList;


public class DirectionAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private DirectionDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id, Direction.class);
        org.junit.Assert.assertNotNull("Find method for 'Direction' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Direction' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirections();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl + "/directions", DirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'Direction' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Direction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirectionsByLandscapeEquals(obj.getLandscape());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getLandscape().getPlugId() +  "/directions", DirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'Direction' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Direction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirectionsByDirection(obj.getDirection());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl +  "/directions/" + obj.getDirection() + "/", DirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'Direction' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Direction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeAndDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirectionsByLandscapeAndDirection(obj.getLandscape(), obj.getDirection());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getLandscape().getPlugId() +  "/directions/" + obj.getDirection(), DirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'Direction' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Direction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id, Direction.class);
        org.junit.Assert.assertNotNull("Find method for 'Direction' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDirection(obj);
        restTemplate.put(ccfAPIUrl + "/directions/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id, Direction.class);
        org.junit.Assert.assertTrue("Version for 'Direction' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id, Direction.class);
        org.junit.Assert.assertNotNull("Find method for 'Direction' illegally returned null for id '" + id + "'", obj);
        //put with wrong id
        restTemplate.put(ccfAPIUrl + "/directions/"+ id + 42, obj);
        
    }
    
    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getNewTransientDirection(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Direction' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/directions/", obj, Direction.class);
        org.junit.Assert.assertNotNull("Expected 'Direction' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/directions/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id, Direction.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
