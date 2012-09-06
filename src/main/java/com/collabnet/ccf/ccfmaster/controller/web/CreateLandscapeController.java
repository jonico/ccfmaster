package com.collabnet.ccf.ccfmaster.controller.web;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.gp.model.GenericParticipant;
import com.collabnet.ccf.ccfmaster.gp.validator.DefaultGenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.gp.validator.GenericParticipantValidator;
import com.collabnet.ccf.ccfmaster.server.domain.Capabilities;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeConnectionHelper;
import com.collabnet.ccf.ccfmaster.web.model.LandscapeModel;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.LandscapeValidator;
import com.collabnet.teamforge.api.Connection;

@RequestMapping("/admin/**")
@Controller
public class CreateLandscapeController{

	private static final Logger log = LoggerFactory.getLogger(CreateLandscapeController.class);

	private final Capabilities capabilities = new Capabilities();
	
	private CreateLandscapeHelper createLandscapeHelper=new CreateLandscapeHelper();
	
	private LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();
	
	private static final String TF_URL_MODEL_ATTRIBUTE = "tfUrl";
	
	@Autowired
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	@Autowired
	public GenericParticipant genericParticipant;
	
	
	/**
	 * Controller method used to display create landscape wizard or landscape settings screen based on landscape count 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_CCFMASTER, method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		//If there is no landscape exists show create landscape wizard
		if (Landscape.countLandscapes() == 0) { 
			//	if there is no participants show create landscape wizard
			if(Participant.countParticipants()==0){
				Participant participant=new Participant();
				if(genericParticipant != null){
					model.addAttribute("genericParticipantName", genericParticipant.getName());
				}
				model.addAttribute("participant", participant);
				model.addAttribute("enums", capabilities.getParticipantSystemKinds().getSystemKind());
				return UIPathConstants.CREATELANDSCAPE_INDEX;
			}
			//If there is single or more participant already exists show error message
			else{					
				model.addAttribute("errormessage",ctx.getMessage(ControllerConstants.CCF_MIS_CONFIGURATION_ERROR_MESSAGE));
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
		}
		// Else show landscape settings screen
		else{
			Landscape landscape=ControllerHelper.findLandscape();
			// if required entities are not available display error message
			if(createLandscapeHelper.verifyEntities(landscape,model,ctx)){
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
			else if(createLandscapeHelper.verifyTFEntities(landscape,model,ctx)){
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
			//if all the required entities are available show landscape settings screen
			else{
				ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
				if(genericParticipant!= null){
					participantSettingsModel.setLandscapeConfigList(genericParticipant.getLandscapeFieldList());
					participantSettingsModel.setParticipantConfigList(genericParticipant.getParticipantFieldList());
				}
				landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
				Participant participant=landscape.getParticipant();
				landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
				return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
			}
		}

	}

	/**
	 * Controller method used to navigate from select participant screen to create landscape screen 
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE, method = RequestMethod.POST)
	public void createLandscape(@ModelAttribute Participant participant, Model model) {
		log.debug("createLandscape started");
		LandscapeModel landscapemodel=new LandscapeModel();
		populateGenericParticipantToModel(landscapemodel);
		createLandscapeHelper.populateCreateLandscapeModel(model,participant);
		landscapemodel.setParticipant(participant);
		model.addAttribute("landscapemodel",landscapemodel);
		log.debug("createLandscape finished");
	}

	/**
	 * Controller method used to save landscape 
	 * 
	 */ 
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_SAVELANDSCAPE, method = RequestMethod.POST)
	public String saveLandscape(@RequestParam(ControllerConstants.PARTICIPANTHIDDEN) String participantparam,@ModelAttribute("landscapemodel") @Valid LandscapeModel landscapemodel,BindingResult bindingResult, Model model, HttpServletRequest request) {
		log.debug("saveLandscape started");
		LandscapeValidator landscapeValidator=new LandscapeValidator();
		landscapemodel.normalizeParticipantUrl(); 
		landscapeValidator.validate(landscapemodel, bindingResult);
		if(landscapemodel.getParticipant().getSystemKind().equals(SystemKind.GENERIC)){ 
			validateGenericParticipant(landscapemodel, bindingResult);
		}
		if (bindingResult.hasErrors()) {
			Participant particpantHidden = getParticipant(participantparam);
			createLandscapeHelper.populateCreateLandscapeModel(model,particpantHidden);
			model.addAttribute("participant", particpantHidden);
			model.addAttribute("landscapemodel", landscapemodel);
			return UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE;
		}  
		try {
			log.debug("before persistModel");
			createLandscapeHelper.persistModel(landscapemodel);
			log.debug("persistModel succeeded");
		} catch(Exception exception) {
			log.error("persistModel failed", exception);
			model.addAttribute("errormessage",exception.getMessage());
			model.addAttribute("connectionerror", exception.getMessage());
			return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
		}
		ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
		if(genericParticipant != null){
			participantSettingsModel.setLandscapeConfigList(genericParticipant.getLandscapeFieldList());
			participantSettingsModel.setParticipantConfigList(genericParticipant.getParticipantFieldList());
		}
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel, model);
		log.debug("saveLandscape ended");
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS; 
	}

	/**
	 * @param participantparam
	 * @return
	 */
	private Participant getParticipant(String participantparam) {
		Participant particpantHidden=new Participant();
		if((SystemKind.QC.toString()).equals(participantparam)){
			particpantHidden.setSystemKind(SystemKind.QC);
		}  
		else if(((SystemKind.SWP.toString()).equals(participantparam))){ 
			particpantHidden.setSystemKind(SystemKind.SWP);
		}else{
			particpantHidden.setSystemKind(SystemKind.GENERIC); // Not sure why this method is used need to validate
		}
		return particpantHidden;
	}

	
	/**
	 * Controller method to test connection with TF credentials 
	 * 
	 */
	@RequestMapping(value =UIPathConstants.CREATELANDSCAPE_TESTCONNECTION, method = RequestMethod.POST)
	public @ResponseBody String testTeamForgeConnection(@RequestParam("username") String username,@RequestParam("password") String password, Model model, HttpServletRequest request) {
		String returnText; 
		RequestContext ctx = new RequestContext(request);
		try{ 
			Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
			connection.getTeamForgeClient().login50(username, password); 
			returnText="<strong><font color='green'>"+ ctx.getMessage(ControllerConstants.TF_CONNECTION_SUCCESS_MESSAGE)+"</font></strong>";
		}  catch(RemoteException remoteException){
			log.debug(remoteException.getMessage());
			returnText="<strong><font color='red'>"+ctx.getMessage(ControllerConstants.TF_CONNECTION_FAILURE_MESSAGE)+ remoteException.getMessage()+"</font></strong>";
		}
		return returnText;
	}
	
	@InitBinder
	public void myInitBinder(WebDataBinder binder){
		//do not bind these fields 
		binder.setDisallowedFields(new String[]{"landscape.participant","landscape.teamForge"});
	}

	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

	@ModelAttribute(TF_URL_MODEL_ATTRIBUTE)
	public String populateTfUrl() {
		return ccfRuntimePropertyHolder.getTfUrl();
	}

	private void populateGenericParticipantToModel(LandscapeModel landscapemodel) {
		if(genericParticipant != null){
			if(genericParticipant.getLandscapeFieldList()!= null){
				landscapemodel.setLandscapeConfigList(genericParticipant.getLandscapeFieldList());
			}
			if(genericParticipant.getParticipantFieldList()!= null){
				landscapemodel.setParticipantConfigList(genericParticipant.getParticipantFieldList());
			}
		}
	}
	
	private void validateGenericParticipant(LandscapeModel landscapeModel, BindingResult bindingResult){
		if(genericParticipant != null){
			GenericParticipantValidator participantValidator = genericParticipant.getCustomValidator();
			if(genericParticipant.getCustomValidator() == null){
				participantValidator = new DefaultGenericParticipantValidator(); 
				participantValidator.validate(landscapeModel, bindingResult);
			}else{
				participantValidator.validate(landscapeModel, bindingResult);
			}
		}
	}



}
