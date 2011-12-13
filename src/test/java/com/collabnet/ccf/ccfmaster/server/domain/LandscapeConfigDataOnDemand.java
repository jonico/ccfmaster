package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.collabnet.ccf.ccfmaster.server.core.PropertiesLandscapeConfigPersisterFactory;

@RooDataOnDemand(entity = LandscapeConfig.class)
public class LandscapeConfigDataOnDemand {
    @Autowired
    private LandscapeDataOnDemand landscapeDataOnDemand;

	public LandscapeConfig getNewTransientLandscapeConfig(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = new com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig();
        obj.setLandscape(landscapeDataOnDemand.getRandomLandscape());
        obj.setName(PropertiesLandscapeConfigPersisterFactory.PREFIX + "name_" + index);
        obj.setVal("val_" + index);
        return obj;
    }
}
