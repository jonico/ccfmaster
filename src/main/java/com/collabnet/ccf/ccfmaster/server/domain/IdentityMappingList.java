package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class IdentityMappingList extends ForwardingList<IdentityMapping> {

    private List<IdentityMapping> participant;

    public IdentityMappingList() {
        this(new ArrayList<IdentityMapping>());
    }

    public IdentityMappingList(List<IdentityMapping> participants) {
        this.setIdentityMapping(participants);
    }

    public List<IdentityMapping> getIdentityMapping() {
        return participant;
    }

    public void setIdentityMapping(List<IdentityMapping> participant) {
        this.participant = participant;
    }

    @Override
    protected List<IdentityMapping> delegate() {
        return getIdentityMapping();
    }

}
