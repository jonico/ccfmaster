package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class LandscapeList extends ForwardingList<Landscape> {

    private List<Landscape> participant;

    public LandscapeList() {
        this(new ArrayList<Landscape>());
    }

    public LandscapeList(List<Landscape> participants) {
        this.setLandscape(participants);
    }

    public List<Landscape> getLandscape() {
        return participant;
    }

    public void setLandscape(List<Landscape> participant) {
        this.participant = participant;
    }

    @Override
    protected List<Landscape> delegate() {
        return getLandscape();
    }

}
