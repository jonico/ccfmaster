// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

privileged aspect ExternalAppDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ExternalAppDataOnDemand: @Component;
    
    private Random ExternalAppDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<ExternalApp> ExternalAppDataOnDemand.data;
    
    private void ExternalAppDataOnDemand.setLinkId(ExternalApp obj, int index) {
        java.lang.String linkId = "linkId_" + index;
        obj.setLinkId(linkId);
    }
    
    private void ExternalAppDataOnDemand.setProjectPath(ExternalApp obj, int index) {
        java.lang.String projectPath = "projectPath_" + index;
        obj.setProjectPath(projectPath);
    }
    
    private void ExternalAppDataOnDemand.setLandscape(ExternalApp obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape landscape = landscapeDataOnDemand.getRandomLandscape();
        obj.setLandscape(landscape);
    }
    
    public ExternalApp ExternalAppDataOnDemand.getSpecificExternalApp(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        ExternalApp obj = data.get(index);
        return ExternalApp.findExternalApp(obj.getId());
    }
    
    public ExternalApp ExternalAppDataOnDemand.getRandomExternalApp() {
        init();
        ExternalApp obj = data.get(rnd.nextInt(data.size()));
        return ExternalApp.findExternalApp(obj.getId());
    }
    
    public boolean ExternalAppDataOnDemand.modifyExternalApp(ExternalApp obj) {
        return false;
    }
    
    public void ExternalAppDataOnDemand.init() {
        data = com.collabnet.ccf.ccfmaster.server.domain.ExternalApp.findExternalAppEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'ExternalApp' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.ExternalApp>();
        for (int i = 0; i < 10; i++) {
            com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = getNewTransientExternalApp(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
