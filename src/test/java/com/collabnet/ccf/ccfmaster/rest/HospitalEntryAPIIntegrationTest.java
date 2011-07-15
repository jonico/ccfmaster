package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;


public class HospitalEntryAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private HospitalEntryDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/"+ id, HospitalEntry.class);
        org.junit.Assert.assertNotNull("Find method for 'HospitalEntry' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'HospitalEntry' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", dod.getRandomHospitalEntry());
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrys();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", dod.getRandomHospitalEntry());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/", HospitalEntryList.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountWithCountMethod() {
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", dod.getRandomHospitalEntry());
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrys();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", dod.getRandomHospitalEntry());
        String result = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/count/", String.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, Long.parseLong(result));
    }
    
    @Test
    public void testCountLandscapeScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByLandscape(obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/hospitalentrys", HospitalEntryList.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeScopeWithCountMethod() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByLandscape(obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape());
        String result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/hospitalentrys/count/", String.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, Long.parseLong(result));
    }
    
    @Test
    public void testCountLandscapeAndDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByLandscapeAndDirection(obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape(), obj.getRepositoryMappingDirection().getDirection());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/hospitalentrys/" + obj.getRepositoryMappingDirection().getDirection(), HospitalEntryList.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeAndDirectionScopeWithCountMethod() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByLandscapeAndDirection(obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape(), obj.getRepositoryMappingDirection().getDirection());
        String result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getRepositoryMappingDirection().getRepositoryMapping().getExternalApp().getLandscape().getPlugId() +  "/hospitalentrys/" + obj.getRepositoryMappingDirection().getDirection() + "/count/", String.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, Long.parseLong(result));
    }
    
    @Test
    public void testCountDirectionScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByDirection(obj.getRepositoryMappingDirection().getDirection());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl +  "/hospitalentrys/" + obj.getRepositoryMappingDirection().getDirection() + "/", HospitalEntryList.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountDirectionScopeWithCountMethod() {
    	com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry.countHospitalEntrysByDirection(obj.getRepositoryMappingDirection().getDirection());
        String result = restTemplate.getForObject(ccfAPIUrl +  "/hospitalentrys/" + obj.getRepositoryMappingDirection().getDirection() + "/count/", String.class);
        org.junit.Assert.assertTrue("Counter for 'HospitalEntry' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'HospitalEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'HospitalEntry' returned an incorrect number of entries", count, Long.parseLong(result));
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/"+ id, HospitalEntry.class);
        org.junit.Assert.assertNotNull("Find method for 'HospitalEntry' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyHospitalEntry(obj);
        restTemplate.put(ccfAPIUrl + "/hospitalentrys/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/"+ id, HospitalEntry.class);
        org.junit.Assert.assertTrue("Version for 'HospitalEntry' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/"+ id, HospitalEntry.class);
        org.junit.Assert.assertNotNull("Find method for 'HospitalEntry' illegally returned null for id '" + id + "'", obj);
        //test with wrong id
        restTemplate.put(ccfAPIUrl + "/hospitalentrys/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", dod.getRandomHospitalEntry());
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getNewTransientHospitalEntry(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'HospitalEntry' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/hospitalentrys/", obj, HospitalEntry.class);
        org.junit.Assert.assertNotNull("Expected 'HospitalEntry' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod.getRandomHospitalEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'HospitalEntry' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/hospitalentrys/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/"+ id, HospitalEntry.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
