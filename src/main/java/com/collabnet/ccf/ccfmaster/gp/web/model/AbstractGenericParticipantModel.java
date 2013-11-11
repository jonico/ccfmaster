package com.collabnet.ccf.ccfmaster.gp.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;

public abstract class AbstractGenericParticipantModel {

    @SuppressWarnings("unchecked")
    private List<CCFCoreProperty> landscapeConfigList   = LazyList
                                                                .decorate(
                                                                        new ArrayList<CCFCoreProperty>(),
                                                                        FactoryUtils
                                                                                .instantiateFactory(CCFCoreProperty.class));

    @SuppressWarnings("unchecked")
    private List<CCFCoreProperty> participantConfigList = LazyList
                                                                .decorate(
                                                                        new ArrayList<CCFCoreProperty>(),
                                                                        FactoryUtils
                                                                                .instantiateFactory(CCFCoreProperty.class));

    public List<CCFCoreProperty> getLandscapeConfigList() {
        return landscapeConfigList;
    }

    public List<CCFCoreProperty> getParticipantConfigList() {
        return participantConfigList;
    }

    public void setLandscapeConfigList(List<CCFCoreProperty> landscapeConfigList) {
        this.landscapeConfigList = landscapeConfigList;
    }

    public void setParticipantConfigList(
            List<CCFCoreProperty> participantConfigList) {
        this.participantConfigList = participantConfigList;
    }

}
