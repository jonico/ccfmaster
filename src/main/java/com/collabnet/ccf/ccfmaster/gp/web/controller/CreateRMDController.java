package com.collabnet.ccf.ccfmaster.gp.web.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
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
import com.collabnet.ccf.ccfmaster.web.helper.TFSMetadataHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeConnectionHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeMetadataHelper;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.ProjectDO;

import flexjson.JSONSerializer;

@RequestMapping("/admin/**")
@Controller
public class CreateRMDController {
	
	@Autowired
	private CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	private TFSMetadataHelper tfsMetadataHelper = new TFSMetadataHelper();
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE, method=RequestMethod.POST)
	public String intializeRMDSettings(Model model, HttpServletRequest request){
		model.addAttribute("rmdModel", new RMDModel());
		return UIPathConstants.RMD_CONFIGURE;		
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE_DIRECTION, method=RequestMethod.POST)
	public String intializeDirectionSettings(Model model,@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		return UIPathConstants.RMD_CONFIGURE_DIRECTION;
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE_TFSETTINGS, method=RequestMethod.POST)
	public String intializeTFSettings(Model model,@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		return UIPathConstants.RMD_CONFIGURE_TFSETTINGS;
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS, method=RequestMethod.POST)
	public String intializeParticipantSettings(@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		buildTFSMetadatahelper();
		rmdmodel.setParticipantDomainNames(tfsMetadataHelper.getTFSCollectionList().toArray(new String[]{}));
		rmdmodel.setParticipantMappingTypes(new String[]{"User Story","Task","Bug"});
		return UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS;
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_SAVE, method=RequestMethod.POST)
	public void saveRMD(@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		String direction = rmdmodel.getDirection();
		ConflictResolutionPolicy forwardConflictPolicy = ConflictResolutionPolicy.valueOf(rmdmodel.getForwardConfilictPolicies());
		ConflictResolutionPolicy reverseConflictPolicy = ConflictResolutionPolicy.valueOf(rmdmodel.getReversedConfilictPolicies());
		if("FORWARD".equalsIgnoreCase(direction)){
			buildRepositoryMappingDir(rmdmodel, forwardConflictPolicy, Directions.FORWARD,rmdmodel.getForwardFieldMappingTemplateName());
		}else if("REVERSE".equalsIgnoreCase(direction)){
			buildRepositoryMappingDir(rmdmodel, reverseConflictPolicy, Directions.REVERSE,rmdmodel.getReverseFieldMappingTemplateName());	
		}else{
			buildRepositoryMappingDir(rmdmodel, forwardConflictPolicy, Directions.FORWARD,rmdmodel.getForwardFieldMappingTemplateName());
			buildRepositoryMappingDir(rmdmodel, reverseConflictPolicy, Directions.REVERSE,rmdmodel.getReverseFieldMappingTemplateName());
		}
		
	}
		
	@RequestMapping(value="/admin/teamForge/trackerList",method= RequestMethod.POST)
	public @ResponseBody String getAllTrackerInfo(@RequestParam String projectId){
		List<Map<String, String>> teamForgeTracker = new ArrayList<Map<String,String>>();
		try {
			teamForgeTracker = TeamForgeMetadataHelper.getAllTrackersOfProject(projectId);
		} catch (RemoteException e) { }//ignore the remote exception
		return new JSONSerializer().serialize(teamForgeTracker);		
	}
	
	@RequestMapping(value="/admin/participant/projectList",method= RequestMethod.POST)
	public @ResponseBody String getAllProjectNames(@RequestParam String collectionName){//TODO: Need to move this method
		List<String> participantProjectList = new ArrayList<String>();
		buildTFSMetadatahelper();
		participantProjectList = tfsMetadataHelper.getTFSProjectList(collectionName);
		return new JSONSerializer().serialize(participantProjectList);		
	}
	
	@RequestMapping(value="/admin/participant/WorkItem",method= RequestMethod.POST)
	public @ResponseBody String getWorkItemList(@RequestParam String collectionName,@RequestParam String projectName){//TODO: Need to move this method
		List<String> workItemList = new ArrayList<String>();
		buildTFSMetadatahelper();
		workItemList = tfsMetadataHelper.getTFSWorkItemList(collectionName, projectName);
		return new JSONSerializer().serialize(workItemList);		
	}
		
	@ModelAttribute(value="conflictPolicies")
	public String[] getConfilictPolicies(){
		ConflictResolutionPolicy[]  conflictValues = ConflictResolutionPolicy.values();
		String[] conflictPolicyArray = new String[conflictValues.length];
		for(int i=0;i<conflictValues.length;i++){
			conflictPolicyArray[i]= conflictValues[i].toString();
		}
		return conflictPolicyArray;
	}	
	
	@ModelAttribute(value="teamForgeProjects")
	public Map<String, String> getAllTeamForgeProjects(){
		Map<String,String> teamForgeProject = new HashMap<String,String>();
		try {
			teamForgeProject =  TeamForgeMetadataHelper.getAllTeamForgeProjects();
		} catch (RemoteException e) { } //ignore the remote exception
		return teamForgeProject;
	}
	
	@ModelAttribute(value="directions")
	public String[] getDirectionList(){
		return new String[]{"FORWARD","REVERSE","BOTH"};
	}
	
	@ModelAttribute(value="teamForgeMappingType")	
	public String[] getTeamForgeMappingType(){
		return new String[] {"PlanningFolder","Tracker","Metadata"};
	}
	
	@ModelAttribute(value="forwardFieldMappingTemplateNames")
	public List<String> getForwardFieldMappingTemplateNames(){
		return getFieldMappingTemplateNames(Directions.FORWARD);
	}
	
	@ModelAttribute(value="reverseFieldMappingTemplateNames")
	public List<String> getReverseFieldMappingTemplateNames(){
		return getFieldMappingTemplateNames(Directions.REVERSE);
	}

	private void buildRepositoryMappingDir(RMDModel rmdmodel,ConflictResolutionPolicy conflictPolicy,Directions directions,String templateName) {
		try {
			String teamForgeRepositoryId = getTeamForgeRepoId(rmdmodel);
			String participantRepositoryId = String.format("%s-%s-%s", rmdmodel.getParticipantDomainName(),rmdmodel.getParticipantProjectId(),rmdmodel.getParticipantMappingType());
			ExternalApp externalApp = getExternalApp(ccfRuntimePropertyHolder.getCcfBaseUrl(), rmdmodel.getTeamforgeProjectId());
			RepositoryMapping repositoryMapping = getRespositoryMapping(externalApp, teamForgeRepositoryId, participantRepositoryId);
			RepositoryMappingDirection repoMappingDirection = getRepositoryMappingDirection(directions, repositoryMapping, conflictPolicy);
			//TODO: need to provide the selected fieldMappingtemplate name take the tempateName from rmdModel
			FieldMapping fieldMapping = getFieldMapping(templateName,directions, repoMappingDirection);
			mergeRepositoryMappingDirection(repoMappingDirection, fieldMapping);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private String getTeamForgeRepoId(RMDModel rmdmodel) {
		String teamForgeRepositoryId = String.format("%s-%s-%s", rmdmodel.getTeamforgeProjectId(),rmdmodel.getTeamforgeTracker(),rmdmodel.getTeamForgeMappingType());
		if(rmdmodel.getTeamForgeMappingType().equalsIgnoreCase("tracker")){
			teamForgeRepositoryId = rmdmodel.getTeamforgeTracker();
		}
		if(rmdmodel.getTeamForgeMappingType().equalsIgnoreCase("planning folder")){
			teamForgeRepositoryId = String.format("%s-%s", rmdmodel.getTeamforgeProjectId(),rmdmodel.getTeamForgeMappingType());
		}
		return teamForgeRepositoryId;
	}
	
	private List<String> getFieldMappingTemplateNames(Directions direction) {
		List<String> fieldMappingTemplateNames = new ArrayList<String>();
		List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplates = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByDirection(direction).getResultList();
		for(FieldMappingLandscapeTemplate template: fieldMappingLandscapeTemplates){
			fieldMappingTemplateNames.add(template.getName());
		}
		return fieldMappingTemplateNames;
	}

	@Transactional
	private void mergeRepositoryMappingDirection(RepositoryMappingDirection repoMappingDirection, FieldMapping fieldMapping) {
		if(fieldMapping != null){
			repoMappingDirection.setActiveFieldMapping(fieldMapping);
			repoMappingDirection.merge();
		}
	}
	
	@Transactional
	private ExternalApp getExternalApp(String baseUrl,String projectId) throws RemoteException{
		Landscape landscape = ControllerHelper.findLandscape();
		Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
		String linkId = TeamForgeMetadataHelper.getTFLinkId(connection,landscape.getPlugId(),projectId);
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
	
	@Transactional
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
	
	@Transactional
	private RepositoryMappingDirection getRepositoryMappingDirection(Directions direction, RepositoryMapping repositoryMapping, ConflictResolutionPolicy conflictPolicy){
		List<RepositoryMappingDirection> repositoryMappingDirectionList = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, direction).getResultList();
		if(repositoryMappingDirectionList.isEmpty()){
			RepositoryMappingDirection repositoryMappingDirection = new RepositoryMappingDirection();
//			repositoryMappingDirection.setActiveFieldMapping(fieldMapping);
			repositoryMappingDirection.setConflictResolutionPolicy(conflictPolicy);
			repositoryMappingDirection.setDirection(direction);
			repositoryMappingDirection.setRepositoryMapping(repositoryMapping);
			repositoryMappingDirection.setLastSourceArtifactId(StringUtils.EMPTY);
			repositoryMappingDirection.setLastSourceArtifactVersion(StringUtils.EMPTY);
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
	
	private void buildTFSMetadatahelper(){
		Landscape landscape = ControllerHelper.findLandscape();
		ParticipantConfig urlConfig = ParticipantConfig.findParticipantConfigsByParticipantAndName(landscape.getParticipant(),"ccf.participant.tfs.url").getSingleResult();
		LandscapeConfig userNameConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,"ccf.landscape.tfs.username").getSingleResult();
		LandscapeConfig passwordConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,"ccf.landscape.tfs.password").getSingleResult();
		tfsMetadataHelper.setUrl(urlConfig.getVal());
		tfsMetadataHelper.setUserName(userNameConfig.getVal()); // TODO: currently username got appended with domain name; so split by "/" get the username later
		tfsMetadataHelper.setPassword(passwordConfig.getVal());
	}

}
