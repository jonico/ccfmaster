package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class LandscapeConfigList extends ForwardingList<LandscapeConfig> {

    private List<LandscapeConfig> landscapeConfig;

    public LandscapeConfigList() {
        this(new ArrayList<LandscapeConfig>());
    }

    public LandscapeConfigList(List<LandscapeConfig> landscapeConfigs) {
        this.setLandscapeConfig(landscapeConfigs);
    }

    public List<LandscapeConfig> getLandscapeConfig() {
        return landscapeConfig;
    }

    public void setLandscapeConfig(List<LandscapeConfig> landscapeConfig) {
        this.landscapeConfig = landscapeConfig;
    }

    @Override
    protected List<LandscapeConfig> delegate() {
        return getLandscapeConfig();
    }

}
