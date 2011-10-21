package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.ParticipantSettingsValidator;



@RequestMapping("/admin/**")
@Controller
public class LandscapeParticipantSettingsController extends AbstractLandscapeController{

	private static final String RESTART = "restart";
	private static final Logger log = LoggerFactory.getLogger(LandscapeParticipantSettingsController.class);
	LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();

	/**
	 * Controller method to display participant settings 
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS, method = RequestMethod.GET)
	public String displayParticipantSettings(Model model, HttpServletRequest request) {
		ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant participant=landscape.getParticipant();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
		landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
	}



	/**
	 * Controller method to save participant settings 
	 * 
	 */ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVEPARTICIPANTSETTINGS, method = RequestMethod.POST)
	@Transactional
	public String saveParticipantSettings(@ModelAttribute("qcsettingsmodel") @Valid ParticipantSettingsModel participantSettingsModel,BindingResult bindingResult,Model model, HttpServletRequest request) {
		//validate participantSettingsModel
		ParticipantSettingsValidator participantSettingsValidator=new ParticipantSettingsValidator();	
		participantSettingsValidator.validate(participantSettingsModel, bindingResult);
		if (bindingResult.hasErrors()) {
			landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
			Landscape landscape=ControllerHelper.findLandscape(model);
			Participant participant=landscape.getParticipant();
			landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
			return  UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
		} 
		else{
			try{  
				landscapeParticipantSettingsHelper.updateParticipantSettings(participantSettingsModel, model, request); 
				boolean hasRestart = Boolean.parseBoolean(request.getParameter(RESTART));
				if(hasRestart){
					LandscapeStatusController.restartCCFCoreStatus(model);
					FlashMap.setSuccessMessage(ControllerConstants.PARTICIPANT_RESTART_SUCCESS_MESSAGE);
				}
			}
			catch(Exception exception){
				log.debug(exception.getMessage());
				FlashMap.setErrorMessage(ControllerConstants.PARTICIPANT_SAVE_FAIL_MESSAGE, exception.getMessage());
			}
			model.asMap().clear();
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
		} 
	}


	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

}
