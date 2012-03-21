package com.collabnet.ccf.ccfmaster.server.domain;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CCFCoreProperty {
	
	@NotNull
	@XmlAttribute(required=true)
	private String name;
	
	@NotNull
	@XmlAttribute(required=true)
	private String value;
	
	@XmlAttribute
	private Directions direction;
	
	@NotNull
	@XmlAttribute(required=true)
	private SystemKind systemKind;
	
	@XmlAttribute
	private String category;
	
	@NotNull
	@XmlAttribute(required= true)
	private String labelName;
	
	public CCFCoreProperty() {	}
	
	
	public Directions getDirection() {
		return direction;
	}

	public void setDirection(Directions directions) {
		this.direction = directions;
	}
	
	public SystemKind getSystemKind() {
		return systemKind;
	}
	public void setSystemKind(SystemKind systemKind) {
		this.systemKind = systemKind;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getLabelName() {
		return labelName;
	}


	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CCFCoreProperty [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", direction=");
		builder.append(direction);
		builder.append(", systemKind=");
		builder.append(systemKind);
		builder.append(", category=");
		builder.append(category);
		builder.append(", labelName=");
		builder.append(labelName);
		builder.append("]");
		return builder.toString();
	}
	
}
