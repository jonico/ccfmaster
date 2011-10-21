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
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
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
		tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,model);
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
			tfSettingsHelper.populateTFSettingsModel(tfSettingsModel,model);
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
			}  
			catch(Exception exception){
				log.debug(exception.getMessage());
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
	public String  testTeamForgeConnection(@ModelAttribute("tfsettingsmodel") TFSettingsModel tfSettingsModel, Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		String username=tfSettingsModel.getTfUserNameLandscapeConfig().getVal();
		String password=Obfuscator.decodePassword(tfSettingsModel.getTfPasswordLandscapeConfig().getVal());
		try{ 
			Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
			connection.getTeamForgeClient().login50(username, password); 
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.TFCONNECTIONSUCCESSMESSAGE));
			
		} 
		catch(RemoteException remoteException){
			log.debug(remoteException.getMessage());
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.TEAMFORGE)+ remoteException.getMessage());
		}
		populateTFConnectionModel(tfSettingsModel, model);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFSETTINGS;
	}
	
	
	

	/**
	 * @param tfSettingsModel
	 * @param model
	 */
	private void populateTFConnectionModel(TFSettingsModel tfSettingsModel,
			Model model) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant teamforge=landscape.getTeamForge();
		Direction forwarDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		DirectionConfig tfMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(forwarDirection,ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE).getSingleResult();
		tfSettingsModel.setTeamforge(teamforge);
		tfSettingsModel.setLandscape(landscape);
		tfSettingsModel.setTfUserNameLandscapeConfig(tfSettingsModel.getTfUserNameLandscapeConfig());
		tfSettingsModel.setTfPasswordLandscapeConfig(tfSettingsModel.getTfPasswordLandscapeConfig());
		tfSettingsModel.setTfMaxAttachmentSize(tfMaxAttachmentSize);
		LandscapeConfig tfUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_TF_USERNAME).getSingleResult();
		LandscapeConfig tfPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD).getSingleResult();
		model.addAttribute("tfsystemid",teamforge.getSystemId());
		model.addAttribute("tfdescription",teamforge.getDescription());
		model.addAttribute("tfsettingsmodel",tfSettingsModel);
		model.addAttribute("landscape",landscape);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("selectedLink", ControllerConstants.TFSETTINGS);
		model.addAttribute("tfusernameversion",tfUserNameLandscapeConfig.getVersion());
		model.addAttribute("tfusernameid",tfUserNameLandscapeConfig.getId());
		model.addAttribute("tfpasswordversion",tfPasswordLandscapeConfig.getVersion());
		model.addAttribute("tfpasswordid",tfPasswordLandscapeConfig.getId());
		model.addAttribute("teamforgeversion",tfSettingsModel.getTeamforge().getVersion());
		model.addAttribute("teamforgeid",tfSettingsModel.getTeamforge().getId());
		model.addAttribute("landscapeversion",tfSettingsModel.getLandscape().getVersion());
		model.addAttribute("landscapeid",tfSettingsModel.getLandscape().getId());
		model.addAttribute("tfmaxattachmentsizeversion",tfSettingsModel.getTfMaxAttachmentSize().getVersion());
		model.addAttribute("tfmaxattachmentsizeid",tfSettingsModel.getTfMaxAttachmentSize().getId());
	}



	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

}
