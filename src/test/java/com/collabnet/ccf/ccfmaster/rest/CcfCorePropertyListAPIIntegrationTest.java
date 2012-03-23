package com.collabnet.ccf.ccfmaster.rest;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.config.CoreConfigLoader;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;

public class CcfCorePropertyListAPIIntegrationTest extends AbstractAPIIntegrationTest {
	
	@Autowired
    private DirectionDataOnDemand dirdod;
	
	@Autowired
	private CoreConfigLoader coreConfigLoader;
	
	@Test
	public void testFind() throws JAXBException, IOException {
		Direction dir = dirdod.getRandomDirection();
		Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly",dir);
		Long id = dir.getId();
		Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier",id);
		CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl+ "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
		Assert.assertNotNull("Find method for 'CCFCorePropertyList' illegally returned null for id '"+ id + "'", obj);
		Assert.assertNotNull(coreConfigLoader.populateDirectionSpecificList(dir));
		Assert.assertEquals(obj.getCcfCoreProperties().size(), coreConfigLoader.populateDirectionSpecificList(dir).size());

	}
	
	@Test(expected=HttpClientErrorException.class)
	public void testShowWithInvalidId() throws JAXBException, IOException {
		CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl+ "/ccfcoreproperties/101", CCFCorePropertyList.class);
		Assert.assertNull(obj);

	}
	
}
