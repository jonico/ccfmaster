package com.collabnet.ccf.ccfmaster.controller.web;


import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;


@RequestMapping("/admin/**")
@Controller
public class LandscapeStatusController {

	private static final Logger log = LoggerFactory.getLogger(LandscapeStatusController.class);
	
	/**
	* Controller method to display TF to participant ccfcorestatus
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS)
    public String displaystatustftopart(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		populateCCFCoreStatusModel(landscape,model,forwardDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;
		  
    }  
    
	/**
	* Controller method to start TF ccfcore status 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STARTCCFCORESTATUS, method = RequestMethod.POST)
    public String  startCCFCoreStatus(@ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel, Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		mergeCCFCoreStatus(ccfCoreStatusModel,request,forwardDirection,ExecutedCommand.START);
		populateCCFCoreStatusModel(landscape,model,forwardDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;
    }
	
	/**
	* Controller method to stop TF ccfcore status 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STOPCCFCORESTATUS, method = RequestMethod.POST)
    public String  stopCCFCoreStatus(@ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel, Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		mergeCCFCoreStatus(ccfCoreStatusModel,request,forwardDirection,ExecutedCommand.STOP);
		populateCCFCoreStatusModel(landscape,model,forwardDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;
    }
	
	
	
	
	/**
	* Controller method to display participant to TF ccfcorestatus
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS)
    public String displaystatusparttotf(Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		populateCCFCoreStatusModel(landscape,model,reverseDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
		  
    }

    
	/**
	* Controller method to start participant ccfcore status 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STARTPARTICIPANTCCFCORESTATUS, method = RequestMethod.POST)
    public String  startParticipantCCFCoreStatua(@ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel, Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		mergeCCFCoreStatus(ccfCoreStatusModel,request,reverseDirection,ExecutedCommand.START);
		populateCCFCoreStatusModel(landscape,model,reverseDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
    }
	
	/**
	* Controller method to stop participant ccfcore status 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STOPPARTICIPANTCCFCORESTATUS, method = RequestMethod.POST)
    public String  stopParticipantCCFCoreStatua(@ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel, Model model, HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		mergeCCFCoreStatus(ccfCoreStatusModel,request,reverseDirection,ExecutedCommand.STOP);
		populateCCFCoreStatusModel(landscape,model,reverseDirection);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
    }
	
	
	/**
	* Help method to merge/save the ccfcore status 
	* 
	*/  
	@Transactional
	public  void mergeCCFCoreStatus(CcfCoreStatus ccfCoreStatusModel,HttpServletRequest request,Direction direction,ExecutedCommand eCommand){
		ccfCoreStatusModel.setExecutedCommand(eCommand);
		ccfCoreStatusModel.setDirection(direction);
		ccfCoreStatusModel.setCurrentStatus(ccfCoreStatusModel.getCurrentStatus());
		ccfCoreStatusModel.setId(Long.valueOf(request.getParameter(ControllerConstants.CCF_CORE_STAUS_ID)));
		ccfCoreStatusModel.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.CCF_CORE_STATUS_VERSION)));
		ccfCoreStatusModel.merge(); 
	}
	
	
	/** Helper method to populate direction config   
	 * 
	 */	
	public void populateCCFCoreStatusModel(Landscape landscape,Model model,Direction direction){
		CcfCoreStatus ccfCoreStatusModel=CcfCoreStatus.findCcfCoreStatusesByDirection(direction).getSingleResult();
		model.addAttribute("ccfCoreStatusModel", ccfCoreStatusModel);
		model.addAttribute("selectedLink", ControllerConstants.STATUS);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("ccfCoreStatusId",ccfCoreStatusModel.getId());
		model.addAttribute("ccfCoreStatusVersion",ccfCoreStatusModel.getVersion());
	}	 
	
}
