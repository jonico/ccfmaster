package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.List;
import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;



  

@RequestMapping("/landscapesettings/**")
@Controller
public class LandscapeRepositoryMappingsController {

	
	private static final Logger log = LoggerFactory.getLogger(LandscapeRepositoryMappingsController.class);
	
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART, method = RequestMethod.GET)
    public String displayrepositorymappingtftopart(Model model, HttpServletRequest request) {
		log.info("in displayrepositorymappingtftopart");
		Landscape landscape=ControllerHelper.findLandscape(model);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGTFTOPART;
		  
    }
    
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF, method = RequestMethod.GET)
    public String displayrepositorymappingparttotf(Model model, HttpServletRequest request) {
		log.info("in displayrepositorymappingparttotf");
		Landscape landscape=ControllerHelper.findLandscape(model);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYREPOSITORYMAPPINGPARTTOTF;
		  
    }
	
	
	
}
