package com.collabnet.ccf.ccfmaster.gp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class GenericParticipantConfigItemFactory {

	@XmlTransient
	private IGenericParticipantValidator<AbstractGenericParticipantModel> customValidator;
	
	@XmlElement(name = "participantProperty")
	private List<CCFCoreProperty> participantFieldList;

	@XmlElement(name = "landscapeProperty")
	private List<CCFCoreProperty> landscapeFieldList;
	
	@XmlElement(name = "directionProperty")
	private List<CCFCoreProperty> directionFieldList;


	public IGenericParticipantValidator<AbstractGenericParticipantModel> getCustomValidator() {
		return customValidator;
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

	public void setCustomValidator(IGenericParticipantValidator<AbstractGenericParticipantModel> customValidator) {
		this.customValidator = customValidator;
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
		builder.append("GenericParticipantConfigBuilder [customValidator=");
		builder.append(customValidator);
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
