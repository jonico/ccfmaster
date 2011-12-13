package com.collabnet.ccf.ccfmaster.rest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;

public class ParticipantAPISystemTest extends AbstractAPISystemTest {
		
	@Test
	public void participantsExist() {
		ParticipantList participants = restTemplate.getForObject(ccfAPIUrl + "/participants", ParticipantList.class);
		assertTrue("no participants returned", ! participants.isEmpty());
		assertTrue(true);
	}
	
}
