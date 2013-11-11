package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class CcfCoreStatusList extends ForwardingList<CcfCoreStatus> {

    private List<CcfCoreStatus> ccfCoreStatus;

    public CcfCoreStatusList() {
        this(new ArrayList<CcfCoreStatus>());
    }

    public CcfCoreStatusList(List<CcfCoreStatus> ccfCoreStatuss) {
        this.setCcfCoreStatus(ccfCoreStatuss);
    }

    public List<CcfCoreStatus> getCcfCoreStatus() {
        return ccfCoreStatus;
    }

    public void setCcfCoreStatus(List<CcfCoreStatus> ccfCoreStatus) {
        this.ccfCoreStatus = ccfCoreStatus;
    }

    @Override
    protected List<CcfCoreStatus> delegate() {
        return getCcfCoreStatus();
    }

}
