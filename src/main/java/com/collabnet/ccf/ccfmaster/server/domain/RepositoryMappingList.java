package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class RepositoryMappingList extends ForwardingList<RepositoryMapping> {

	private List<RepositoryMapping> participant;

	public RepositoryMappingList() {
		this(new ArrayList<RepositoryMapping>());
	}
	public RepositoryMappingList(List<RepositoryMapping> participants) {
		this.setRepositoryMapping(participants);
	}

	@Override
	protected List<RepositoryMapping> delegate() {
		return getRepositoryMapping();
	}

	public void setRepositoryMapping(List<RepositoryMapping> participant) {
		this.participant = participant;
	}

	public List<RepositoryMapping> getRepositoryMapping() {
		return participant;
	}

}
