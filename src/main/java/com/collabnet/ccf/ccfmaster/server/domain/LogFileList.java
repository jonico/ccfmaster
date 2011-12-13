package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class LogFileList extends ForwardingList<LogFile> {

	private List<LogFile> logFile;

	public LogFileList() {
		this(new ArrayList<LogFile>());
	}
	public LogFileList(List<LogFile> participants) {
		this.setLogFile(participants);
	}

	@Override
	protected List<LogFile> delegate() {
		return getLogFile();
	}

	public void setLogFile(List<LogFile> logFile) {
		this.logFile = logFile;
	}

	public List<LogFile> getLogFile() {
		return logFile;
	}

}
