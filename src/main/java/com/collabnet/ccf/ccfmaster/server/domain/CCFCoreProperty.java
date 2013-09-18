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
	
	@XmlAttribute
	private String toolTip;
	
	@XmlAttribute
	private  CCFCorePropertyType type;
	
	@XmlAttribute
	private boolean displayInHTML;
	
	@XmlAttribute
	private String conditionalRegex;
	
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
	
	
	public String getToolTip() {
		return toolTip;
	}


	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}


	public CCFCorePropertyType getType() {
		return type;
	}


	public void setType(CCFCorePropertyType type) {
		this.type = type;
	}

	public boolean isDisplayInHTML() {
		return displayInHTML;
	}

	public void setDisplayInHTML(boolean displayInHTML) {
		this.displayInHTML = displayInHTML;
	}


	public String getConditionalRegex() {
		return conditionalRegex;
	}


	public void setConditionalRegex(String conditionalRegex) {
		this.conditionalRegex = conditionalRegex;
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
		builder.append(", toolTip=");
		builder.append(toolTip);
		builder.append(", type=");
		builder.append(type);
		builder.append(", displayInHTML=");
		builder.append(displayInHTML);
		builder.append(", conditionalRegex=");
		builder.append(conditionalRegex);
		builder.append("]");
		return builder.toString();
	}
	
}
