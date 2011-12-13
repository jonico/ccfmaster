package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;


public class RepositoryMappingDirectionAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private RepositoryMappingDirectionDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections/"+ id, RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull("Find method for 'RepositoryMappingDirection' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'RepositoryMappingDirection' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", dod.getRandomRepositoryMappingDirection());
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection.countRepositoryMappingDirections();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", dod.getRandomRepositoryMappingDirection());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections", RepositoryMappingDirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'RepositoryMappingDirection' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection.countRepositoryMappingDirectionsByLandscape(obj.getRepositoryMapping().getExternalApp().getLandscape());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/repositorymappingdirections", RepositoryMappingDirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'RepositoryMappingDirection' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeAndDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection.countRepositoryMappingDirectionsByLandscapeAndDirection(obj.getRepositoryMapping().getExternalApp().getLandscape(), obj.getDirection());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/repositorymappingdirections/" + obj.getDirection(), RepositoryMappingDirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'RepositoryMappingDirection' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection.countRepositoryMappingDirectionsByDirection(obj.getDirection());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(ccfAPIUrl +  "/repositorymappingdirections/" + obj.getDirection() + "/", RepositoryMappingDirectionList.class);
        org.junit.Assert.assertTrue("Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'RepositoryMappingDirection' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections/"+ id, RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull("Find method for 'RepositoryMappingDirection' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyRepositoryMappingDirection(obj);
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirections/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections/"+ id, RepositoryMappingDirection.class);
        org.junit.Assert.assertTrue("Version for 'RepositoryMappingDirection' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections/"+ id, RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull("Find method for 'RepositoryMappingDirection' illegally returned null for id '" + id + "'", obj);
        //put with wrong id
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirections/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getNewTransientRepositoryMappingDirection(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'RepositoryMappingDirection' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/repositorymappingdirections/", obj, RepositoryMappingDirection.class);
        org.junit.Assert.assertNotNull("Expected 'RepositoryMappingDirection' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod.getRandomRepositoryMappingDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'RepositoryMappingDirection' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/repositorymappingdirections/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/repositorymappingdirections/"+ id, RepositoryMappingDirection.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
