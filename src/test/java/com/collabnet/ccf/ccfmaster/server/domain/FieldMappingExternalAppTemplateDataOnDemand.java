package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Random;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FieldMappingExternalAppTemplate.class)
public class FieldMappingExternalAppTemplateDataOnDemand {

	@Autowired
    private ExternalAppDataOnDemand externalAppDataOnDemand;
	private Random rnd = new java.security.SecureRandom();

	public FieldMappingExternalAppTemplate getNewTransientFieldMappingExternalAppTemplate(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = new com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate();
        obj.setParent(externalAppDataOnDemand.getRandomExternalApp());
        obj.setName("name_" + index);
        Directions dir = Directions.values()[rnd.nextInt(Directions.values().length)];
        obj.setDirection(dir);
		FieldMappingKind kind = FieldMappingKind.values()[rnd.nextInt(FieldMappingKind.values().length)];
        obj.setKind(kind);
        FieldMappingDataOnDemand.processMappingKind(obj);
        return obj;
    }

}
