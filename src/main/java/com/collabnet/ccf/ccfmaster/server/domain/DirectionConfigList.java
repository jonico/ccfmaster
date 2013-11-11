package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class DirectionConfigList extends ForwardingList<DirectionConfig> {

    private List<DirectionConfig> directionConfig;

    public DirectionConfigList() {
        this(new ArrayList<DirectionConfig>());
    }

    public DirectionConfigList(List<DirectionConfig> directionConfigs) {
        this.setDirectionConfig(directionConfigs);
    }

    public List<DirectionConfig> getDirectionConfig() {
        return directionConfig;
    }

    public void setDirectionConfig(List<DirectionConfig> directionConfig) {
        this.directionConfig = directionConfig;
    }

    @Override
    protected List<DirectionConfig> delegate() {
        return getDirectionConfig();
    }

}
