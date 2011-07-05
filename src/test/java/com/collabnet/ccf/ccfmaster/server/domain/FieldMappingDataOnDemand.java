package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingScope;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;

@RooDataOnDemand(entity = FieldMapping.class)
public class FieldMappingDataOnDemand {
    private Random rnd = new java.security.SecureRandom();
    
    @Autowired
    private RepositoryMappingDirectionDataOnDemand repositoryMappingDirectionDataOnDemand;

	public FieldMapping getNewTransientFieldMapping(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = new com.collabnet.ccf.ccfmaster.server.domain.FieldMapping();
        obj.setParent(repositoryMappingDirectionDataOnDemand.getRandomRepositoryMappingDirection());
        obj.setScope(FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
        obj.setParam("param_" + index);
		FieldMappingKind kind = FieldMappingKind.values()[rnd.nextInt(FieldMappingKind.values().length)];
        obj.setKind(kind);
        processMappingKind(obj);
        return obj;
    }
	
	public boolean modifyFieldMapping(FieldMapping fm) {
		if (!fm.getRules().isEmpty()) {
			fm.getRules().clear();
			processMappingKind(fm);
			return true;
		}
		return false;
	}

	public static void processMappingKind(Mapping<?> obj) {
        switch (obj.getKind()) {
		case CUSTOM_XSLT:
			FieldMappingRule customXsltRule = new FieldMappingRule();
			customXsltRule.setType(FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT);
			customXsltRule.setXmlContent("<custom />");
			customXsltRule.setSource("source");
			customXsltRule.setTarget("target");
			//customXsltRule.persist();
			obj.getRules().add(customXsltRule);
			break;
		case MAPFORCE:
			FieldMappingRule pre = new FieldMappingRule();
			pre.setType(FieldMappingRuleType.MAPFORCE_PRE);
			pre.setXmlContent("<pre />");
			pre.setSource("source");
			pre.setTarget("target");
			//pre.persist();
			obj.getRules().add(pre);
			FieldMappingRule main = new FieldMappingRule();
			main.setType(FieldMappingRuleType.MAPFORCE_MAIN);
			main.setXmlContent("<main />");
			main.setSource("source");
			main.setTarget("target");
			//main.persist();
			obj.getRules().add(main);
			FieldMappingRule post = new FieldMappingRule();
			post.setType(FieldMappingRuleType.MAPFORCE_POST);
			post.setXmlContent("<post />");
			post.setSource("source");
			post.setTarget("target");
			//post.persist();
			obj.getRules().add(post);
			FieldMappingRule sourceLayout = new FieldMappingRule();
			sourceLayout.setType(FieldMappingRuleType.SOURCE_REPOSITORY_LAYOUT);
			sourceLayout.setXmlContent("<source-layout />");
			sourceLayout.setSource("source");
			sourceLayout.setTarget("target");
			//post.persist();
			obj.getRules().add(sourceLayout);
			FieldMappingRule targetLayout = new FieldMappingRule();
			targetLayout.setType(FieldMappingRuleType.TARGET_REPOSITORY_LAYOUT);
			targetLayout.setXmlContent("<target-layout />");
			targetLayout.setSource("source");
			targetLayout.setTarget("target");
			//post.persist();
			obj.getRules().add(targetLayout);
			break;
		case MAPPING_RULES:
			// no rules.
			break;
		default:
			throw new IllegalArgumentException("unknown kind: " + obj.getKind());	
		}
	}
}
