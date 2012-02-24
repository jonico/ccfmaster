package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;

@ContextConfiguration()
public class SingleLandscapeCCFCoreInteractionStrategyTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	private String[] propertyNames = {
			SingleLandscapeCCFCoreInteractionStrategy.CCF_DB_DRIVER,
			SingleLandscapeCCFCoreInteractionStrategy.CCF_DB_PASSWORD,
			SingleLandscapeCCFCoreInteractionStrategy.CCF_DB_URL,
			SingleLandscapeCCFCoreInteractionStrategy.CCF_FORWARD_JMX_PORT,
			SingleLandscapeCCFCoreInteractionStrategy.CCF_REVERSE_JMX_PORT,
			SingleLandscapeCCFCoreInteractionStrategy.CCF_DB_USERNAME 
	};

	@Autowired
	private LandscapeDataOnDemand dod;

	private Landscape landscape;

	File directoryToDelete;

	private File ccfHomeDirectory;

	private SingleLandscapeCCFCoreInteractionStrategy strategy;

	@Before
	public void setDirectoryToDeleteToNull() {
		directoryToDelete = null;
	}

	@After
	public void deleteDirectoryIfNecessary() throws IOException {
		if (directoryToDelete != null) {
			FileUtils.deleteDirectory(directoryToDelete);
		}
	}

	@Test
	public void testCreateWithoutDB() throws IOException {
		landscape = dod.getNewTransientLandscape(0);
		landscape.setId(42L);
		ccfHomeDirectory = File.createTempFile("ccfhome", null);
		ccfHomeDirectory.delete();
		ccfHomeDirectory.mkdir();
		directoryToDelete = ccfHomeDirectory;

		strategy = new SingleLandscapeCCFCoreInteractionStrategy();
		strategy.setCcfHome(ccfHomeDirectory.getAbsolutePath());
		strategy.create(landscape);

		File samplesDirectory = new File(ccfHomeDirectory, "landscape"
				+ landscape.getId() + File.separator + "samples");
		landscapePropertiesFile = new File(ccfHomeDirectory, "landscape"
				+ landscape.getId() + File.separator
				+ strategy.getImmutableLandscapePropertyFileName());
		assertTrue(samplesDirectory + " doesn't exist.",
				samplesDirectory.exists());
		Properties landscapeIdProperties = new Properties();
		FileInputStream inStream = new FileInputStream(landscapePropertiesFile);
		landscapeIdProperties.load(inStream);
		inStream.close();
		assertEquals(landscape.getId().toString(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_ID));
		assertEquals(landscape.getPlugId(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_PLUG_ID));
		assertEquals(landscape.getName(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_DESCRIPTION));
		
		for (String propertyName : propertyNames) {
			assertTrue(propertyName.equals(landscapeIdProperties.get(propertyName)));
		}
		
	}

	@Autowired
	SingleLandscapeCCFCoreInteractionStrategy wiredInStrategy;

	private File landscapePropertiesFile;

	@Test
	public void testCreateWithDB() throws IOException {
		landscape = dod.getNewTransientLandscape(0);
		landscape.persist();
		ccfHomeDirectory = new File(wiredInStrategy.getCcfHome());
		directoryToDelete = ccfHomeDirectory;
		File samplesDirectory = new File(ccfHomeDirectory, "landscape"
				+ landscape.getId() + File.separator + "samples");
		landscapePropertiesFile = new File(ccfHomeDirectory, "landscape"
				+ landscape.getId() + File.separator
				+ wiredInStrategy.getImmutableLandscapePropertyFileName());
		assertTrue(samplesDirectory + " doesn't exist.",
				samplesDirectory.exists());
		Properties landscapeIdProperties = new Properties();
		FileInputStream inStream = new FileInputStream(landscapePropertiesFile);
		landscapeIdProperties.load(inStream);
		inStream.close();
		assertEquals(landscape.getId().toString(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_ID));
		assertEquals(landscape.getPlugId(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_PLUG_ID));
		assertEquals(landscape.getName(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_DESCRIPTION));
		
		for (String propertyName : propertyNames) {
			assertEquals(propertyName, landscapeIdProperties.get(propertyName));
		}
		
		// TODO Find a better test for field mapping landscape template creation 
		assertEquals(89, FieldMappingRule.findAllFieldMappingRules().size());
	}

	@Test
	public void testDeleteWithoutDB() throws IOException {
		testCreateWithoutDB();
		strategy.delete(landscape);
		// File samplesDirectory = new File (ccfHomeDirectory, "landscape" +
		// landscape.getId() + File.separator + "samples");
		// assertTrue(samplesDirectory + " does still exist.",
		// samplesDirectory.exists());
		File archivedSamplesDirectory = new File(ccfHomeDirectory, "archive"
				+ File.separator + "landscape" + landscape.getId()
				+ File.separator + "samples");
		assertTrue(archivedSamplesDirectory + " doesn't exist.",
				archivedSamplesDirectory.exists());
	}

	@Test
	public void testUpdateWithoutDB() throws IOException {
		testCreateWithoutDB();
		landscape.setName("Updated description");
		landscape.setPlugId("plug1234");
		Map<String, String> propertyMap = strategy.getPropertyMap();
		for (String propertyName : propertyNames) {
			propertyMap.put(propertyName, "'Updated'+" + propertyMap.get(propertyName));
		}

		strategy.update(landscape);
		FileInputStream inStream = new FileInputStream(landscapePropertiesFile);
		Properties landscapeIdProperties = new Properties();
		landscapeIdProperties.load(inStream);
		inStream.close();
		assertEquals(landscape.getId().toString(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_ID));
		assertEquals(landscape.getPlugId(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_PLUG_ID));
		assertEquals(landscape.getName(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_DESCRIPTION));
		
		for (String propertyName : propertyNames) {
			assertEquals("Updated" + propertyName, landscapeIdProperties.get(propertyName));
		}
		
	}

	@Test
	public void testUpdateWithDB() throws IOException {
		testCreateWithDB();
		landscape.setName("Updated description");
		landscape.setPlugId("plug1234");
		landscape.merge();
		FileInputStream inStream = new FileInputStream(landscapePropertiesFile);
		Properties landscapeIdProperties = new Properties();
		landscapeIdProperties.load(inStream);
		inStream.close();
		assertEquals(landscape.getId().toString(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_ID));
		assertEquals(landscape.getPlugId(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_PLUG_ID));
		assertEquals(landscape.getName(), landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_DESCRIPTION));
	}

	@Test
	public void testDeleteWithDB() throws IOException {
		testCreateWithDB();
		landscape.remove();
		// File samplesDirectory = new File (ccfHomeDirectory, "landscape" +
		// landscape.getId() + File.separator + "samples");
		// assertTrue(samplesDirectory + " does still exist.",
		// samplesDirectory.exists());
		File archivedSamplesDirectory = new File(ccfHomeDirectory, "archive"
				+ File.separator + "landscape" + landscape.getId()
				+ File.separator + "samples");
		assertTrue(archivedSamplesDirectory + " doesn't exist.",
				archivedSamplesDirectory.exists());
	}

}
