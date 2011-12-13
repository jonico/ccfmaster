package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.Serializable;
import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.google.common.base.Preconditions;

/**
 * we need this class because "regular" Participant entities aren't Serializable
 * and we need to store a "participant" attribute in scope for the tab labels
 * and the menu in the views to work properly.
 */
public final class SerializableParticipant implements Serializable {

	private static final long serialVersionUID = 1L;

	private final SystemKind systemKind;

	/**
	 * Dig around in the database to find the participant. Default constructor is
	 * necessary to make Spring happy.
	 */
	public SerializableParticipant() {
		List<Landscape> landscapes = Landscape.findAllLandscapes();
		Preconditions.checkState(!landscapes.isEmpty(), "no landscapes in database");
		this.systemKind = landscapes.get(0).getParticipant().getSystemKind();
	}
	
	public SerializableParticipant(SystemKind systemKind) {
		this.systemKind = systemKind;
	}

	public SystemKind getSystemKind() {
		return systemKind;
	}


}
