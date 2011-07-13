package com.collabnet.ccf.ccfmaster.controller.web;


import java.rmi.RemoteException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;

import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TFSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.model.TFSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.TFSettingsValidator;
import com.collabnet.teamforge.api.Connection;


  

@RequestMapping("/admin/**")
@Controller
public class LandscapeTFSettingsController {

	
	TFSettingsHelper tfSettingsHelper=new TFSettingsHelper();
	private static final Logger log = LoggerFactory.getLogger(LandscapeTFSettingsController.class);
	
	/**
	* Controller method to display TF settings 
	* 
	*/ 
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS, method = RequestMethod.GET)
    public String displaytfsettings(Model model, HttpServletRequest request) {
		log.info("in displaytfsettings");
		Landscape landscape=ControllerHelper.findLandscape(model);
		TFSettingsModel tfSettingsModel=new TFSettingsModel();
		Participant teamforge=landscape.getTeamForge();
		tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,landscape,teamforge,model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
		  
    }
	
	/**
	* Controller method to persist TF settings 
	* 
	*/ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVETFSETTINGS, method = RequestMethod.POST)
	@Transactional
    public String savetfsettings(@ModelAttribute("tfsettingsmodel") @Valid TFSettingsModel tfSettingsModel,BindingResult bindingResult,Model model, HttpServletRequest request) {
		log.info("in savetfsettings");
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model); 
		Participant teamforge=landscape.getTeamForge();
		TFSettingsValidator tfSettingsValidator=new TFSettingsValidator();	
		tfSettingsValidator.validate(tfSettingsModel, bindingResult);
		if (bindingResult.hasErrors()) {
			tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,landscape,teamforge,model);
			return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
        }  
		try{
			tfSettingsHelper.updateTFSettings(tfSettingsModel, model, request, ctx, landscape);
		} 
		catch(Exception exception){
			model.addAttribute("connectionerror",ctx.getMessage("tfsavefailmessage")+ exception.getMessage());
		}
		Landscape updatedLandscape=ControllerHelper.findLandscape(model);
		Participant updatedTeamforge=updatedLandscape.getTeamForge();
		tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,updatedLandscape,updatedTeamforge,model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
	  }

	
	/**
	* Controller method to test connection with TF credentials 
	* 
	*/ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_TESTCONNECTION, method = RequestMethod.POST)
    public String  testconnection(@ModelAttribute("tfSettingsModel") TFSettingsModel tfSettingsModel, Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant teamforge=landscape.getTeamForge();
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		TFUserDetails tfuser=(TFUserDetails)user;
		String username=tfSettingsModel.getTfUserNameLandscapeConfig().getVal();
		String password=Obfuscator.decodePassword(tfSettingsModel.getTfPasswordLandscapeConfig().getVal());
		tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,landscape,teamforge,model);		
		try{ 
			Connection teamforgeConnection=tfuser.getConnection();
			teamforgeConnection.getTeamForgeClient().login50(username, password); 
			model.addAttribute("connectionmessage",ctx.getMessage("tfconnectionsuccessmessage"));
			model.addAttribute("tfSettingsModel",tfSettingsModel);
		} 
		catch(RemoteException remoteException){
			model.addAttribute("connectionerror",ctx.getMessage("teamforge")+ remoteException.getMessage());
		}
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
    }
	
	
	
	@ModelAttribute("timezones")
    public java.util.Collection<Timezone> populateTimezones() {
        return Arrays.asList(Timezone.class.getEnumConstants());
    }
	
}
