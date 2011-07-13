package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;
import com.google.common.io.Resources;

public class LogFileTest{

	private static final String LOG_FILE = "persistence.xml";

	private static final String EMPTY_LOG_FILE = "empty.log";

	private File ccfHome;
	private File logDir;

	/* a sample landscape for us to play around with. not persisted */
	private Participant tf;
	private Participant swp;
	private Landscape landscape;
	private Direction direction;

	
	@Before
	public void setup() throws IOException {
		tf = new Participant();
		tf.setSystemKind(SystemKind.TF);
		swp = new Participant();
		swp.setSystemKind(SystemKind.SWP);
		landscape = new Landscape();
		landscape.setId(1L);
		landscape.setTeamForge(tf);
		landscape.setParticipant(swp);
		direction = new Direction();
		direction.setDirection(Directions.FORWARD);
		direction.setLandscape(landscape);
		
		ccfHome = Files.createTempDir();
		logDir = new File(ccfHome, "landscape1/samples/TFSWP/TF2SWP/logs");
		logDir.mkdirs();
		Files.touch(new File(logDir, EMPTY_LOG_FILE));
		final FileOutputStream to = new FileOutputStream(new File(logDir, LOG_FILE));
		Resources.copy(
				Resources.getResource("META-INF/"+LOG_FILE),
				to);
		to.close();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullFileNameThrows() throws IOException {
		new LogFile(ccfHome, direction, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void directoryTraversalThrows() throws IOException {
		new LogFile(ccfHome, direction, "../logs");
	}

	@Test(expected=FileNotFoundException.class)
	public void fileNotFoundThrows() throws IOException {
		new LogFile(ccfHome, direction, "doesNotExist.txt");
	}

	@Test
	public void validEmptyLogFile() throws IOException {
		LogFile logFile = new LogFile(ccfHome, direction, EMPTY_LOG_FILE);
		assertEquals(EMPTY_LOG_FILE, logFile.getName());
		assertSame(direction, logFile.getDirection());
		assertEquals(0L, logFile.getSize());
		LineIterator it = logFile.lines();
		try {
			assertFalse(it.hasNext());
			it.nextLine();
			fail("NoSuchElementException wasn't thrown at nextLine()");
		} catch (NoSuchElementException expected) {
		} finally {
			it.close();
		}
	}

	@Test
	public void validLogFile() throws IOException {
		LogFile logFile = new LogFile(ccfHome, direction, LOG_FILE);
		assertEquals(LOG_FILE, logFile.getName());
		assertSame(direction, logFile.getDirection());
		assertFalse("file " + LOG_FILE + " was empty", 0L == logFile.getSize());
		LineIterator it = logFile.lines();
		try {
			assertTrue(it.hasNext());
			it.nextLine();
		} catch (NoSuchElementException expected) {
		} finally {
			it.close();
		}
	}

	@After
	public void cleanup() throws IOException {
		FileUtils.deleteQuietly(ccfHome);
	}
}
