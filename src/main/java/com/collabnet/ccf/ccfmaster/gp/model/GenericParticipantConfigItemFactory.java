package com.collabnet.ccf.ccfmaster.gp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantConfigItemValidator;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public class GenericParticipantConfigItemFactory {

    @XmlTransient
    private IGenericParticipantConfigItemValidator customValidator;

    @XmlElement(name = "participantProperty")
    private List<CCFCoreProperty>                  participantFieldList;

    @XmlElement(name = "landscapeProperty")
    private List<CCFCoreProperty>                  landscapeFieldList;

    @XmlElement
    private boolean                                displayTestConnection;

    public IGenericParticipantConfigItemValidator getCustomValidator() {
        return customValidator;
    }

    public List<CCFCoreProperty> getLandscapeFieldList() {
        return landscapeFieldList;
    }

    public List<CCFCoreProperty> getParticipantFieldList() {
        return participantFieldList;
    }

    public boolean isDisplayTestConnection() {
        return displayTestConnection;
    }

    public void setCustomValidator(
            IGenericParticipantConfigItemValidator customValidator) {
        this.customValidator = customValidator;
    }

    public void setDisplayTestConnection(boolean displayTestConnection) {
        this.displayTestConnection = displayTestConnection;
    }

    public void setLandscapeFieldList(List<CCFCoreProperty> landscapeFieldList) {
        this.landscapeFieldList = landscapeFieldList;
    }

    public void setParticipantFieldList(
            List<CCFCoreProperty> participantFieldList) {
        this.participantFieldList = participantFieldList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GenericParticipantConfigBuilder [customValidator=");
        builder.append(customValidator);
        builder.append(", displayTestConnection=");
        builder.append(displayTestConnection);
        builder.append(", participantFieldList=");
        builder.append(participantFieldList);
        builder.append(", landscapeFieldList=");
        builder.append(landscapeFieldList);
        builder.append("]");
        return builder.toString();
    }
}
