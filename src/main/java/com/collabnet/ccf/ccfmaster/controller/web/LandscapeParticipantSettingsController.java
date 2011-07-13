package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.ParticipantSettingsValidator;


  
@RequestMapping("/admin/**")
@Controller
public class LandscapeParticipantSettingsController {

		private static final Logger log = LoggerFactory.getLogger(LandscapeParticipantSettingsController.class);
	LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();
	
	/**
	* Controller method to display participant settings 
	* 
	*/ 
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS, method = RequestMethod.GET)
    public String displayparticipantsettings(Model model, HttpServletRequest request) {
		log.info("in displayqcsettings");
		Landscape landscape=ControllerHelper.findLandscape(model);
		ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
		Participant participant=landscape.getParticipant();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,landscape,participant,model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
	}
	
	
	
	/**
	* Controller method to save participant settings 
	* 
	*/ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVEPARTICIPANTSETTINGS, method = RequestMethod.POST)
	@Transactional
    public String saveparticipantsettings(@ModelAttribute("qcsettingsmodel") @Valid ParticipantSettingsModel participantSettingsModel,BindingResult bindingResult,Model model, HttpServletRequest request) {
		log.info("in saveqcsettings");
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant participant=landscape.getParticipant();
		ParticipantSettingsValidator participantSettingsValidator=new ParticipantSettingsValidator();	
		participantSettingsValidator.validate(participantSettingsModel, bindingResult);
		if (bindingResult.hasErrors()) {
			landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,landscape,participant,model);
			return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
        }  
		try{
			landscapeParticipantSettingsHelper.updateParticipantSettings(participantSettingsModel, model, request,
				ctx, landscape, participant); 
		}
		catch(Exception exception){
			model.addAttribute("connectionerror",ctx.getMessage("participantsavefailmessage")+ exception.getMessage());
		}
		
		Landscape updatedLandscape=ControllerHelper.findLandscape(model);
		Participant updatedParticipant=updatedLandscape.getParticipant();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,updatedLandscape,updatedParticipant,model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
		  
    }
	
	
	@ModelAttribute("timezones")
    public java.util.Collection<Timezone> populateTimezones() {
        return Arrays.asList(Timezone.class.getEnumConstants());
    }
	
}
