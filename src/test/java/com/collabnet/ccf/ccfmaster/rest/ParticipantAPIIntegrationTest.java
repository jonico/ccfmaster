package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;


public class ParticipantAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private ParticipantDataOnDemand dod;
	
	
	@Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod.getRandomParticipant();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/"+ id, Participant.class);
        org.junit.Assert.assertNotNull("Find method for 'Participant' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Participant' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testCount() {
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", dod.getRandomParticipant());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Participant.countParticipants();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", dod.getRandomParticipant());
        List<Participant> result = restTemplate.getForObject(ccfAPIUrl + "/participants", ParticipantList.class);
        org.junit.Assert.assertTrue("Counter for 'Participant' incorrectly reported there were no entries", count > 0);
        org.junit.Assert.assertNotNull("Find entries method for 'Participant' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Participant' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod.getRandomParticipant();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to provide an identifier", id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/"+ id, Participant.class);
        org.junit.Assert.assertNotNull("Find method for 'Participant' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyParticipant(obj);
        restTemplate.put(ccfAPIUrl + "/participants/"+ id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/"+ id, Participant.class);
        org.junit.Assert.assertTrue("Version for 'Participant' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testWrongUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod.getRandomParticipant();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to provide an identifier", id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/"+ id, Participant.class);
        org.junit.Assert.assertNotNull("Find method for 'Participant' illegally returned null for id '" + id + "'", obj);
        //test with wrong id
        restTemplate.put(ccfAPIUrl + "/participants/"+ id + 42, obj);
    }
    
    @Test
    public void testCreate() {
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", dod.getRandomParticipant());
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod.getNewTransientParticipant(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Participant' identifier to be null", obj.getId());
        obj = restTemplate.postForObject(ccfAPIUrl + "/participants/", obj, Participant.class);
        org.junit.Assert.assertNotNull("Expected 'Participant' identifier to no longer be null", obj.getId());
    }
    
    @Test(expected=HttpClientErrorException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod.getRandomParticipant();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Participant' failed to provide an identifier", id);
        restTemplate.delete(ccfAPIUrl + "/participants/"+ id);
        try {
        	obj = restTemplate.getForObject(ccfAPIUrl + "/participants/"+ id, Participant.class);
        } catch (HttpClientErrorException e) {
        	Assert.assertEquals("Expected 400", 400, e.getStatusCode().value());
        	throw e;
        }
    }

}
