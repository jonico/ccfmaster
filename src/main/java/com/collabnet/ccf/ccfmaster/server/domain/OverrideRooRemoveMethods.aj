package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

public aspect OverrideRooRemoveMethods {
	before(Participant part) : execution(void Participant.remove()) && target(part) {
		List<Landscape> kids = Landscape.findLandscapesByTeamForgeOrParticipant(part, part).getResultList();
		for (Landscape l : kids) {
			l.remove();
		}
	}
	
	before(Landscape landscape) : execution(void Landscape.remove()) && target(landscape) {
		List<ExternalApp> kids = ExternalApp.findExternalAppsByLandscape(landscape).getResultList();
		for (ExternalApp ea : kids) {
			ea.remove();
		}
		List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplates = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParent(landscape).getResultList();
		for (FieldMappingLandscapeTemplate fieldMappingLandscapeTemplate : fieldMappingLandscapeTemplates) {
			fieldMappingLandscapeTemplate.remove();
		}
	}
	
	before(ExternalApp ea) : execution(void ExternalApp.remove()) && target(ea){
		List<RepositoryMapping> kids = RepositoryMapping.findRepositoryMappingsByExternalApp(ea).getResultList();
		for (RepositoryMapping rm : kids) {
			rm.remove();
		}
		List<FieldMappingExternalAppTemplate> externalAppTemplates = FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParent(ea).getResultList();
		for (FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate : externalAppTemplates) {
			fieldMappingExternalAppTemplate.remove();
		}
	}
	
	before(RepositoryMapping rm) : execution(void RepositoryMapping.remove()) && target(rm){
		List<RepositoryMappingDirection> kids = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMapping(rm).getResultList();
		for (RepositoryMappingDirection rmd : kids) {
			rmd.remove();
		}
	}
	
	before(RepositoryMappingDirection rmd) : execution(void RepositoryMappingDirection.remove()) && target(rmd){
		// make sure we don't run into integrity constraint violations
		rmd.setActiveFieldMapping(null);
		List<FieldMapping> kids = FieldMapping.findFieldMappingsByParent(rmd).getResultList();
		for (FieldMapping fm : kids) {
			fm.remove();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	before(Mapping fm) : execution(void Mapping.remove()) && target(fm){
		List<FieldMappingRule> rules = fm.getRules();
		for (FieldMappingRule rule : rules) {
			rule.remove();
		}
		fm.getRules().clear();
		final List<FieldMappingValueMap> valueMaps = fm.getValueMaps();
		for (FieldMappingValueMap valueMap : valueMaps) {
			valueMap.remove();
		}
		valueMaps.clear();
	}
}
