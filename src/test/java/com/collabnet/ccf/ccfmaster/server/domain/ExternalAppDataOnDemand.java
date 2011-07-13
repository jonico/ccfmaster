package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

@RooDataOnDemand(entity = ExternalApp.class)
public class ExternalAppDataOnDemand {
    @Autowired
    private LandscapeDataOnDemand landscapeDataOnDemand;
    
	public ExternalApp getNewTransientExternalApp(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = new com.collabnet.ccf.ccfmaster.server.domain.ExternalApp();
        obj.setLandscape(landscapeDataOnDemand.getRandomLandscape());
        obj.setLinkId("prpl" + index);
        obj.setProjectPath("projects.project" + index);
        return obj;
    }
}
