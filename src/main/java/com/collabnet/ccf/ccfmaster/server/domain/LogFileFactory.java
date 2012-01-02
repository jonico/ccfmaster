package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class LogFileFactory {
	private File baseDirectory;
	
	static final FileFilter readableRegularFiles = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname != null && pathname.isFile() && pathname.canRead();
		}
	};
	
	static final Comparator<File> fileNameComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};;
	
	public List<LogFile> findLogFilesByDirection(Direction direction) throws IOException {
		final File logDir = logDirectory(direction);
		final File[] logFiles = logDir.listFiles(readableRegularFiles);
		if (logFiles == null) {
			// the logs directory is created when CCF is run for the first time.
			// listFiles returns null if dir doesn't exist => we return empty list.
			return Collections.emptyList();
		}
		Arrays.sort(logFiles, fileNameComparator);
		
		ImmutableList.Builder<LogFile> builder = ImmutableList.builder();
		for (File f : logFiles) {
			String fileName = f.getName();
			builder.add(new LogFile(getBaseDirectory(), direction, fileName));
		}
		return builder.build();
	}

	public LogFile findLogFile(Direction direction, String fileName) throws IOException {
		// the LogFile constructor validates the parameters
		return new LogFile(getBaseDirectory(), direction, fileName);
	}

	private File logDirectory(Direction direction) {
		final File baseDir = direction.determineBaseDirectory(getBaseDirectory());
		final File logDir = new File(baseDir, "logs");
		return logDir;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = new File(baseDirectory);
	}

	private File getBaseDirectory() {
		return baseDirectory;
	}
	
}
