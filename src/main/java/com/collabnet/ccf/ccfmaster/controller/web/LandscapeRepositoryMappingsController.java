package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;
import com.collabnet.ccf.ccfmaster.web.model.RepositoryMappingsModel;


@RequestMapping("/admin/**")
@Controller
public class LandscapeRepositoryMappingsController extends AbstractLandscapeController {


	@Autowired 
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	private static final String RMDID = "rmdid";
	private static final String FORWARD = "forward";
	
	/**
	* Controller method to display repository mapping directions for TF to Participant
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART, method = RequestMethod.GET)
    public String displayrepositorymappingtftopart(
    		@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
    		Model model, HttpServletRequest request,HttpSession session) {
		doList(Directions.FORWARD, page, size, model,session,request);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
	 }
    
	
	/**
	* Controller method to resume synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_RESUMESYNCHRONIZATION, method = RequestMethod.POST)
    public String  resumeSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request,HttpSession session) {
		 setStatusForRMDs(RepositoryMappingDirectionStatus.RUNNING, paramdirection, model, request);
		 model.asMap().clear();
		 Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		 populatePageSizetoModel(directions,model, session);
		 if(paramdirection.equals(FORWARD)){
			 return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		 }
		 else{
			 return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		 }
    }


	public static void setStatusForRMDs(RepositoryMappingDirectionStatus status, String paramdirection, Model model,
			HttpServletRequest request) {
		 String[] items = request.getParameterValues(RMDID);
		 Directions directions = FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		 try{
			setStatusForRMDs(items, directions, status);
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCSUCCESSMESSAGE : ControllerConstants.RMDRESUMESYNCSUCCESSMESSAGE;
			FlashMap.setSuccessMessage(messageCode);
		 }
		 catch(Exception exception){
			String messageCode = (status == RepositoryMappingDirectionStatus.PAUSED) ? ControllerConstants.RMDPAUSESYNCFAILUREMESSAGE : ControllerConstants.RMDRESUMESYNCFAILUREMESSAGE;
			FlashMap.setErrorMessage(messageCode, exception.getMessage());
		 }
	}
	
	
	private static List<RepositoryMappingDirection> setStatusForRMDs(String[] rmIds, Directions direction, RepositoryMappingDirectionStatus status) {
		if (rmIds == null) rmIds = new String[0];
		ArrayList<RepositoryMappingDirection> ret = new ArrayList<RepositoryMappingDirection>(rmIds.length);
		for (String rmId : rmIds) {
			RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(Long.valueOf(rmId));
			RepositoryMappingDirection repositoryMappingDirection = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, direction).getSingleResult();
			repositoryMappingDirection.setStatus(status);
			ret.add(repositoryMappingDirection.merge());
		}
		return ret;
	}
	
	@InitBinder
	public void myInitBinder(WebDataBinder binder) {
		// do not bind these fields
		binder.setDisallowedFields(new String[] { "repositoryMappingDirection", });
	}
	
	/**
	* Controller method to pause synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_PAUSESYNCHRONIZATION, method = RequestMethod.POST)
    public String  pauseSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request,HttpSession session) {
		setStatusForRMDs(RepositoryMappingDirectionStatus.PAUSED, paramdirection, model, request);
		model.asMap().clear();
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		populatePageSizetoModel(directions,model, session);
		if (paramdirection.equals(FORWARD)) {
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		} else {
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		}
    }
	
	/**
	* Controller method to delete synchronization 
	* 
	*/  
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_DELETESYNCHRONIZATION, method = RequestMethod.POST)
    public String  deleteSynchronization(@RequestParam(ControllerConstants.DIRECTION) String paramdirection, Model model, HttpServletRequest request,HttpSession session) {
		String[] items = request.getParameterValues(RMDID);
		if (items == null) 
			items = new String[0];
		Directions directions = FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		try{
			for (String rmId : items) {
				RepositoryMapping repositoryMapping = RepositoryMapping.findRepositoryMapping(Long.valueOf(rmId));
				RepositoryMappingDirection repositoryMappingDirection = RepositoryMappingDirection.findRepositoryMappingDirectionsByRepositoryMappingAndDirection(repositoryMapping, directions).getSingleResult();
				repositoryMappingDirection.remove();
			}
			FlashMap.setSuccessMessage(ControllerConstants.RMDDELETESUCCESSMESSAGE);
		} catch(Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.RMDDELETEFAILUREMESSAGE, exception.getMessage());
		}
		 model.asMap().clear();
		 populatePageSizetoModel(directions,model, session);
		 if(paramdirection.equals(FORWARD)){
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		} else {
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		}
    }
	
	/**
	* Controller method to display repository mapping directions for Participant to TF
	* 
	*/  
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF, method = RequestMethod.GET)
    public String displayrepositorymappingparttotf(
    		@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
    		Model model, HttpServletRequest request,HttpSession session) {
		doList(Directions.REVERSE, page, size, model,session,request);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
	}


	/**
	 * @param direction
	 * @param page
	 * @param size
	 * @param model
	 */
	private void doList(Directions direction, Integer page, Integer size, Model model,HttpSession session,HttpServletRequest request) {
		List<RepositoryMappingDirection> rmds =paginate(
				RepositoryMappingDirection.findRepositoryMappingDirectionsByDirection(direction),
				RepositoryMappingDirection.countRepositoryMappingDirectionsByDirection(direction),
				page, size, model)
			.getResultList();
		session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
		session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
		populateModel(model, rmds);
	}


	/**
	 * Helper method to populate Repository Mapping Direction Model
	 * @param model
	 * @param rmds
	 */
	private void populateModel(Model model, List<RepositoryMappingDirection> rmds) {
		String tfUrl = ccfRuntimePropertyHolder.getTfUrl();
		List<RepositoryMappingsModel> rmmList = makeRepositoryMappingsModel(rmds, tfUrl);
		Landscape landscape=ControllerHelper.findLandscape(model);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		model.addAttribute("repositoryMappingsModel", rmmList);
	}
	
	public static List<RepositoryMappingsModel> makeRepositoryMappingsModel(
			List<RepositoryMappingDirection> repositoryMappingDirectionList,
			String tfUrl) {
		List<RepositoryMappingsModel> rmmList = new ArrayList<RepositoryMappingsModel>(repositoryMappingDirectionList.size());
		for (RepositoryMappingDirection rmd: repositoryMappingDirectionList){
			RepositoryMappingsModel rmm = new RepositoryMappingsModel();
			rmm.setRepositoryMappingDirection(rmd);
			rmm.setTfUrl(tfUrl);
			rmm.setArtifatTFUrl(tfUrl);
			rmm.setHospitalCount(HospitalEntry.countHospitalEntrysByRepositoryMappingDirection(rmd));
			rmm.setIdentityMappingCount(IdentityMapping.countIdentityMappingsByRepositoryMapping(rmd.getRepositoryMapping()));
			Directions dir = rmd.getDirection();
			String tfRepoId = (dir == Directions.FORWARD) ? rmd.getSourceRepositoryId() : rmd.getTargetRepositoryId();
			RepositoryDetail repositoryDetail = RepositoryConnections.detailsForRepository(tfRepoId);
			ArtifactDetail artifactDetail=RepositoryConnections.detailsForArtifact(rmd.getLastSourceArtifactId());
			rmm.setArtifactDetail(artifactDetail);
			rmm.setRepositoryDetail(repositoryDetail);
			rmmList.add(rmm);
		}
		return rmmList;
	}	
	
	
	public static void populatePageSizetoModel(Directions directions, Model model,
			HttpSession session) {
		Integer size = (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE: (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION);
		float nrOfPages = (float)RepositoryMappingDirection.countRepositoryMappingDirectionsByDirection(directions) / size.intValue();
		Integer page = (Integer) session.getAttribute(ControllerConstants.PAGE_IN_SESSION);
		// if page in session is null.get the default value of page
		if (page == null) {
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (page <= 0) {
			// in case if current page value is less than or equal to zero get
			// default value of page (on deleting the last record of the first
			// page)
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (Math.ceil(nrOfPages) != 0.0 && page >= Math.ceil(nrOfPages)) {
			// in case if current page value is greater than no of page (on
			// deleting last record from the current page.traverse to the
			// previous page)
			page = (int) Math.ceil(nrOfPages);
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
	}
	
}
