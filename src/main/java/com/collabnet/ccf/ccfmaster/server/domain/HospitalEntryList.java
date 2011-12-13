package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class HospitalEntryList extends ForwardingList<HospitalEntry> {

	private List<HospitalEntry> participant;

	public HospitalEntryList() {
		this(new ArrayList<HospitalEntry>());
	}
	public HospitalEntryList(List<HospitalEntry> participants) {
		this.setHospitalEntry(participants);
	}

	@Override
	protected List<HospitalEntry> delegate() {
		return getHospitalEntry();
	}

	public void setHospitalEntry(List<HospitalEntry> participant) {
		this.participant = participant;
	}

	public List<HospitalEntry> getHospitalEntry() {
		return participant;
	}

}
