package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppList;


public class ExternalAppAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private ExternalAppDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ id, ExternalApp.class);
        org.junit.Assert.assertNotNull("Find method for 'ExternalApp' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'ExternalApp' returned the incorrect identifier", id, obj.getId());
    }
	
	@Test
    public void testFindWithLinkId() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ obj.getLinkId(), ExternalApp.class);
        org.junit.Assert.assertNotNull("Find method for 'ExternalApp' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'ExternalApp' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", dod.getRandomExternalApp());
        long count = com.collabnet.ccf.ccfmaster.server.domain.ExternalApp.countExternalApps();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", dod.getRandomExternalApp());
        List<ExternalApp> result = restTemplate.getForObject(ccfAPIUrl + "/externalapps", ExternalAppList.class);
        org.junit.Assert.assertTrue("Counter for 'ExternalApp' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'ExternalApp' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'ExternalApp' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testCountLandscapeScope() {
    	com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        long count = com.collabnet.ccf.ccfmaster.server.domain.ExternalApp.countExternalAppsByLandscape(obj.getLandscape());
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", dod.getRandomExternalApp());
        List<ExternalApp> result = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + obj.getLandscape().getPlugId() +  "/externalapps", ExternalAppList.class);
        org.junit.Assert.assertTrue("Counter for 'ExternalApp' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'ExternalApp' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'ExternalApp' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ id, ExternalApp.class);
        org.junit.Assert.assertNotNull("Find method for 'ExternalApp' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyExternalApp(obj);
        restTemplate.put(ccfAPIUrl + "/externalapps/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ id, ExternalApp.class);
        org.junit.Assert.assertTrue("Version for 'ExternalApp' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ id, ExternalApp.class);
        org.junit.Assert.assertNotNull("Find method for 'ExternalApp' illegally returned null for id '" + id + "'", obj);
        restTemplate.put(ccfAPIUrl + "/externalapps/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", dod.getRandomExternalApp());
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getNewTransientExternalApp(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'ExternalApp' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/externalapps/", obj, ExternalApp.class);
        org.junit.Assert.assertNotNull("Expected 'ExternalApp' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod.getRandomExternalApp();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'ExternalApp' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/externalapps/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/"+ id, ExternalApp.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 404", 404, e.getStatusCode().value());
        	throw e;
        }
    }

}
