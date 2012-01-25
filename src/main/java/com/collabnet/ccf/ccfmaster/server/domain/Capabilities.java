package com.collabnet.ccf.ccfmaster.server.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.collabnet.ccf.ccfmaster.config.Version;



@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Capabilities {
	
	private final ParticipantSystemKinds participantSystemKinds = new ParticipantSystemKinds();
	private final Version version = new Version(1, 0, 1, "$Revision$");
	
	@XmlElement
	public ParticipantSystemKinds getParticipantSystemKinds() {
		return participantSystemKinds;
	}

	@XmlElement(name="version")
	public Version getVersion() {
		return version;
	}

}
