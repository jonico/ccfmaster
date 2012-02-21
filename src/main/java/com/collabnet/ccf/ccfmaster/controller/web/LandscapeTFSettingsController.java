package com.collabnet.ccf.ccfmaster.controller.web;


import java.rmi.RemoteException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.web.helper.TFSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeConnectionHelper;
import com.collabnet.ccf.ccfmaster.web.model.TFSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.TFSettingsValidator;
import com.collabnet.teamforge.api.Connection;




@RequestMapping("/admin/**")
@Controller
public class LandscapeTFSettingsController extends AbstractLandscapeController{


	private static final String RESTART = "restart";
	TFSettingsHelper tfSettingsHelper=new TFSettingsHelper();
	private static final Logger log = LoggerFactory.getLogger(LandscapeTFSettingsController.class);

	
	/**
	 * Controller method to display TF settings 
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS, method = RequestMethod.GET)
	public String displayTFSettings(Model model, HttpServletRequest request) {
		TFSettingsModel tfSettingsModel=new TFSettingsModel();
		tfSettingsHelper.populateTFSettingsModel(tfSettingsModel);
		tfSettingsHelper.makeModel(tfSettingsModel, model); 
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;

	}

	/**
	 * Controller method to persist TF settings 
	 * 
	 */ 
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVETFSETTINGS, method = RequestMethod.POST)
	@Transactional
	public String saveTFSettings(@ModelAttribute("tfsettingsmodel") @Valid TFSettingsModel tfSettingsModel,BindingResult bindingResult,Model model, HttpServletRequest request) {
		//validate tfSettingsModel
		TFSettingsValidator tfSettingsValidator=new TFSettingsValidator();	
		tfSettingsValidator.validate(tfSettingsModel, bindingResult);
		if (bindingResult.hasErrors()) {
			tfSettingsHelper.populateTFSettingsModel(tfSettingsModel);
			tfSettingsHelper.makeModel(tfSettingsModel, model);
			return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
		} 
		else{
			try{
				tfSettingsHelper.updateTFSettings(tfSettingsModel, model, request);
				boolean hasRestart = Boolean.parseBoolean(request.getParameter(RESTART));
				if(hasRestart){
					LandscapeStatusController.restartCCFCoreStatus(model);
					FlashMap.setSuccessMessage(ControllerConstants.TF_SAVE_RESTART_SUCCESS_MESSAGE);
				}
			} catch(Exception exception) {
				log.debug("Error saving TF settings: " + exception.getMessage(), exception);
				FlashMap.setErrorMessage(ControllerConstants.TF_SAVE_FAIL_MESSAGE, exception.getMessage());
			}
			model.asMap().clear();
			return "redirect:/" +UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
		}
	}

	

	/**
	 * Controller method to test connection with TF credentials 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_TESTCONNECTION, method = RequestMethod.POST)
	public @ResponseBody String testTeamForgeConnection(@RequestParam("username") String username,@RequestParam("password") String password, Model model, HttpServletRequest request) {
		String returnText; 
		RequestContext ctx = new RequestContext(request);
		try{ 
			Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
			connection.getTeamForgeClient().login50(username, password); 
			returnText="<strong><font color='green'>"+ ctx.getMessage(ControllerConstants.TF_CONNECTION_SUCCESS_MESSAGE)+"</font></strong>";
			
		} 
		catch(RemoteException remoteException){
			log.debug("Error testing TeamFOrge connection: " + remoteException.getMessage(), remoteException);
			returnText="<strong><font color='red'>"+ctx.getMessage(ControllerConstants.TF_CONNECTION_FAILURE_MESSAGE)+ remoteException.getMessage()+"</font></strong>";
		}
		return returnText;
	}
	

	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

}
