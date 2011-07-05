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
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;

public class LandscapeParticipantSettingsHelper {

	
    


	/**
	 * Helper method to populate the participant settings 
	 * 
	 */
public void populateParticipantSettingsModel(ParticipantSettingsModel participantSettingsModel,Landscape landscape,Participant participant,Model model){
		
		ParticipantConfig participantUrlParticipantConfig=null;
		LandscapeConfig participantUserNameLandscapeConfig=null;
		LandscapeConfig participantPasswordLandscapeConfig=null;
		LandscapeConfig participantResyncUserNameLandscapeConfig=null;
		LandscapeConfig participantResyncPasswordLandscapeConfig=null;
		Direction reverseDirection=null;
		DirectionConfig participantMaxAttachmentSize=null;
		 
		if(participant.getSystemKind().equals(SystemKind.QC)){
			participantUrlParticipantConfig=(ParticipantConfig)ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_QC_URL).getSingleResult();
			participantUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_QC_USERNAME).getSingleResult();
			participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_QC_PASSWORD).getSingleResult();
			participantPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantPasswordLandscapeConfig.getVal()));
			participantPasswordLandscapeConfig.flush();
			reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
			participantMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE).getSingleResult();
		}
		
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participantUrlParticipantConfig=(ParticipantConfig)ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_SWP_URL).getSingleResult();
			participantUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_USERNAME).getSingleResult();
			participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_PASSWORD).getSingleResult();
			participantPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantPasswordLandscapeConfig.getVal()));
			participantPasswordLandscapeConfig.flush();
			participantResyncUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getSingleResult();
			participantResyncPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getSingleResult();
			participantResyncPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantResyncPasswordLandscapeConfig.getVal()));
			participantResyncPasswordLandscapeConfig.flush();
			reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
			participantMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE).getSingleResult();
		}
			
		participantSettingsModel.setParticipant(participant);
		participantSettingsModel.setLandscape(landscape);
		participantSettingsModel.setParticipantUrlParticipantConfig(participantUrlParticipantConfig);
		participantSettingsModel.setParticipantPasswordLandscapeConfig(participantPasswordLandscapeConfig);
		participantSettingsModel.setParticipantUserNameLandscapeConfig(participantUserNameLandscapeConfig);
		participantSettingsModel.setParticipantMaxAttachmentSize(participantMaxAttachmentSize);
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participantSettingsModel.setParticipantResyncUserNameLandscapeConfig(participantResyncUserNameLandscapeConfig);
			participantSettingsModel.setParticipantResyncPasswordLandscapeConfig(participantResyncPasswordLandscapeConfig);
		}
		
		model.addAttribute("systemkind",participant.getSystemKind());
		model.addAttribute("systemid",participant.getSystemId());
		model.addAttribute("description",participant.getDescription());
		model.addAttribute("qcsettingsmodel",participantSettingsModel);
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", ControllerConstants.QCSETTINGS);
		model.addAttribute("version",participantSettingsModel.getParticipantUrlParticipantConfig().getVersion());
		model.addAttribute("id",participantSettingsModel.getParticipantUrlParticipantConfig().getId());
		model.addAttribute("usernameversion",participantSettingsModel.getParticipantUserNameLandscapeConfig().getVersion());
		model.addAttribute("usernameid",participantSettingsModel.getParticipantUserNameLandscapeConfig().getId());
		model.addAttribute("passwordversion",participantSettingsModel.getParticipantPasswordLandscapeConfig().getVersion());
		model.addAttribute("passwordid",participantSettingsModel.getParticipantPasswordLandscapeConfig().getId());
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			model.addAttribute("resyncusernameversion",participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getVersion());
			model.addAttribute("resyncusernameid",participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getId());
			model.addAttribute("resyncpasswordversion",participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getVersion());
			model.addAttribute("resyncpasswordid",participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getId());
			
		}
		model.addAttribute("participantversion",participantSettingsModel.getParticipant().getVersion());
		model.addAttribute("participantid",participantSettingsModel.getParticipant().getId());
		model.addAttribute("landscapeversion",participantSettingsModel.getLandscape().getVersion());
		model.addAttribute("landscapeid",participantSettingsModel.getLandscape().getId());
		model.addAttribute("maxattachmentsizeversion",participantSettingsModel.getParticipantMaxAttachmentSize().getVersion());
		model.addAttribute("maxattachmentsizeid",participantSettingsModel.getParticipantMaxAttachmentSize().getId());
		model.addAttribute("participant",participantSettingsModel.getParticipant());
	}
    
