package com.collabnet.ccf.ccfmaster.controller.web;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.ccf.ccfmaster.web.model.RepositoryMappingsModel;
import com.collabnet.teamforge.api.Connection;


@RequestMapping("/admin/**")
@Controller
public class LandscapeRepositoryMappingsController {


	private static final String METADATA = " : Metadata";
	private static final String FOLDER_CLOSED_GIF = "folder_closed.gif";
	@Autowired 
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	private static final String RMDID = "rmdid";
	private static final Logger log = LoggerFactory.getLogger(LandscapeRepositoryMappingsController.class);
	private static final String FORWARD = "forward";
	
	/**
	* Controller method to display repository mapping directions for TF to Participant
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART, method = RequestMethod.GET)
    public String displayrepositorymappingtftopart(Model model, HttpServletRequest request) {
		log.info("in displayrepositorymappingtftopart");
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateRepositoryMappingsModel(landscape,model,Directions.FORWARD,request);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		  
    }
    
	
	/**
	* Controller method to resume synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_RESUMESYNCHRONIZATION, method = RequestMethod.POST)
    public String  resumeSynchronization(@ModelAttribute("repositoryMappingsModel") RepositoryMappingsModel repositoryMappingsModel, Model model, HttpServletRequest request) {
		 log.info("in resumeSynchronization");
		 Landscape landscape=ControllerHelper.findLandscape(model);
		 RequestContext ctx = new RequestContext(request);
		 String[] items = request.getParameterValues(RMDID);
		 String paramdirection=request.getParameter(ControllerConstants.DIRECTION);
		 Directions directions;
			if(paramdirection.equals(FORWARD)){
				 directions=Directions.FORWARD;
				}
				else{
				 directions=Directions.REVERSE;
				}
		 String rmId=null;
		 RepositoryMapping repositoryMapping=null;
		 RepositoryMappingDirection repositoryMappingDirection=null;
		 try{
			 for (int i = 0; i < items.length; i++) {
				 rmId = items[i];
				 repositoryMapping= RepositoryMapping.findRepositoryMapping(new Long(rmId));
				 repositoryMappingDirection=RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, directions).getSingleResult();
				 repositoryMappingDirection.setStatus(RepositoryMappingDirectionStatus.RUNNING);
				 repositoryMappingDirection.merge();
			 }
		 	model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.RMDRESUMESYNCSUCCESSMESSAGE));
		 }
		 catch(Exception exception){
			 model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.RMDRESUMESYNCFAILUREMESSAGE)+ exception.getMessage());
		 }
		 populateRepositoryMappingsModel(landscape,model,directions,request);
		 if(paramdirection.equals(FORWARD)){
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		 }
		 else{
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		 }
    }
	
	 @InitBinder
	 public void myInitBinder(WebDataBinder binder){
	    //do not bind these fields 
	    binder.setDisallowedFields(new String[]{"repositoryMappingDirection",});
	  }
	
	/**
	* Controller method to pause synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_PAUSESYNCHRONIZATION, method = RequestMethod.POST)
    public String  pauseSynchronization(@ModelAttribute("repositoryMappingsModel") RepositoryMappingsModel repositoryMappingsModel, Model model, HttpServletRequest request) {
		log.info("in pauseSynchronization");
		 Landscape landscape=ControllerHelper.findLandscape(model);
		 RequestContext ctx = new RequestContext(request);
		 String[] items = request.getParameterValues(RMDID);
		 String rmId=null;
		 RepositoryMapping repositoryMapping=null;
		 RepositoryMappingDirection repositoryMappingDirection=null;
		 String paramdirection=request.getParameter(ControllerConstants.DIRECTION);
		 Directions directions;
			if(paramdirection.equals(FORWARD)){
				 directions=Directions.FORWARD;
				}
				else{
				 directions=Directions.REVERSE;
				}
		 try{
		 for (int i = 0; i < items.length; i++) {
			  rmId = items[i];
			  repositoryMapping= RepositoryMapping.findRepositoryMapping(new Long(rmId));
			  repositoryMappingDirection=RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, directions).getSingleResult();
			  repositoryMappingDirection.setStatus(RepositoryMappingDirectionStatus.PAUSED);
			  repositoryMappingDirection.merge();
			 
		 }
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.RMDPAUSESYNCSUCCESSMESSAGE));
		 } 
		 catch(Exception exception){
			 model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.RMDPAUSESYNCFAILUREMESSAGE)+ exception.getMessage());
		 }
		 populateRepositoryMappingsModel(landscape,model,directions,request);
		 if(paramdirection.equals(FORWARD)){
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		 }
		 else{
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		 }
    }
	
	/**
	* Controller method to delete synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_DELETESYNCHRONIZATION, method = RequestMethod.POST)
    public String  deleteSynchronization(@ModelAttribute("repositoryMappingsModel") RepositoryMappingsModel repositoryMappingsModel, Model model, HttpServletRequest request) {
		log.info("in delteSynchronization");
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		String[] items = request.getParameterValues(RMDID);
		String paramdirection=request.getParameter(ControllerConstants.DIRECTION);
		String rmId=null;
		RepositoryMapping repositoryMapping=null;
		RepositoryMappingDirection repositoryMappingDirection=null;
		Directions directions;
			if(paramdirection.equals(FORWARD)){
				 directions=Directions.FORWARD;
				}
				else{
				 directions=Directions.REVERSE;
				}
		try{
		for (int i = 0; i < items.length; i++) {
			  rmId = items[i];
			  repositoryMapping= RepositoryMapping.findRepositoryMapping(new Long(rmId));
			  repositoryMappingDirection=RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, directions).getSingleResult();
			  repositoryMappingDirection.remove();
		 }
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.RMDDELETESUCCESSMESSAGE));
		 }
		 catch(Exception exception){
			 model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.RMDDELETEFAILUREMESSAGE)+ exception.getMessage());
		 }
		 populateRepositoryMappingsModel(landscape,model,directions,request);
		 if(paramdirection.equals(FORWARD)){
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		 }
		 else{
			 return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		 }
    }
	
	/**
	* Controller method to display repository mapping directions for Participant to TF
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF, method = RequestMethod.GET)
    public String displayrepositorymappingparttotf(Model model, HttpServletRequest request) {
		log.info("in displayrepositorymappingparttotf");
		Landscape landscape=ControllerHelper.findLandscape(model);
		populateRepositoryMappingsModel(landscape,model,Directions.REVERSE,request);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
	}
	
	/** Helper method to populate Repository Mapping Direction Model   
	 * 
	 */	
	public void populateRepositoryMappingsModel(Landscape landscape,Model model, Directions directions,HttpServletRequest request){
		RepositoryMappingsModel  repositoryMappingsModel=new RepositoryMappingsModel();
		List<RepositoryMappingDirection> repositoryMappingDirectionList=RepositoryMappingDirection.findRepositoryMappingDirectionsByDirection(directions).getResultList();
		List hospitalCountList=new ArrayList();
		List repositoryIdList=new ArrayList();
		List repositoryIconList=new ArrayList();
		List repositorydataList=new ArrayList();
		String repositoryDetail=null;
		
		for(int i=0;i<repositoryMappingDirectionList.size();i++){
			hospitalCountList.add(HospitalEntry.countHospitalEntrysByRepositoryMappingDirection(repositoryMappingDirectionList.get(i)));
			if(directions.name().equals("FORWARD")){
								
				repositoryDetail=getRepositoryData(repositoryMappingDirectionList.get(i).getSourceRepositoryId(),model,request);
			}
			else{
				repositoryDetail=getRepositoryData(repositoryMappingDirectionList.get(i).getTargetRepositoryId(),model,request);
			}
			if(repositoryDetail!=null){
		    String[] repositoryData = Pattern.compile("-").split(repositoryDetail);
		    repositoryIconList.add(repositoryData[0]);
		    repositorydataList.add(repositoryData[1]);
		    repositoryIdList.add(repositoryData[2]);
			}
			else{
				repositoryIconList.add("null");
				repositorydataList.add(repositoryMappingDirectionList.get(i).getSourceRepositoryId());
				repositoryIdList.add(repositoryMappingDirectionList.get(i).getSourceRepositoryId());
			}
		
		}
		repositoryMappingsModel.setRepositoryMappingDirection(repositoryMappingDirectionList);
		repositoryMappingsModel.setHospitalCount(hospitalCountList);
		repositoryMappingsModel.setRepositoryIcon(repositoryIconList);
		repositoryMappingsModel.setRepositoryId(repositoryIdList);
		repositoryMappingsModel.setRepositoryData(repositorydataList);
		repositoryMappingsModel.setTfUrl(ccfRuntimePropertyHolder.getTfUrl());
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		model.addAttribute("repositoryMappingsModel",repositoryMappingsModel);
	
	}
	
	
	public String getRepositoryData(String sourceRepositoryId,Model model, HttpServletRequest request){
		RequestContext ctx = new RequestContext(request);
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TFUserDetails tfUser=(TFUserDetails)user;
		String repositoryDetail=null;
		try{ 
			Connection teamforgeConnection=tfUser.getConnection();
			if(RepositoryConnections.isTrackerRepository(sourceRepositoryId)){
				repositoryDetail=teamforgeConnection.getTrackerClient().getTrackerData(sourceRepositoryId).getIcon()+"-"+teamforgeConnection.getTrackerClient().getTrackerData(sourceRepositoryId).getTitle()+"-"+sourceRepositoryId;
			}
			else if (RepositoryConnections.isPlanningFolderRepository(sourceRepositoryId)) {
				String sourceProjectId=RepositoryConnections.extractProjectFromRepositoryId(sourceRepositoryId);
				repositoryDetail=FOLDER_CLOSED_GIF+"-"+teamforgeConnection.getTeamForgeClient().getProjectData(sourceProjectId).getTitle()+"-"+sourceProjectId;
			}
			else if (RepositoryConnections.isTrackerMetaDataRepository(sourceRepositoryId)) {
				String sourceProjectId=RepositoryConnections.extractTrackerFromMetaDataRepositoryId(sourceRepositoryId);
				repositoryDetail=teamforgeConnection.getTrackerClient().getTrackerData(sourceProjectId).getIcon()+"-"+teamforgeConnection.getTrackerClient().getTrackerData(sourceProjectId).getTitle()+METADATA+"-"+sourceProjectId;
			
			}
		}
		catch(RemoteException remoteException){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.TEAMFORGE)+ remoteException.getMessage());
		//	trackerDetail=trackerId;
		}
		return repositoryDetail;
		}
	
	
}
