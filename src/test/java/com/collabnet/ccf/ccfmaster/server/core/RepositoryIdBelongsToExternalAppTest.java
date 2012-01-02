package com.collabnet.ccf.ccfmaster.server.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;


@ContextConfiguration
public class RepositoryIdBelongsToExternalAppTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private RepositoryMappingDataOnDemand dod;

	@Test(expected=CoreConfigurationException.class)
	public void preventRepositoryidNotEqualsExternalApp() {

		RepositoryMapping rMapping=dod.getRandomRepositoryMapping();
		RepositoryMapping modifiedRMapping= new RepositoryMapping();
		modifiedRMapping.setDescription("Modified Description");
		modifiedRMapping.setExternalApp(rMapping.getExternalApp());
		modifiedRMapping.setId(rMapping.getId());
		modifiedRMapping.setParticipantRepositoryId(rMapping.getParticipantRepositoryId());
		modifiedRMapping.setTeamForgeRepositoryId(rMapping.getTeamForgeRepositoryId());
		modifiedRMapping.setVersion(rMapping.getVersion());
		modifiedRMapping.merge();	
	}

}
