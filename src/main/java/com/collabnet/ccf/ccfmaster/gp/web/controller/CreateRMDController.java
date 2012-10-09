package com.collabnet.ccf.ccfmaster.gp.web.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TFSMetadataHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeMetadataHelper;

import flexjson.JSONSerializer;

@RequestMapping("/admin/**")
@Controller
public class CreateRMDController {
	
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
//		rmdmodel.setParticipantDomainNames(new String[]{"ccf22","DefaultCollection"});
		rmdmodel.setParticipantMappingTypes(new String[]{"WorkItem","Task","Bug"});
		return UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS;
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_SAVE, method=RequestMethod.POST)
	public void saveRMD(@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		System.out.println(rmdmodel.toString());
		
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
	
	private void buildTFSMetadatahelper(){
		Landscape landscape = ControllerHelper.findLandscape();
		ParticipantConfig urlConfig = ParticipantConfig.findParticipantConfigsByParticipantAndName(landscape.getParticipant(),"ccf.participant.tfs.url").getSingleResult();
		LandscapeConfig userNameConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,"ccf.landscape.tfs.username").getSingleResult();
		LandscapeConfig passwordConfig = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,"ccf.landscape.tfs.password").getSingleResult();
		tfsMetadataHelper.setUrl(urlConfig.getVal());
		tfsMetadataHelper.setUserName(userNameConfig.getVal());
		tfsMetadataHelper.setPassword(passwordConfig.getVal());
	}

}
