package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.SystemUtils;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@XmlRootElement
public class ParticipantSystemKinds extends ForwardingList<SystemKind> {

	private final List<SystemKind> systemKinds;
	
	public ParticipantSystemKinds() {
		Builder<SystemKind> builder = ImmutableList.builder();
		if (SystemUtils.IS_OS_WINDOWS) {
			builder.add(SystemKind.QC);
		}
		builder.add(SystemKind.SWP);
		systemKinds = builder.build();
	}

	@Override
	protected List<SystemKind> delegate() {
		return getSystemKind();
	}

	public List<SystemKind> getSystemKind() {
		return systemKinds;
	}
	
}