/**
 * Helper method to update the participant settings 
 * 
 */
public  void updateParticipantSettings(
		ParticipantSettingsModel participantSettingsModel, Model model,
		HttpServletRequest request, RequestContext ctx,
		Landscape landscape, Participant participant) {
	
	Participant participantTimezone=participantSettingsModel.getParticipant();
	participantTimezone.setId(Long.valueOf(request.getParameter(ControllerConstants.PARTICIPANT_ID)));
	participantTimezone.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.PARTICIPANT_VERSION)));
	participantTimezone.merge();	
		
	ParticipantConfig participantUrlParticipantConfig=participantSettingsModel.getParticipantUrlParticipantConfig();
	participantUrlParticipantConfig.setParticipant(participant);
	participantUrlParticipantConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.URLPARTICIPANTCONFIG_ID)));
	participantUrlParticipantConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.URLPARTICIPANTCONFIG_VERSION)));
	participantUrlParticipantConfig.merge(); 
	
	
	LandscapeConfig participantUsernameLandscapeConfig=participantSettingsModel.getParticipantUserNameLandscapeConfig();
	landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
	landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
	participantUsernameLandscapeConfig.setLandscape(landscape);
	participantUsernameLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.USERNAME_ID)));		
	participantUsernameLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.USERNAME_VERSION)));
	participantUsernameLandscapeConfig.merge();
	
	
	LandscapeConfig participantPasswordLandscapeConfig=participantSettingsModel.getParticipantPasswordLandscapeConfig();
	participantPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantPasswordLandscapeConfig.getVal()));
	landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
	landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
	participantPasswordLandscapeConfig.setLandscape(landscape);
	participantPasswordLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.PASSWORD_ID)));
	participantPasswordLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.PASSWORD_VERSION)));
	participantPasswordLandscapeConfig.merge();
	
	// For SWP only
	if(landscape.getParticipant().getSystemKind().equals(SystemKind.SWP)){
	LandscapeConfig participantResyncUserNameLandscapeConfig=participantSettingsModel.getParticipantResyncUserNameLandscapeConfig();
	landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
	landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
	participantResyncUserNameLandscapeConfig.setLandscape(landscape);
	participantResyncUserNameLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.RESYNCUSERNAME_ID)));		
	participantResyncUserNameLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.RESYNCUSERNAME_VERSION)));
	participantResyncUserNameLandscapeConfig.merge();
	
	
	LandscapeConfig participantResyncPasswordLandscapeConfig=participantSettingsModel.getParticipantResyncPasswordLandscapeConfig();
	participantResyncPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantResyncPasswordLandscapeConfig.getVal()));
	landscape.setId(Long.valueOf(request.getParameter(ControllerConstants.LANDSCAPE_ID)));
	landscape.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.LANDSCAPE_VERSION)));
	participantResyncPasswordLandscapeConfig.setLandscape(landscape);
	participantResyncPasswordLandscapeConfig.setId(Long.valueOf(request.getParameter(ControllerConstants.RESYNCPASSWORD_ID)));
	participantResyncPasswordLandscapeConfig.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.RESYNCPASSWORD_VERSION)));
	participantResyncPasswordLandscapeConfig.merge();
	}
	
	DirectionConfig participantMaxAttachmentSize=participantSettingsModel.getParticipantMaxAttachmentSize();
	participantMaxAttachmentSize.setId(Long.valueOf(request.getParameter(ControllerConstants.MAXATTACHMENTSIZE_ID)));
	participantMaxAttachmentSize.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.MAXATTACHMENTSIZE_VERSION)));
	Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
	participantMaxAttachmentSize.setDirection(reverseDirection);
	participantMaxAttachmentSize.merge();
	
	model.addAttribute("qcsettingsmodel",participantSettingsModel);
	model.addAttribute("selectedLink", ControllerConstants.QCSETTINGS);
	model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.PARTICIPANTSAVESUCCESSMESSAGE));
}
	
}
