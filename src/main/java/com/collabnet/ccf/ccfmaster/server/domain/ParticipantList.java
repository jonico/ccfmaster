package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class ParticipantList extends ForwardingList<Participant> {

    private List<Participant> participant;

    public ParticipantList() {
        this(new ArrayList<Participant>());
    }

    public ParticipantList(List<Participant> participants) {
        this.setParticipant(participants);
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    @Override
    protected List<Participant> delegate() {
        return getParticipant();
    }

}
