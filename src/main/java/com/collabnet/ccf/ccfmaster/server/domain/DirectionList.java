package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class DirectionList extends ForwardingList<Direction> {

    private List<Direction> participant;

    public DirectionList() {
        this(new ArrayList<Direction>());
    }

    public DirectionList(List<Direction> participants) {
        this.setDirection(participants);
    }

    public List<Direction> getDirection() {
        return participant;
    }

    public void setDirection(List<Direction> participant) {
        this.participant = participant;
    }

    @Override
    protected List<Direction> delegate() {
        return getDirection();
    }

}
