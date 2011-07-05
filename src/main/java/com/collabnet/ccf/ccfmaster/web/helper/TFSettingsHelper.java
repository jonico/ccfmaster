package com.collabnet.ccf.ccfmaster.web.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.model.TFSettingsModel;

public class TFSettingsHelper {
	
	/**
	 * 
	 * Helper method to populate TFsettings
	 * 
	 */
	public void populateTFSettingsModel(TFSettingsModel tfSettingsModel,Landscape landscape,Participant teamforge,Model model){
		
		LandscapeConfig tfUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_TF_USERNAME).getSingleResult();
		LandscapeConfig tfPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD).getSingleResult();
		tfPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(tfPasswordLandscapeConfig.getVal()));
		tfPasswordLandscapeConfig.flush();
		
		Direction forwarDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		DirectionConfig tfMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(forwarDirection,ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE).getSingleResult();
		
		tfSettingsModel.setTeamforge(teamforge);
		tfSettingsModel.setLandscape(landscape);
		tfSettingsModel.setTfUserNameLandscapeConfig(tfUserNameLandscapeConfig);
		tfSettingsModel.setTfPasswordLandscapeConfig(tfPasswordLandscapeConfig);
		tfSettingsModel.setTfMaxAttachmentSize(tfMaxAttachmentSize);
		
		model.addAttribute("tfsystemid",teamforge.getSystemId());
		model.addAttribute("tfdescription",teamforge.getDescription());
		model.addAttribute("tfsettingsmodel",tfSettingsModel);
		model.addAttribute("landscape",landscape);
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("selectedLink", ControllerConstants.TFSETTINGS);
		model.addAttribute("tfusernameversion",tfSettingsModel.getTfUserNameLandscapeConfig().getVersion());
		model.addAttribute("tfusernameid",tfSettingsModel.getTfUserNameLandscapeConfig().getId());
		model.addAttribute("tfpasswordversion",tfSettingsModel.getTfPasswordLandscapeConfig().getVersion());
		model.addAttribute("tfpasswordid",tfSettingsModel.getTfPasswordLandscapeConfig().getId());
		model.addAttribute("teamforgeversion",tfSettingsModel.getTeamforge().getVersion());
		model.addAttribute("teamforgeid",tfSettingsModel.getTeamforge().getId());
		model.addAttribute("landscapeversion",tfSettingsModel.getLandscape().getVersion());
		model.addAttribute("landscapeid",tfSettingsModel.getLandscape().getId());
		model.addAttribute("tfmaxattachmentsizeversion",tfSettingsModel.getTfMaxAttachmentSize().getVersion());
		model.addAttribute("tfmaxattachmentsizeid",tfSettingsModel.getTfMaxAttachmentSize().getId());
	}
	

	/**
	 * Helper method to update TF settings
	 *
	 */
	public void updateTFSettings(TFSettingsModel tfSettingsModel,
			Model model, HttpServletRequest request, RequestContext ctx,
			Landscape landscape) {
		Participant teamforgeTimezone=tfSettingsModel.getTeamforge();
		teamforgeTimezone.setId(Long.valueOf(request.getParameter(ControllerConstants.TEAMFORGE_ID)));
		teamforgeTimezone.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.TEAMFORGE_VERSION)));
		teamforgeTimezone.merge();	
		
		LandscapeConfig tfUsernameLandscapeConfig=tfSettingsModel.getTfUserNameLandscapeConfig();
		landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
		landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
		tfUsernameLandscapeConfig.setLandscape(landscape);
		tfUsernameLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.TFUSERNAME_ID)));		
		tfUsernameLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.TFUSERNAME_VERSION)));
		tfUsernameLandscapeConfig.merge();
		
		
		LandscapeConfig tfPasswordLandscapeConfig=tfSettingsModel.getTfPasswordLandscapeConfig();
		tfPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(tfPasswordLandscapeConfig.getVal()));
		landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
		landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
		tfPasswordLandscapeConfig.setLandscape(landscape);
		tfPasswordLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.TFPASSWORD_ID)));
		tfPasswordLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.TFPASSWORD_VERSION)));
		tfPasswordLandscapeConfig.merge();
		
		DirectionConfig tfMaxAttachmentSize=tfSettingsModel.getTfMaxAttachmentSize();
		tfMaxAttachmentSize.setId(Long.valueOf(request.getParameter(ControllerConstants.TFMAXATTACHMENTSIZE_ID)));
		tfMaxAttachmentSize.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.TFMAXATTACHMENTSIZE_VERSION)));
		Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
		tfMaxAttachmentSize.setDirection(forwardDirection);
		tfMaxAttachmentSize.merge();
		
		model.addAttribute("tfsettingsmodel",tfSettingsModel);
		model.addAttribute("selectedLink", ControllerConstants.TFSETTINGS);
		model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.TFSAVESUCCESSMESSAGE));
	}
	
	
}
