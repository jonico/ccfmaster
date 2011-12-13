package com.collabnet.ccf.ccfmaster.web.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.ui.Model;

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
	public void populateParticipantSettingsModel(ParticipantSettingsModel participantSettingsModel,Model model){
		Landscape landscape=ControllerHelper.findLandscape(model);
		Participant participant=landscape.getParticipant();
		makeParticipantSettingsModel(participantSettingsModel, landscape,participant);
		makeModel(model, participantSettingsModel, landscape, participant);		
	}

	/**
	 * @param participantSettingsModel
	 * @param landscape
	 * @param participant
	 */
	private void makeParticipantSettingsModel(
			ParticipantSettingsModel participantSettingsModel,
			Landscape landscape, Participant participant) {
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
			reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
			participantMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE).getSingleResult();
		}

		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participantUrlParticipantConfig=(ParticipantConfig)ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_SWP_URL).getSingleResult();
			participantUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_USERNAME).getSingleResult();
			participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_PASSWORD).getSingleResult();
			participantPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantPasswordLandscapeConfig.getVal()));
			participantResyncUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getSingleResult();
			participantResyncPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getSingleResult();
			participantResyncPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantResyncPasswordLandscapeConfig.getVal()));
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
	}

	/**
	 * @param model
	 * @param participantSettingsModel
	 * @param landscape
	 * @param participant
	 */
	public void makeModel(Model model,
			ParticipantSettingsModel participantSettingsModel,
			Landscape landscape, Participant participant) {
		model.addAttribute("systemkind", participant.getSystemKind());
		model.addAttribute("systemid", participant.getSystemId());
		model.addAttribute("description", participant.getDescription());
		model.addAttribute("qcsettingsmodel", participantSettingsModel);
		model.addAttribute("landscape", landscape);
		model.addAttribute("selectedLink", ControllerConstants.QCSETTINGS);
		model.addAttribute("version", participantSettingsModel.getParticipantUrlParticipantConfig().getVersion());
		model.addAttribute("id", participantSettingsModel.getParticipantUrlParticipantConfig().getId());
		model.addAttribute("usernameversion", participantSettingsModel.getParticipantUserNameLandscapeConfig().getVersion());
		model.addAttribute("usernameid", participantSettingsModel.getParticipantUserNameLandscapeConfig().getId());
		model.addAttribute("passwordversion", participantSettingsModel.getParticipantPasswordLandscapeConfig().getVersion());
		model.addAttribute("passwordid", participantSettingsModel
				.getParticipantPasswordLandscapeConfig().getId());
		if (participant.getSystemKind().equals(SystemKind.SWP)) {
			model.addAttribute("resyncusernameversion",participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getVersion());		
			model.addAttribute("resyncusernameid", participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getId());
			model.addAttribute("resyncpasswordversion",	participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getVersion());
			model.addAttribute("resyncpasswordid", participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getId());
		}
		model.addAttribute("participantversion", participantSettingsModel.getParticipant().getVersion());
		model.addAttribute("participantid", participantSettingsModel.getParticipant().getId());
		model.addAttribute("landscapeversion", participantSettingsModel.getLandscape().getVersion());
		model.addAttribute("landscapeid", participantSettingsModel.getLandscape().getId());
		model.addAttribute("maxattachmentsizeversion", participantSettingsModel.getParticipantMaxAttachmentSize().getVersion());
		model.addAttribute("maxattachmentsizeid", participantSettingsModel.getParticipantMaxAttachmentSize().getId());
		model.addAttribute("participant",participantSettingsModel.getParticipant());
	}

	/**
	 * Helper method to update the participant settings 
	 * 
	 */
	public  void updateParticipantSettings(ParticipantSettingsModel participantSettingsModel, Model model,
			HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape(model);
		// merge swp participant settings 
		if(landscape.getParticipant().getSystemKind().equals(SystemKind.SWP)){
			mergeSWPParticipantSettings(participantSettingsModel, request,model,landscape);
		}else{
			//merge qc participant settings
			mergeQCPartcipantSettings(participantSettingsModel, request, model,landscape);
		}
		model.addAttribute("qcsettingsmodel",participantSettingsModel);
		model.addAttribute("selectedLink", ControllerConstants.QCSETTINGS);
		FlashMap.setSuccessMessage(ControllerConstants.PARTICIPANT_SAVE_SUCCESS_MESSAGE);
		}

	/**
	 * @param participantSettingsModel
	 * @param request
	 * @param landscape
	 * @param participant
	 */
	private void mergeQCPartcipantSettings(
			ParticipantSettingsModel participantSettingsModel,
			HttpServletRequest request,Model model,Landscape landscape) {
	
		Participant participant=landscape.getParticipant();
		
		//merge participantTimezone
		Participant participantTimezone=participantSettingsModel.getParticipant();
		participantTimezone.setId(Long.valueOf(request.getParameter(ControllerConstants.PARTICIPANT_ID)));
		participantTimezone.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.PARTICIPANT_VERSION)));
		participantTimezone.merge();	

		//merge participantUrlParticipantConfig
		ParticipantConfig participantUrlParticipantConfig=ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_QC_URL).getSingleResult();
		participantUrlParticipantConfig.setVal(participantSettingsModel.getParticipantUrlParticipantConfig().getVal());
		participantUrlParticipantConfig.merge(); 

		//merge participantUsernameLandscapeConfig
		LandscapeConfig participantUsernameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_QC_USERNAME).getSingleResult();
		participantUsernameLandscapeConfig.setVal(participantSettingsModel.getParticipantUserNameLandscapeConfig().getVal());
		participantUsernameLandscapeConfig.merge();

		//merge participantPasswordLandscapeConfig
		LandscapeConfig participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_QC_PASSWORD).getSingleResult();
		participantPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantSettingsModel.getParticipantPasswordLandscapeConfig().getVal()));
		participantPasswordLandscapeConfig.merge();

		//merge participantMaxAttachmentSize
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		DirectionConfig participantMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(direction, ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE).getSingleResult();
		participantMaxAttachmentSize.setVal(participantSettingsModel.getParticipantMaxAttachmentSize().getVal());
		participantMaxAttachmentSize.merge();
	}

	/**
	 * @param participantSettingsModel
	 * @param request
	 * @param landscape
	 */
	private void mergeSWPParticipantSettings(
			ParticipantSettingsModel participantSettingsModel,
			HttpServletRequest request,Model model,Landscape landscape) {
		
		Participant participant=landscape.getParticipant();
		//merge participantTimezone
		Participant participantTimezone=participantSettingsModel.getParticipant();
		participantTimezone.setId(Long.valueOf(request.getParameter(ControllerConstants.PARTICIPANT_ID)));
		participantTimezone.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.PARTICIPANT_VERSION)));
		participantTimezone.merge();	

		//merge participantUrlParticipantConfig
		ParticipantConfig participantUrlParticipantConfig=ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_SWP_URL).getSingleResult();
		participantUrlParticipantConfig.setVal(participantSettingsModel.getParticipantUrlParticipantConfig().getVal());
		participantUrlParticipantConfig.merge(); 

		//merge participantUsernameLandscapeConfig
		LandscapeConfig participantUsernameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_USERNAME).getSingleResult();
		participantUsernameLandscapeConfig.setVal(participantSettingsModel.getParticipantUserNameLandscapeConfig().getVal());
		participantUsernameLandscapeConfig.merge();

		//merge participantPasswordLandscapeConfig
		LandscapeConfig participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_PASSWORD).getSingleResult();
		participantPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantSettingsModel.getParticipantPasswordLandscapeConfig().getVal()));
		participantPasswordLandscapeConfig.merge();

		//merge participantMaxAttachmentSize
		Direction direction=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
		DirectionConfig participantMaxAttachmentSize=DirectionConfig.findDirectionConfigsByDirectionAndName(direction, ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE).getSingleResult();
		participantMaxAttachmentSize.setVal(participantSettingsModel.getParticipantMaxAttachmentSize().getVal());
		participantMaxAttachmentSize.merge();
		
		//merge participantResyncUserNameLandscapeConfig
		LandscapeConfig participantResyncUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getSingleResult();
		participantResyncUserNameLandscapeConfig.setVal(participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getVal());
		participantResyncUserNameLandscapeConfig.merge();

		//merge participantResyncPasswordLandscapeConfig
		LandscapeConfig participantResyncPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getSingleResult();
		participantResyncPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getVal()));
		participantResyncPasswordLandscapeConfig.merge();
	}

}
