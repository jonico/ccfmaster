package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

@RooDataOnDemand(entity = FieldMappingLandscapeTemplate.class)
public class FieldMappingLandscapeTemplateDataOnDemand {
    @Autowired
    private LandscapeDataOnDemand landscapeDataOnDemand;
    private Random                rnd = new java.security.SecureRandom();

    public FieldMappingLandscapeTemplate getNewTransientFieldMappingLandscapeTemplate(
            int index) {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = new com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate();
        obj.setParent(landscapeDataOnDemand.getRandomLandscape());
        obj.setName("name_" + index);
        Directions dir = Directions.values()[rnd
                .nextInt(Directions.values().length)];
        obj.setDirection(dir);
        FieldMappingKind kind = FieldMappingKind.values()[rnd
                .nextInt(FieldMappingKind.values().length)];
        obj.setKind(kind);
        FieldMappingDataOnDemand.processMappingKind(obj);
        return obj;
    }
}
