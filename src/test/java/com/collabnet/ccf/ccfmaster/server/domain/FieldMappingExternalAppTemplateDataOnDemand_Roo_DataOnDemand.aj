// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import java.util.List;
import org.springframework.stereotype.Component;

privileged aspect FieldMappingExternalAppTemplateDataOnDemand_Roo_DataOnDemand {
    
    declare @type: FieldMappingExternalAppTemplateDataOnDemand: @Component;
    
    private List<FieldMappingExternalAppTemplate> FieldMappingExternalAppTemplateDataOnDemand.data;
    
    private void FieldMappingExternalAppTemplateDataOnDemand.setParent(FieldMappingExternalAppTemplate obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp parent = externalAppDataOnDemand.getRandomExternalApp();
        obj.setParent(parent);
    }
    
    private void FieldMappingExternalAppTemplateDataOnDemand.setName(FieldMappingExternalAppTemplate obj, int index) {
        java.lang.String name = "name_" + index;
        obj.setName(name);
    }
    
    private void FieldMappingExternalAppTemplateDataOnDemand.setDirection(FieldMappingExternalAppTemplate obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Directions direction = com.collabnet.ccf.ccfmaster.server.domain.Directions.class.getEnumConstants()[0];
        obj.setDirection(direction);
    }
    
    private void FieldMappingExternalAppTemplateDataOnDemand.setKind(FieldMappingExternalAppTemplate obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind kind = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind.class.getEnumConstants()[0];
        obj.setKind(kind);
    }
    
    public FieldMappingExternalAppTemplate FieldMappingExternalAppTemplateDataOnDemand.getSpecificFieldMappingExternalAppTemplate(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        FieldMappingExternalAppTemplate obj = data.get(index);
        return FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(obj.getId());
    }
    
    public FieldMappingExternalAppTemplate FieldMappingExternalAppTemplateDataOnDemand.getRandomFieldMappingExternalAppTemplate() {
        init();
        FieldMappingExternalAppTemplate obj = data.get(rnd.nextInt(data.size()));
        return FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(obj.getId());
    }
    
    public boolean FieldMappingExternalAppTemplateDataOnDemand.modifyFieldMappingExternalAppTemplate(FieldMappingExternalAppTemplate obj) {
        return false;
    }
    
    public void FieldMappingExternalAppTemplateDataOnDemand.init() {
        data = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplateEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'FieldMappingExternalAppTemplate' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate>();
        for (int i = 0; i < 10; i++) {
            com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = getNewTransientFieldMappingExternalAppTemplate(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
