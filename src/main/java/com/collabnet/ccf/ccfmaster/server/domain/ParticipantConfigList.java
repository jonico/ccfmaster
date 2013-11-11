package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class ParticipantConfigList extends ForwardingList<ParticipantConfig> {

    private List<ParticipantConfig> participantConfig;

    public ParticipantConfigList() {
        this(new ArrayList<ParticipantConfig>());
    }

    public ParticipantConfigList(List<ParticipantConfig> participantConfigs) {
        this.setParticipantConfig(participantConfigs);
    }

    public List<ParticipantConfig> getParticipantConfig() {
        return participantConfig;
    }

    public void setParticipantConfig(List<ParticipantConfig> participantConfig) {
        this.participantConfig = participantConfig;
    }

    @Override
    protected List<ParticipantConfig> delegate() {
        return getParticipantConfig();
    }

}
