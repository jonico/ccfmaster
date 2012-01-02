package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Configurable
public class LogFile {
	
	@Autowired
	private transient LogFileFactory logFileFactory;
	
	private static LogFileFactory logFileFactory() {
		LogFileFactory logFileFactory = new LogFile().logFileFactory;
		if (logFileFactory == null) {
			throw new IllegalStateException("Logfile factory has not been injected");
		}
		return logFileFactory;
	}

	private final File logFile;
	private final Direction direction;
	
	/**
	 * JAXB requires a no-arg constructor to be present.
	 * Only used by logFileFactory() method
	 */
	private LogFile() {
		logFile = null;
		direction = null;
	}
	
	LogFile(File ccfHome, Direction direction, String fileName) throws IOException {
		Assert.notNull(fileName);
		final File baseDir = direction.determineBaseDirectory(ccfHome);
		final File logDir = new File(baseDir, "logs");
		this.direction = direction;
		this.logFile = new File(logDir, fileName);
		if (!logFile.exists()) {
			throw new FileNotFoundException("File not found: " + logFile.getName());
		}
		Assert.isTrue(
				logFile.getCanonicalPath().startsWith(ccfHome.getCanonicalPath()),
				"log file is not in ccf home directory.");
		Assert.isTrue(
				logFile.getParentFile().equals(logDir),
				"log file is not in log directory for direction " + direction.getDescription());
	}
	
	@XmlElement
	public String getName() {
		return logFile.getName();
	}
	
	@XmlElement
	public long getSize() {
		return logFile.length();
	}
	
	@XmlJavaTypeAdapter(Direction.XmlAdapter.class)
	public Direction getDirection() {
		return direction;
	}
	
	@XmlElement
	public Date getLastModifiedDate() {
		return new Date(logFile.lastModified());
	}

	/**
	 * non-JavaBean method because we don't want JAXB et al accidentally
	 * exposing this while still allowing programmatic access.
	 */
	public File logFile() {
		return logFile;
	}
	
	/**
	 * returns a {@link LineIterator} for the current log file. Be sure to close
	 * the iterator after use by calling {@link LineIterator#close()} or
	 * {@link LineIterator#closeQuietly(LineIterator)}.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * LineIterator it = logFile.lines();
	 * try {
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// do something with line
	 * 	}
	 * } finally {
	 * 	it.close();
	 * }
	 * </pre>
	 * 
	 * @return a {@link LineIterator} for the current log file.
	 * @throws FileNotFoundException
	 *             if the current log file doesn't exist.
	 */
	public LineIterator lines() throws FileNotFoundException {
		// assumes that CCF core uses the system default encoding for writing log files.
		final FileReader reader = new FileReader(logFile);
		return new LineIterator(reader);
	}
	
	/**
	 * returns a {@link LineIterator} for the current log file that skips all
	 * lines before startLineNumber.
	 * 
	 * Be sure to close the iterator after use by calling
	 * {@link LineIterator#close()} or
	 * {@link LineIterator#closeQuietly(LineIterator)}.
	 * 
	 * @param startLineNumber
	 *            the (zero-based) line number of the first line that
	 *            {@link LineIterator#next()} will return.
	 * @see #lines()
	 */
	public LineIterator linesFrom(long startLineNumber) throws FileNotFoundException {
		LineIterator it = lines();
		for (long i = 0; it.hasNext() && i < startLineNumber; it.nextLine(), i++);
		return it;
	}
	
	public static List<LogFile> findLogFilesByDirection(Direction direction) throws IOException {
		return logFileFactory().findLogFilesByDirection(direction);
	}
	
	public static LogFile findLogFile(Direction direction, String fileName) throws IOException {
		// the LogFile constructor validates the parameters
		return logFileFactory().findLogFile(direction, fileName);
	}
}
