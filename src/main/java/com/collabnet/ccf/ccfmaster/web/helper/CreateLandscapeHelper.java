package com.collabnet.ccf.ccfmaster.web.helper;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
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
import com.collabnet.ccf.ccfmaster.web.model.LandscapeModel;

@Configurable
public class CreateLandscapeHelper {

	@Autowired 
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;


	/**
	 * Helper method to populate default values of participant and teamforge to create landscape
	 * 
	 */
	public void populateCreateLandscapeModel(Model model, Participant participant){

		Participant teamforge=new Participant();
		teamforge.setSystemId(ControllerConstants.TF);
		teamforge.setSystemKind(SystemKind.TF);
		teamforge.setDescription(ControllerConstants.TFDESCRIPTION);
		if(participant.getSystemKind().equals(SystemKind.QC))
		{
			participant.setSystemId(ControllerConstants.QC);
			participant.setDescription(ControllerConstants.QCDESCRIPTION);
		}
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participant.setSystemId(ControllerConstants.SWP);
			participant.setDescription(ControllerConstants.SW_PDESCRIPTION);
		}
		model.addAttribute("plugid", ControllerConstants.PLUGID);
		model.addAttribute("tfsystemkind", teamforge.getSystemKind());
		model.addAttribute("tfsystemid", teamforge.getSystemId());
		model.addAttribute("parsystemid", participant.getSystemId());
		model.addAttribute("pdescription", participant.getDescription());
		model.addAttribute("tdescription", teamforge.getDescription());

	}


	/**
	 * Helper method to persist the landscapemodel to createlandscape 
	 * 
	 */
	@Transactional
	public void persistModel( LandscapeModel landscapemodel){

		Participant participant=landscapemodel.getParticipant();
		Participant teamforge=landscapemodel.getTeamforge();
		Landscape landscape=landscapemodel.getLandscape();
		ParticipantConfig urlParticipantconfig=landscapemodel.getParticipantUrlParticipantConfig();
		LandscapeConfig tfUserNameLandscapeConfig=landscapemodel.getTfUserNameLandscapeConfig();
		LandscapeConfig tfPasswordLandscapeConfig=landscapemodel.getTfPasswordLandscapeConfig();
		tfPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(tfPasswordLandscapeConfig.getVal()));
		LandscapeConfig participantUserNameLandscapeConfig=landscapemodel.getParticipantUserNameLandscapeConfig(); 
		LandscapeConfig participantPasswordLandscapeConfig=landscapemodel.getParticipantPasswordLandscapeConfig();
		participantPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantPasswordLandscapeConfig.getVal()));

		ParticipantConfig tfParticipantUrlParticipantconfig=new ParticipantConfig();
		tfParticipantUrlParticipantconfig.setName(ControllerConstants.CCF_PARTICIPANT_TF_URL);
		tfParticipantUrlParticipantconfig.setVal(ccfRuntimePropertyHolder.getTfUrl());
		tfParticipantUrlParticipantconfig.setParticipant(teamforge);

		//Persist
		teamforge.persist();
		participant.persist();
		landscape.persist();
		createDirections(landscape,participant);
		urlParticipantconfig.persist();
		tfUserNameLandscapeConfig.persist();
		tfPasswordLandscapeConfig.persist();
		participantUserNameLandscapeConfig.persist();
		participantPasswordLandscapeConfig.persist();
		tfParticipantUrlParticipantconfig.persist();

		//persist resync name and password for SWP
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			LandscapeConfig participantResyncUserNameLandscapeConfig=landscapemodel.getParticipantResyncUserNameLandscapeConfig();
			LandscapeConfig participantResyncPasswordLandscapeConfig=landscapemodel.getParticipantResyncPasswordLandscapeConfig();
			participantResyncPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantResyncPasswordLandscapeConfig.getVal()));
			participantResyncUserNameLandscapeConfig.persist();
			participantResyncPasswordLandscapeConfig.persist();
		}
	}

	@Transactional
	public void createDirections(Landscape landscape,Participant participant){
		String forwardSwpDirectionDescription=ControllerConstants.TFTOSWP_DIRECTION_DESCRIPTION;
		String reverseSwpDirectionDescription=ControllerConstants.SWPTOTFDIRECTIONDESCRIPTION;
		String forwardQcDirectionDescription=ControllerConstants.TFTOQCDIRECTIONDESCRIPTION;
		String reverseQcDirectionDescription=ControllerConstants.QCTOTFDIRECTIONDESCRIPTION;
		Direction forwardDirection=new Direction();
		if(participant.getSystemKind().equals(SystemKind.QC)){
			forwardDirection.setDescription(forwardQcDirectionDescription);
		}
		else{
			forwardDirection.setDescription(forwardSwpDirectionDescription);
		}
		forwardDirection.setDirection(Directions.FORWARD);
		forwardDirection.setLandscape(landscape);
		forwardDirection.setShouldStartAutomatically(false);
		forwardDirection.persist();

		Direction reverseDirection=new Direction();
		if(participant.getSystemKind().equals(SystemKind.QC)){
			reverseDirection.setDescription(reverseQcDirectionDescription);
		}
		else{
			reverseDirection.setDescription(reverseSwpDirectionDescription);
		}
		reverseDirection.setDirection(Directions.REVERSE);
		reverseDirection.setLandscape(landscape);
		reverseDirection.setShouldStartAutomatically(false);
		reverseDirection.persist();
		createDirectionConfigs(forwardDirection,reverseDirection,participant);
	}

	@Transactional
	public void createDirectionConfigs(Direction forwardDirection,Direction reverseDirection, Participant participant){

		String directionConfigdefaultVal=ControllerConstants.DEFAULTLOGTEMPLATE;		
		Pattern pat = Pattern.compile("[>]"); 
		Matcher mat = pat.matcher(directionConfigdefaultVal);
		String newval=mat.replaceAll(">\r\n\n"); 

		if(participant.getSystemKind().equals(SystemKind.QC)){
			DirectionConfig directionConfigLogTemplateQCtoTF=new DirectionConfig();
			DirectionConfig directionConfigLogTemplateTFtoQC=new DirectionConfig();
			DirectionConfig directionConfigQCMaxSize=new DirectionConfig();

			directionConfigLogTemplateTFtoQC.setDirection(forwardDirection);
			directionConfigLogTemplateQCtoTF.setDirection(reverseDirection);
			directionConfigLogTemplateQCtoTF.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateTFtoQC.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateQCtoTF.setVal(newval);
			directionConfigLogTemplateTFtoQC.setVal(newval);
			directionConfigQCMaxSize.setDirection(reverseDirection);
			directionConfigQCMaxSize.setName(ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE);
			directionConfigQCMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());

			directionConfigLogTemplateQCtoTF.persist();
			directionConfigLogTemplateTFtoQC.persist();
			directionConfigQCMaxSize.persist();
		}

		if(participant.getSystemKind().equals(SystemKind.SWP)){

			DirectionConfig directionConfigLogTemplateSWPtoTF=new DirectionConfig();
			DirectionConfig directionConfigLogTemplateTFtoSWP=new DirectionConfig();
			DirectionConfig directionConfigSWPMaxSize=new DirectionConfig();

			directionConfigLogTemplateTFtoSWP.setDirection(forwardDirection);
			directionConfigLogTemplateSWPtoTF.setDirection(reverseDirection);
			directionConfigLogTemplateTFtoSWP.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateSWPtoTF.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateTFtoSWP.setVal(newval);
			directionConfigLogTemplateSWPtoTF.setVal(newval);
			directionConfigSWPMaxSize.setDirection(reverseDirection);
			directionConfigSWPMaxSize.setName(ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE);
			directionConfigSWPMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());

			directionConfigLogTemplateTFtoSWP.persist();
			directionConfigLogTemplateSWPtoTF.persist();
			directionConfigSWPMaxSize.persist();
		}

		DirectionConfig directionConfigTFMaxSize=new DirectionConfig();
		directionConfigTFMaxSize.setDirection(forwardDirection);
		directionConfigTFMaxSize.setName(ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE);
		directionConfigTFMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());
		directionConfigTFMaxSize.persist();
	}




	public boolean verifyEntities(Landscape landscape,Model model,RequestContext context) {
		Participant participant=landscape.getParticipant();
		if(verifyDirectionEntities(landscape,model,context)){
			Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
			if(participant.getSystemKind().equals(SystemKind.QC)){
				if(verifyQCEntities(landscape,reverseDirection,model,context)){
					return false;
				}
			}
			else if(participant.getSystemKind().equals(SystemKind.SWP)){
				if(verifySWPEntities(landscape,reverseDirection,model,context)){
					return false;
				}
			}	
		}
		return true;
	}

	public boolean verifyDirectionEntities(Landscape landscape,Model model,RequestContext context){
		if(Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getResultList().size()!=0 &&
				Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getResultList().size()!=0){
			Direction reverseDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.REVERSE).getSingleResult();
			Direction forwardDirection=Direction.findDirectionsByLandscapeEqualsAndDirectionEquals(landscape, Directions.FORWARD).getSingleResult();
			if(DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE).getResultList().size()!=0&&
					DirectionConfig.findDirectionConfigsByDirectionAndName(forwardDirection,ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE).getResultList().size()!=0&&
					DirectionConfig.findDirectionConfigsByDirectionAndName(forwardDirection,ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE).getResultList().size()!=0){
				return true;
			}
			else{
				model.addAttribute("errormessage",context.getMessage(ControllerConstants.DIRECTION_CONFIG_ERROR_MESSAGE));
				return false;

			}
		}
		else{
			model.addAttribute("errormessage",context.getMessage(ControllerConstants.DIRECTION_ERROR_MESSAGE));
			return false;
		}
	}
	public boolean verifyQCEntities(Landscape landscape,Direction reverseDirection,Model model,RequestContext context){
		if(ParticipantConfig.findParticipantConfigsByParticipantAndName(landscape.getParticipant(), ControllerConstants.CCF_PARTICIPANT_QC_URL).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_QC_USERNAME).getResultList().size()!=0&&
				DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE).getResultList().size()!=0){
			return true;
		}
		else{
			model.addAttribute("errormessage",context.getMessage(ControllerConstants.QUALITY_CENTER_ERROR_MESSAGE));
			return false;
		}
	}


	public boolean verifySWPEntities(Landscape landscape, Direction reverseDirection,Model model,RequestContext context){
		if(ParticipantConfig.findParticipantConfigsByParticipantAndName(landscape.getParticipant(), ControllerConstants.CCF_PARTICIPANT_SWP_URL).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_USERNAME).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_PASSWORD).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape, ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_USERNAME).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_SWP_RESYNC_PASSWORD).getResultList().size()!=0 &&
				DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE).getResultList().size()!=0){
			return true;
		}
		else{
			model.addAttribute("errormessage",context.getMessage(ControllerConstants.SCRUM_WORKS_PRO_ERROR_MESSAGE));
			return false; 
		}
	}

	public boolean verifyTFEntities(Landscape landscape,Model model,RequestContext context){
		if(LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_TF_USERNAME).getResultList().size()!=0 &&
				LandscapeConfig.findLandscapeConfigsByLandscapeAndName(landscape,ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD).getResultList().size()!=0 ){
			return false;
		}
		else{
			model.addAttribute("errormessage",context.getMessage(ControllerConstants.TEAMFORGE_ERROR_MESSAGE)); 
			return true;
		}
	}

}
