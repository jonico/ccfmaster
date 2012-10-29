package com.collabnet.ccf.ccfmaster.gp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.gp.web.rmd.ICustomizeRMDParticipant;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class GenericParticipantRMDFactory {
	
	@XmlTransient
	private IGenericParticipantValidator<RMDModel> customRMDValidator;
	
	@XmlElement(name = "rmdParticipantProperty")
	private List<CCFCoreProperty> participantSelectorFieldList;
	
	@XmlTransient
	private ICustomizeRMDParticipant<RMDModel> customParticipantRMD;

	public IGenericParticipantValidator<RMDModel> getCustomRMDValidator() {
		return customRMDValidator;
	}

	public void setCustomRMDValidator(IGenericParticipantValidator<RMDModel> customRMDValidator) {
		this.customRMDValidator = customRMDValidator;
	}

	public ICustomizeRMDParticipant<RMDModel> getCustomParticipantRMD() {
		return customParticipantRMD;
	}

	public void setCustomParticipantRMD(ICustomizeRMDParticipant<RMDModel> customParticipantRMD) {
		this.customParticipantRMD = customParticipantRMD;
	}

	public List<CCFCoreProperty> getParticipantSelectorFieldList() {
		return participantSelectorFieldList;
	}

	public void setParticipantSelectorFieldList(List<CCFCoreProperty> participantSelectorFieldList) {
		this.participantSelectorFieldList = participantSelectorFieldList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenericParticipantRMDBuilder [customRMDValidator=");
		builder.append(customRMDValidator);
		builder.append(", participantSelectorList=");
		builder.append(participantSelectorFieldList);
		builder.append(", customParticipantRMD=");
		builder.append(customParticipantRMD);
		builder.append("]");
		return builder.toString();
	}

}
