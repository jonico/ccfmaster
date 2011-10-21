package com.collabnet.ccf.ccfmaster.server.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;

@ContextConfiguration
public class PreventLandscapeDescriptionModificationTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private LandscapeDataOnDemand dod;
	
	@Test(expected=CoreConfigurationException.class)
	public void modifyingLandscapeDescriptionIsPrevented() {
		Landscape landscape = dod.getRandomLandscape();
		Landscape modifiedLandscape = new Landscape();
		modifiedLandscape.setId(landscape.getId());
		modifiedLandscape.setParticipant(landscape.getParticipant());
		modifiedLandscape.setTeamForge(landscape.getTeamForge());
		modifiedLandscape.setPlugId(landscape.getPlugId());
		modifiedLandscape.setVersion(landscape.getVersion());

		modifiedLandscape.setName("modified "+landscape.getName());
		modifiedLandscape.merge();
	}
	
}
