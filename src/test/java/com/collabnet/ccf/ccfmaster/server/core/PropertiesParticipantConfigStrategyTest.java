package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.core.PropertiesConfigItemPersister;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigDataOnDemand;

import static org.junit.Assert.*;

@ContextConfiguration()
public class PropertiesParticipantConfigStrategyTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ParticipantConfigDataOnDemand dod;
	private ParticipantConfig pc;
	
	@Before
	public void init() {
		this.pc = dod.getRandomParticipantConfig();
	}
	
	@Test
	public void testSaveWithoutDB() throws IOException {
		final File propFile = File.createTempFile("test", ".properties");
		propFile.deleteOnExit();
		final PropertiesConfigItemPersister<ParticipantConfig> strategy = new PropertiesConfigItemPersister<ParticipantConfig>(propFile);
		strategy.save(pc);
		assertTrue(propFile + " doesn't exist.", propFile.exists());
		Properties props = strategy.loadProperties(propFile);
		assertTrue("properties don't have key " + pc.getName(), props.containsKey(pc.getName()));
		assertEquals("value " + pc.getVal() + " not in properties.", pc.getVal(), props.getProperty(pc.getName()));
	}
	
	@Test
	public void testDeleteWithoutDB() throws IOException {
		final File propFile = File.createTempFile("test", ".properties");
		propFile.deleteOnExit();
		final PropertiesConfigItemPersister<ParticipantConfig> strategy = new PropertiesConfigItemPersister<ParticipantConfig>(propFile);
		strategy.save(pc);
		strategy.delete(pc);
		assertTrue(propFile + " doesn't exist.", propFile.exists());
		Properties props = strategy.loadProperties(propFile);
		assertFalse("properties have key " + pc.getName(), props.containsKey(pc.getName()));
		assertNull("value " + pc.getVal() + " in properties.",  props.getProperty(pc.getName()));
	}
	
	@Test
	public void persistTriggersSaveProperties() throws IOException {
		final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc.getPersister();
		pc.persist();
		final File propFile = strategy.getPropFile();
		try {
			assertTrue(propFile + " doesn't exist.", propFile.exists());
			Properties props = strategy.loadProperties(propFile);
			assertTrue("properties don't have key " + pc.getName(), props.containsKey(pc.getName()));
			assertEquals("value " + pc.getVal() + " not in properties.", pc.getVal(), props.getProperty(pc.getName()));
		} finally {
			propFile.delete();
		}
	}
	
	@Test
	public void mergeTriggersSaveProperties() throws IOException {
		final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc.getPersister();
		final String newVal = "changed";
		pc.setVal(newVal);
		pc.merge();
		final File propFile = strategy.getPropFile();
		try {
			assertTrue(propFile + " doesn't exist.", propFile.exists());
			Properties props = strategy.loadProperties(propFile);
			assertTrue("properties don't have key " + pc.getName(), props.containsKey(pc.getName()));
			assertEquals("value " + pc.getVal() + " not in properties.", newVal, props.getProperty(pc.getName()));
		} finally {
			propFile.delete();
		}
	}

	@Test
	public void deleteProperties() throws IOException {
		final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc.getPersister();
		pc.remove();
		final File propFile = strategy.getPropFile();
		try {
			assertTrue(propFile + " doesn't exist.", propFile.exists());
			Properties props = strategy.loadProperties(propFile);
			assertFalse("properties have key " + pc.getName(), props.containsKey(pc.getName()));
			assertNull("value " + pc.getVal() + " in properties.",  props.getProperty(pc.getName()));
		} finally {
			propFile.delete();
		}		
	}

}
