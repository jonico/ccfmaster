package com.collabnet.ccf.ccfmaster.gp.controller.web;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.collabnet.ccf.ccfmaster.controller.api.BadRequestException;
import com.collabnet.ccf.ccfmaster.gp.model.GenericParticipantFacade;
import com.collabnet.ccf.ccfmaster.gp.validator.IGenericParticipantRMDValidator;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.gp.web.rmd.ICustomizeRMDParticipant;
import com.collabnet.ccf.ccfmaster.server.domain.ConfigItem;
import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingScope;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeMetadataHelper;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.ProjectDO;

import flexjson.JSONSerializer;

/**
 * CreateRMDHelper - handles create repository mapping direction
 * 
 * @author kbalaji
 *
 */
@Configurable
public class CreateRMDHelper {
	
	private static final String PLANNINGFOLDERS = "planningFolders";

	@Autowired(required= false)
	public GenericParticipantFacade genericParticipant;
	
	private boolean validateRMDAgainstExternalApp = false;
	
	public boolean isValidateRMDAgainstExternalApp() {
		return validateRMDAgainstExternalApp;
	}

	public void setValidateRMDAgainstExternalApp(boolean validateRMDAgainstExternalApp) {
		this.validateRMDAgainstExternalApp = validateRMDAgainstExternalApp;
	}

	public void createAndPersistRMD(RMDModel rmdmodel, Model model,String direction, ExternalApp extApp) {
		ConflictResolutionPolicy forwardConflictPolicy = null,reverseConflictPolicy = null;
		if(!rmdmodel.getForwardConflictPolicies().isEmpty()){
			forwardConflictPolicy = ConflictResolutionPolicy.valueOf(rmdmodel.getForwardConflictPolicies());
		}
		if(!rmdmodel.getReversedConflictPolicies().isEmpty()){
			reverseConflictPolicy = ConflictResolutionPolicy.valueOf(rmdmodel.getReversedConflictPolicies());
		}
		if("FORWARD".equalsIgnoreCase(direction)){
			buildRepositoryMappingDir(model, rmdmodel, forwardConflictPolicy, Directions.FORWARD,rmdmodel.getForwardFieldMappingTemplateName(),extApp);
		}else if("REVERSE".equalsIgnoreCase(direction)){
			buildRepositoryMappingDir(model, rmdmodel, reverseConflictPolicy, Directions.REVERSE,rmdmodel.getReverseFieldMappingTemplateName(),extApp);	
		}else{
			buildRepositoryMappingDir(model, rmdmodel, forwardConflictPolicy, Directions.FORWARD,rmdmodel.getForwardFieldMappingTemplateName(),extApp);
			buildRepositoryMappingDir(model, rmdmodel, reverseConflictPolicy, Directions.REVERSE,rmdmodel.getReverseFieldMappingTemplateName(),extApp);
		}
	}

	
	public String getAllTrackerInfo(String projectId){
		List<Map<String, String>> teamForgeTracker = new ArrayList<Map<String,String>>();
		try {
			teamForgeTracker = TeamForgeMetadataHelper.getAllTrackersOfProject(projectId);
		} catch (RemoteException e) { }//ignore the remote exception
		return new JSONSerializer().serialize(teamForgeTracker);		
	}
		
	

	public void buildRepositoryMappingDir(Model model, RMDModel rmdmodel,ConflictResolutionPolicy conflictPolicy,Directions directions,String templateName,ExternalApp extApp) {
		String teamForgeRepositoryId = getTeamForgeRepoId(rmdmodel);
		String participantRepositoryId = getParticipantRepoId(rmdmodel);
		RepositoryMapping repositoryMapping = getRespositoryMapping(extApp, teamForgeRepositoryId, participantRepositoryId);
		if(isValidateRMDAgainstExternalApp()){
			validateRepositoryMapping(extApp, repositoryMapping);
		}
		RepositoryMappingDirection repoMappingDirection = getRepositoryMappingDirection(directions, repositoryMapping, conflictPolicy);
		FieldMapping fieldMapping = getFieldMapping(templateName,directions, repoMappingDirection);
		mergeRepositoryMappingDirection(repoMappingDirection, fieldMapping);			
		model.addAttribute("teamForgeRepoId", teamForgeRepositoryId);
		model.addAttribute("participanteRepoId", participantRepositoryId);
	}

	public void validateRMD(RMDModel rmdmodel, BindingResult bindingResult,Model model) {
		if(genericParticipant != null){
			IGenericParticipantRMDValidator rmdValidator = genericParticipant.getGenericParticipantRMDFactory().getCustomRMDValidator();
			if(rmdValidator!= null){
				rmdValidator.validate(rmdmodel, bindingResult);
			}
		}
	}

	public void populateConfigMaps(RMDModel rmdmodel) {
		List<ParticipantConfig> participantConfig = ParticipantConfig.findAllParticipantConfigs();
		List<LandscapeConfig> landscapeConfig = LandscapeConfig.findAllLandscapeConfigs();
		rmdmodel.setParticipantConfigMap(buildConfigMap(participantConfig));
		rmdmodel.setLandscapeConfigMap(buildConfigMap(landscapeConfig));
	}
	
