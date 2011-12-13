package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class ExternalAppList extends ForwardingList<ExternalApp> {

	private List<ExternalApp> participant;

	public ExternalAppList() {
		this(new ArrayList<ExternalApp>());
	}
	public ExternalAppList(List<ExternalApp> participants) {
		this.setExternalApp(participants);
	}

	@Override
	protected List<ExternalApp> delegate() {
		return getExternalApp();
	}

	public void setExternalApp(List<ExternalApp> participant) {
		this.participant = participant;
	}

	public List<ExternalApp> getExternalApp() {
		return participant;
	}

}
