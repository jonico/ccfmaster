package com.collabnet.ccf.ccfmaster.server.domain;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collabnet.ccf.ccfmaster.config.Version;

@Component
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Capabilities {

	
	private Version ccfMasterVersion;
	
	private Version coreVersion;
	
	private final ParticipantSystemKinds participantSystemKinds = new ParticipantSystemKinds();
	
	@XmlElement
	public ParticipantSystemKinds getParticipantSystemKinds() {
		return participantSystemKinds;
	}

	@XmlElement(name="version")
	public Version getVersion() {
		return ccfMasterVersion;
	}
	
	@Autowired(required=true)
	public void setVersion(Version version) {
		this.ccfMasterVersion = version;
	}

	@XmlElement(name="coreversion")
	public Version getCoreVersion() {
		return coreVersion;
	}

	public void setCoreVersion(Version coreVersion) {
		this.coreVersion = coreVersion;
	}

}
