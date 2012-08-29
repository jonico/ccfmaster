package com.collabnet.ccf.ccfmaster.gp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

/**
 * GenericParticipant - holds all the configuration information needed to setup
 * Generic participant within the CCFMaster system
 * 
 * @author kbalaji
 * 
 */
@XmlRootElement(name = "participant")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericParticipant {

	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "prefix")
	private String prefix;

	@XmlElement(name = "systemKind")
	private String systemKind;

	@XmlElement(name = "description")
	private String description;

	@XmlElement(name = "participantProperty")
	private List<CCFCoreProperty> participantFieldList;

	@XmlElement(name = "landscapeProperty")
	private List<CCFCoreProperty> landscapeFieldList;
	
	@XmlElement(name = "directionProperty")
	private List<CCFCoreProperty> directionFieldList;

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

	public List<CCFCoreProperty> getParticipantFieldList() {
		return participantFieldList;
	}

	public List<CCFCoreProperty> getLandscapeFieldList() {
		return landscapeFieldList;
	}

	public List<CCFCoreProperty> getDirectionFieldList() {
		return directionFieldList;
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

	public void setParticipantFieldList(List<CCFCoreProperty> participantFieldList) {
		this.participantFieldList = participantFieldList;
	}

	public void setLandscapeFieldList(List<CCFCoreProperty> landscapeFieldList) {
		this.landscapeFieldList = landscapeFieldList;
	}

	public void setDirectionFieldList(List<CCFCoreProperty> directionFieldList) {
		this.directionFieldList = directionFieldList;
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
		builder.append(", participantFieldList=");
		builder.append(participantFieldList);
		builder.append(", landscapeFieldList=");
		builder.append(landscapeFieldList);
		builder.append(", directionFieldList=");
		builder.append(directionFieldList);
		builder.append("]");
		return builder.toString();
	}


}
