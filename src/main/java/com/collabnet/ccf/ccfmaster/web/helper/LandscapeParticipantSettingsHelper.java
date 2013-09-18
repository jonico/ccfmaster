package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.ui.Model;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;
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
		Landscape landscape=ControllerHelper.findLandscape();
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
		ParticipantConfig participantUrlParticipantConfig= new ParticipantConfig(); //Code fix needed
		LandscapeConfig participantUserNameLandscapeConfig=new LandscapeConfig();//Code fix needed
		LandscapeConfig participantPasswordLandscapeConfig=new LandscapeConfig();//Code fix needed
		LandscapeConfig participantResyncUserNameLandscapeConfig=null;
		LandscapeConfig participantResyncPasswordLandscapeConfig=null;

		if(participant.getSystemKind().equals(SystemKind.QC)){
			participantUrlParticipantConfig=(ParticipantConfig)ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_QC_URL).getSingleResult();
			participantUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_QC_USERNAME).getSingleResult();
			participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_QC_PASSWORD).getSingleResult();
			participantPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantPasswordLandscapeConfig.getVal()));
		}

		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participantUrlParticipantConfig=(ParticipantConfig)ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, ControllerConstants.CCF_PARTICIPANT_SWP_URL).getSingleResult();
			participantUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_USERNAME).getSingleResult();
			participantPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_PASSWORD).getSingleResult();
			participantPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantPasswordLandscapeConfig.getVal()));
			participantResyncUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getSingleResult();
			participantResyncPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getSingleResult();
			participantResyncPasswordLandscapeConfig.setVal(Obfuscator.decodePassword(participantResyncPasswordLandscapeConfig.getVal()));
		}
		
		if(participant.getSystemKind().equals(SystemKind.GENERIC)){// Need to refactor it later and also needs to consider race condition on list of CCFCoreProperty
			List<CCFCoreProperty> landscapeConfigList= participantSettingsModel.getLandscapeConfigList();
			List<CCFCoreProperty> participantConfigList = participantSettingsModel.getParticipantConfigList();
			for(CCFCoreProperty property: landscapeConfigList){
				String name = property.getName();
				LandscapeConfig config = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,name).getSingleResult();
				if(CCFCorePropertyType.PASSWORD.equals(property.getType())) {
					property.setValue(Obfuscator.decodePassword(config.getVal()));
				} else {
					property.setValue(config.getVal());
				}
			}
		
			for(CCFCoreProperty property: participantConfigList){
				String name = property.getName();
				ParticipantConfig config = ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, name).getSingleResult();
				property.setValue(config.getVal());
				
			}
		}

		participantSettingsModel.setParticipant(participant);
		participantSettingsModel.setLandscape(landscape);
		participantSettingsModel.setParticipantUrlParticipantConfig(participantUrlParticipantConfig);
		participantSettingsModel.setParticipantPasswordLandscapeConfig(participantPasswordLandscapeConfig);
		participantSettingsModel.setParticipantUserNameLandscapeConfig(participantUserNameLandscapeConfig);
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
		model.addAttribute("participant",participantSettingsModel.getParticipant());
	}

	/**
	 * Helper method to update the participant settings 
	 * 
	 */
	public  void updateParticipantSettings(ParticipantSettingsModel participantSettingsModel, Model model,
			HttpServletRequest request) {
		Landscape landscape=ControllerHelper.findLandscape();
		// merge swp participant settings 
		if(landscape.getParticipant().getSystemKind().equals(SystemKind.SWP)){
			mergeSWPParticipantSettings(participantSettingsModel, request,model,landscape);
		}else if ((landscape.getParticipant().getSystemKind().equals(SystemKind.QC))){
			//merge qc participant settings
			mergeQCPartcipantSettings(participantSettingsModel, request, model,landscape);
		}else{
			mergeParticipantSettings(participantSettingsModel, request, model,landscape);
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
		
		//merge participantResyncUserNameLandscapeConfig
		LandscapeConfig participantResyncUserNameLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getSingleResult();
		participantResyncUserNameLandscapeConfig.setVal(participantSettingsModel.getParticipantResyncUserNameLandscapeConfig().getVal());
		participantResyncUserNameLandscapeConfig.merge();

		//merge participantResyncPasswordLandscapeConfig
		LandscapeConfig participantResyncPasswordLandscapeConfig=LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getSingleResult();
		participantResyncPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantSettingsModel.getParticipantResyncPasswordLandscapeConfig().getVal()));
		participantResyncPasswordLandscapeConfig.merge();
	}
	
	private void mergeParticipantSettings(ParticipantSettingsModel participantSettingsModel,
			HttpServletRequest request,Model model,Landscape landscape){
		Participant participant=landscape.getParticipant();
		//merge participantTimezone
		Participant participantTimezone=participantSettingsModel.getParticipant();
		participantTimezone.setId(Long.valueOf(request.getParameter(ControllerConstants.PARTICIPANT_ID)));
		participantTimezone.setVersion(Integer.parseInt(request.getParameter(ControllerConstants.PARTICIPANT_VERSION)));
		participantTimezone.merge();
		
		if(participant.getSystemKind().equals(SystemKind.GENERIC)){// Need to refactor it later and also needs to consider race condition on list of CCFCoreProperty
			List<CCFCoreProperty> landscapeConfigList= participantSettingsModel.getLandscapeConfigList();
			List<CCFCoreProperty> participantConfigList = participantSettingsModel.getParticipantConfigList();
			for(CCFCoreProperty property: landscapeConfigList){
				String name = property.getName();
				LandscapeConfig config = LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,name).getSingleResult();
				if (CCFCorePropertyType.PASSWORD.equals(property.getType())) {
					config.setVal(Obfuscator.encodePassword(property.getValue()));
				} else {
					config.setVal(property.getValue());
				}
				config.persist();
			}
		
			for(CCFCoreProperty property: participantConfigList){
				String name = property.getName();
				ParticipantConfig config = ParticipantConfig.findParticipantConfigsByParticipantAndName(participant, name).getSingleResult();
				config.setVal(property.getValue());
				config.persist();
				
			}
		}
	}

}
