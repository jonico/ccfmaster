package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class RepositoryMappingDirectionList extends ForwardingList<RepositoryMappingDirection> {

    private List<RepositoryMappingDirection> participant;

    public RepositoryMappingDirectionList() {
        this(new ArrayList<RepositoryMappingDirection>());
    }

    public RepositoryMappingDirectionList(
            List<RepositoryMappingDirection> participants) {
        this.setRepositoryMappingDirection(participants);
    }

    public List<RepositoryMappingDirection> getRepositoryMappingDirection() {
        return participant;
    }

    public void setRepositoryMappingDirection(
            List<RepositoryMappingDirection> participant) {
        this.participant = participant;
    }

    @Override
    protected List<RepositoryMappingDirection> delegate() {
        return getRepositoryMappingDirection();
    }

}