	public List<String> getFieldMappingTemplateNames(Directions direction) {
		List<String> fieldMappingTemplateNames = new ArrayList<String>();
		List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplates = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByDirection(direction).getResultList();
		for(FieldMappingLandscapeTemplate template: fieldMappingLandscapeTemplates){
			fieldMappingTemplateNames.add(template.getName());
		}
		return fieldMappingTemplateNames;
	}
	
	private String getParticipantRepoId(RMDModel rmdmodel) {
		String participantRepoId = "";
		if(genericParticipant != null){
			ICustomizeRMDParticipant customizeParticipantRMDInfo = genericParticipant.getGenericParticipantRMDFactory().getCustomParticipantRMD();
			if(customizeParticipantRMDInfo!= null){
				participantRepoId = customizeParticipantRMDInfo.getParticipantRepositoryId(rmdmodel);
			}
		}
		return participantRepoId;
	}
		
	private static Map<String,String> buildConfigMap(List<? extends ConfigItem> configList) {
		Map<String,String> configMap = new HashMap<String, String>();
		for(ConfigItem configitem :configList){
			configMap.put(configitem.getName(), configitem.getVal());
		}
		return configMap;		
	}

	private String getTeamForgeRepoId(RMDModel rmdmodel) {
		String trackerId = getTeamForgeTrackerId(rmdmodel.getTeamforgeTracker());
		String projectId = rmdmodel.getTeamforgeProjectId();
		String teamForgeRepositoryId = String.format("%s-%s-%s", projectId,trackerId,rmdmodel.getTeamForgeMappingType());
		if(rmdmodel.getTeamForgeMappingType().equalsIgnoreCase("tracker")){
			teamForgeRepositoryId = trackerId;
		} else if(rmdmodel.getTeamForgeMappingType().equalsIgnoreCase(PLANNINGFOLDERS)){
			teamForgeRepositoryId = String.format("%s-%s", projectId,PLANNINGFOLDERS);
		} else if (rmdmodel.getTeamForgeMappingType().equalsIgnoreCase("MetaData")){
			teamForgeRepositoryId = String.format("%s-%s", trackerId,rmdmodel.getTeamForgeMappingType());
		}
		return teamForgeRepositoryId;
	}
	
	private String getTeamForgeTrackerId(String teamForgeTrackerInfo){
		if (teamForgeTrackerInfo.contains("|")) { 
			String[] trackerInfo = StringUtils.split(teamForgeTrackerInfo, "|");
			if (trackerInfo.length > 0) {
				return trackerInfo[1];
			}		
		}
		return teamForgeTrackerInfo;
	}
	
