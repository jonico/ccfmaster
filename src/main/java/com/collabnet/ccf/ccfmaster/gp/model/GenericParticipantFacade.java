package com.collabnet.ccf.ccfmaster.gp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * GenericParticipantFacade - holds all the configuration information needed to setup
 * Generic participant within the CCFMaster system
 * 
 * @author kbalaji
 * 
 */
@XmlRootElement(name = "participant")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericParticipantFacade {

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "prefix")
	private String prefix;

	@XmlElement(name = "systemKind")
	private String systemKind;

	@XmlElement(name = "description")
	private String description;
	
	@XmlTransient
	private GenericParticipantConfigItemFactory genericParticipantConfigItemFactory;
	
	@XmlTransient
	private GenericParticipantRMDFactory genericParticipantRMDFactory;
	
	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSystemKind() {
		return systemKind;
	}

	public String getDescription() {
		return description;
	}

	public GenericParticipantRMDFactory getGenericParticipantRMDFactory() {
		return genericParticipantRMDFactory;
	}

	public GenericParticipantConfigItemFactory getGenericParticipantConfigItemFactory() {
		return genericParticipantConfigItemFactory;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSystemKind(String systemKind) {
		this.systemKind = systemKind;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGenericParticipantConfigItemFactory(GenericParticipantConfigItemFactory genericParticipantConfigItemFactory) {
		this.genericParticipantConfigItemFactory = genericParticipantConfigItemFactory;
	}

	public void setGenericParticipantRMDFactory(GenericParticipantRMDFactory genericParticipantRMDFactory) {
		this.genericParticipantRMDFactory = genericParticipantRMDFactory;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenericParticipant [name=");
		builder.append(name);
		builder.append(", prefix=");
		builder.append(prefix);
		builder.append(", systemKind=");
		builder.append(systemKind);
		builder.append(", description=");
		builder.append(description);
		builder.append(", genericParticipantConfigItemFactory=");
		builder.append(genericParticipantConfigItemFactory);
		builder.append(", genericParticipantConfigItemFactory=");
		builder.append(genericParticipantConfigItemFactory);
		builder.append("]");
		return builder.toString();
	}

}
