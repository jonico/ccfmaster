package com.collabnet.ccf.ccfmaster.controller.web;

import java.rmi.RemoteException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.model.LandscapeModel;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.LandscapeValidator;
import com.collabnet.teamforge.api.Connection;


  

@RequestMapping("/createlandscape/**")
@Controller
public class CreateLandscapeController {

	
	private static final Logger log = LoggerFactory.getLogger(CreateLandscapeController.class);
	CreateLandscapeHelper createLandscapeHelper=new CreateLandscapeHelper();
	LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();
	
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
					model.addAttribute("participant", participant);
					model.addAttribute("enums",SystemKind.values());
					return UIPathConstants.CREATELANDSCAPE_INDEX;
				}
				//If there is single or more participant already exists show error message
				else{					
		 			model.addAttribute("errormessage",ctx.getMessage("ccfmisconfigurationerrormessage"));
					return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
				}
		}
		// Else show landscape settings screen
		else{
				Landscape landscape=ControllerHelper.findLandscape(model);
				// if required entities are not available display error message
				if(createLandscapeHelper.verifyEntities(landscape,model,ctx)){
					return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
				}
				else if(createLandscapeHelper.verifyTFEntities(landscape,model,ctx)){
					return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
				}
				//if all the required entities are available show landscape settings screen
				else{
				Participant participant=landscape.getParticipant();
				ParticipantSettingsModel qcSettingsModel=new ParticipantSettingsModel();
				landscapeParticipantSettingsHelper.populateParticipantSettingsModel(qcSettingsModel,landscape,participant,model);
				return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
				}
		}
		   
    }
    
	/**
	 * Controller method used to navigate from select participant screen to create landscape screen 
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE, method = RequestMethod.POST)
    public void createlandscape(@ModelAttribute Participant participant, Model model) {
		LandscapeModel landscapemodel=new LandscapeModel();
		createLandscapeHelper.populateCreateLandscapeModel(model,participant);	
		model.addAttribute("landscapemodel",landscapemodel);
	 }
	
	/**
	* Controller method used to save landscape 
	* 
	*/ 
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_SAVELANDSCAPE, method = RequestMethod.POST)
	public String savelandscape(@ModelAttribute("landscapemodel") @Valid LandscapeModel landscapemodel,BindingResult bindingResult, Model model, HttpServletRequest request) {
		log.info("Creating Landscape");	
		
		LandscapeValidator landscapeValidator=new LandscapeValidator();	
 		landscapeValidator.validate(landscapemodel, bindingResult);
		if (bindingResult.hasErrors()) {
				Participant particpanthidden=new Participant();
				if((request.getParameter(ControllerConstants.PARTICIPANTHIDDEN)).equals(SystemKind.QC.toString())){
					particpanthidden.setSystemKind(SystemKind.QC);
				}   
				else{ 
					particpanthidden.setSystemKind(SystemKind.SWP);
				}
				
				createLandscapeHelper.populateCreateLandscapeModel(model,particpanthidden);		
				model.addAttribute("participant",particpanthidden);
		        model.addAttribute("landscapemodel", landscapemodel);
		        return UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE;
	        }  
		try{	
			createLandscapeHelper.persistModel(landscapemodel);
		}
		catch(Exception exception){
			model.addAttribute("errormessage",exception.getMessage());
			return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
		}
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant participant=landscape.getParticipant();
		ParticipantSettingsModel qcSettingsModel=new ParticipantSettingsModel();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(qcSettingsModel,landscape,participant,model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS; 
     }
	 
	/**
	 * Controller method to test connection with teamforge credentials 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_TESTCONNECTION, method = RequestMethod.POST)
    public String  testconnection(@ModelAttribute("landscapemodel") LandscapeModel landscapemodel, Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TFUserDetails tfuser=(TFUserDetails)user;
		String username=landscapemodel.getTfUserNameLandscapeConfig().getVal();
		String password=Obfuscator.decodePassword(landscapemodel.getTfPasswordLandscapeConfig().getVal());
		Participant particpantHidden=new Participant();
		if((request.getParameter(ControllerConstants.PARTICIPANTHIDDEN)).equals(SystemKind.QC.toString())){
			particpantHidden.setSystemKind(SystemKind.QC);
		}  
		else{ 
			particpantHidden.setSystemKind(SystemKind.SWP);
		}
		createLandscapeHelper.populateCreateLandscapeModel(model,particpantHidden);		
		model.addAttribute("participant",particpantHidden);	
		try{ 
			Connection teamforgeConnection=tfuser.getConnection();
			teamforgeConnection.getTeamForgeClient().login50(username, password);
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.TFCONNECTIONSUCCESSMESSAGE));
			model.addAttribute("landscapemodel",landscapemodel);
		} 
		catch(RemoteException remoteException){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.TEAMFORGE)+ remoteException.getMessage());
		}
		return UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE;
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
 
	
	
	
}