	private void mergeRepositoryMappingDirection(RepositoryMappingDirection repoMappingDirection, FieldMapping fieldMapping) {
		if(fieldMapping != null){
			repoMappingDirection.setActiveFieldMapping(fieldMapping);
			repoMappingDirection.merge();
		}
	}
	
	
	protected ExternalApp getExternalApp(Landscape landscape, String projectId, String linkId, Connection connection) throws RemoteException{
		if(linkId != null){
			List<ExternalApp> externalApps = ExternalApp.findExternalAppsByLinkIdEquals(linkId).getResultList();
			if (externalApps.isEmpty()) {
				return ExternalApp.createNewExternalApp(linkId, connection);
			}
			return externalApps.get(0);
		}else{
			ProjectDO project = TeamForgeMetadataHelper.getTFProjectDetails(connection, projectId);
			ExternalApp externalApp = new ExternalApp();
			externalApp.setLandscape(landscape);
			externalApp.setProjectPath(project.getPath());
			externalApp.setProjectTitle(project.getTitle());
			externalApp.persist();
			return externalApp;
		}		
	}
	
	
	private RepositoryMapping getRespositoryMapping(ExternalApp externalApp, String teamForgeId, String participantID){
		List<RepositoryMapping> repositoryMappingList = RepositoryMapping.findRepositoryMappingsByExternalAppAndParticipantRepositoryIdAndTeamForgeRepositoryId(externalApp,participantID,teamForgeId).getResultList();
		if(repositoryMappingList.isEmpty()){
			RepositoryMapping repositoryMapping = new RepositoryMapping();
			repositoryMapping.setExternalApp(externalApp);
			repositoryMapping.setParticipantRepositoryId(participantID);
			repositoryMapping.setTeamForgeRepositoryId(teamForgeId);
			repositoryMapping.setDescription(teamForgeId+"/"+participantID);
			repositoryMapping.persist();
			return repositoryMapping;
		}
		return repositoryMappingList.get(0);
	}
	
	
	private RepositoryMappingDirection getRepositoryMappingDirection(Directions direction, RepositoryMapping repositoryMapping, ConflictResolutionPolicy conflictPolicy){
		List<RepositoryMappingDirection> repositoryMappingDirectionList = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, direction).getResultList();
		if(repositoryMappingDirectionList.isEmpty()){
			RepositoryMappingDirection repositoryMappingDirection = new RepositoryMappingDirection();
			repositoryMappingDirection.setConflictResolutionPolicy(conflictPolicy);
			repositoryMappingDirection.setDirection(direction);
			repositoryMappingDirection.setRepositoryMapping(repositoryMapping);
			repositoryMappingDirection.setLastSourceArtifactId(StringUtils.EMPTY);
			repositoryMappingDirection.setLastSourceArtifactVersion("0"); // setting default version
			repositoryMappingDirection.setLastSourceArtifactModificationDate(new Timestamp(0)); // setting default timestamp
			repositoryMappingDirection.persist();
			return repositoryMappingDirection;
		}
		return repositoryMappingDirectionList.get(0);
		
	}
	
	private FieldMapping getFieldMapping(String templateName, Directions direction,RepositoryMappingDirection repoMappingDirection){
		Landscape landscape = ControllerHelper.findLandscape();
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplate = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndNameAndDirection(landscape, templateName, direction).getSingleResult();
		List<FieldMapping> fieldMappingList = FieldMapping.findFieldMappingsByNameAndParentAndScope(templateName, repoMappingDirection, FieldMappingScope.REPOSITORY_MAPPING_DIRECTION).getResultList();
		if(fieldMappingLandscapeTemplate != null && fieldMappingList.isEmpty()){
			FieldMapping fieldMapping = new FieldMapping();
			fieldMapping.setKind(fieldMappingLandscapeTemplate.getKind());
			fieldMapping.setName(fieldMappingLandscapeTemplate.getName());
			fieldMapping.setParent(repoMappingDirection);
			fieldMapping.setRules(cloneFieldMappingRules(fieldMappingLandscapeTemplate.getRules()));
			fieldMapping.setScope(FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
			fieldMapping.setValueMaps(cloneFieldMappingValueMap(fieldMappingLandscapeTemplate.getValueMaps()));
			fieldMapping.persist();
			return fieldMapping;
		}
		return fieldMappingList.get(0);
	}
	
	private List<FieldMappingRule> cloneFieldMappingRules(List<FieldMappingRule> templateRules){
		List<FieldMappingRule> newFieldMappingRule = new ArrayList<FieldMappingRule>();
		for(FieldMappingRule templateRule:templateRules){
			FieldMappingRule newRule = new FieldMappingRule();
			newRule.setCondition(templateRule.getCondition());
			newRule.setDescription(templateRule.getDescription());
			newRule.setName(templateRule.getName());
			newRule.setSource(templateRule.getSource());
			newRule.setSourceIsTopLevelAttribute(templateRule.isSourceIsTopLevelAttribute());
			newRule.setTarget(templateRule.getTarget());
			newRule.setTargetIsTopLevelAttribute(templateRule.isTargetIsTopLevelAttribute());
			newRule.setType(templateRule.getType());
			newRule.setValueMapName(templateRule.getValueMapName());
			newRule.setXmlContent(templateRule.getXmlContent());
			newFieldMappingRule.add(newRule);
		}
		return newFieldMappingRule;
	}
	
	private List<FieldMappingValueMap> cloneFieldMappingValueMap(List<FieldMappingValueMap> templateValueMaps){
		List<FieldMappingValueMap> newFieldMappingValueMap = new ArrayList<FieldMappingValueMap>();
		for(FieldMappingValueMap templateMap:templateValueMaps){
			FieldMappingValueMap newValueMap = new FieldMappingValueMap();
			newValueMap.setName(templateMap.getName());
			newValueMap.setDefaultValue(templateMap.getDefaultValue());
			newValueMap.setHasDefault(templateMap.isHasDefault());
			newValueMap.setEntries(cloneFieldMappingValueMapEntry(templateMap.getEntries()));			
			newFieldMappingValueMap.add(newValueMap);
		}
		return newFieldMappingValueMap;
	}
	
	private List<FieldMappingValueMapEntry> cloneFieldMappingValueMapEntry(List<FieldMappingValueMapEntry> templateValueMapEntries){
		List<FieldMappingValueMapEntry> newFieldMappingValueMapEntries = new ArrayList<FieldMappingValueMapEntry>();
		for(FieldMappingValueMapEntry templateMapEntry:templateValueMapEntries){
			FieldMappingValueMapEntry newValueMap = new FieldMappingValueMapEntry();
			newValueMap.setSource(templateMapEntry.getSource());
			newValueMap.setTarget(templateMapEntry.getTarget());			
			newFieldMappingValueMapEntries.add(newValueMap);
		}
		return newFieldMappingValueMapEntries;
	}
	
	private void validateRepositoryMapping(ExternalApp ea, RepositoryMapping rm) {
		if (!ea.getId().equals(rm.getExternalApp().getId()))
			throw new BadRequestException("wrong external app.");
	}
}
