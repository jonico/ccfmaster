package com.collabnet.ccf.ccfmaster.gp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantRMDValidator;
import com.collabnet.ccf.ccfmaster.gp.web.rmd.ICustomizeRMDParticipant;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class GenericParticipantRMDFactory {

    @XmlTransient
    private IGenericParticipantRMDValidator customRMDValidator;

    @XmlElement(name = "rmdParticipantProperty")
    private List<CCFCoreProperty>           participantSelectorFieldList;

    @XmlTransient
    private ICustomizeRMDParticipant        customParticipantRMD;

    public ICustomizeRMDParticipant getCustomParticipantRMD() {
        return customParticipantRMD;
    }

    public IGenericParticipantRMDValidator getCustomRMDValidator() {
        return customRMDValidator;
    }

    public List<CCFCoreProperty> getParticipantSelectorFieldList() {
        return participantSelectorFieldList;
    }

    public void setCustomParticipantRMD(
            ICustomizeRMDParticipant customParticipantRMD) {
        this.customParticipantRMD = customParticipantRMD;
    }

    public void setCustomRMDValidator(
            IGenericParticipantRMDValidator customRMDValidator) {
        this.customRMDValidator = customRMDValidator;
    }

    public void setParticipantSelectorFieldList(
            List<CCFCoreProperty> participantSelectorFieldList) {
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
