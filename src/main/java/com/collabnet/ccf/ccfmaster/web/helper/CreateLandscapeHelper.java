package com.collabnet.ccf.ccfmaster.web.helper;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.gp.model.GenericParticipantFacade;
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
import com.collabnet.ccf.core.utils.GenericParticipantUtils;

@Configurable
public class CreateLandscapeHelper {

	@Autowired 
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	
	@Autowired(required= false)
	public GenericParticipantFacade genericParticipant;


	/**
	 * Helper method to populate default values of participant and teamforge to create landscape
	 * 
	 */
	public void populateCreateLandscapeModel(Model model, Participant participant){

		Participant teamforge=new Participant();
		teamforge.setSystemId(ControllerConstants.TF);
		teamforge.setSystemKind(SystemKind.TF);
		teamforge.setDescription(ControllerConstants.TFDESCRIPTION);
		teamforge.setPrefix(SystemKind.TF.toString()); //prefix added
		if(participant.getSystemKind().equals(SystemKind.QC))
		{
			participant.setSystemId(ControllerConstants.QC);
			participant.setDescription(ControllerConstants.QCDESCRIPTION);
			participant.setPrefix(SystemKind.QC.toString());
		}
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			participant.setSystemId(ControllerConstants.SWP);
			participant.setDescription(ControllerConstants.SW_PDESCRIPTION);
			participant.setPrefix(SystemKind.SWP.toString());
		}
		if(participant.getSystemKind().equals(SystemKind.GENERIC)){ // need to move this code piece to switch case
			if(genericParticipant != null){
				participant.setSystemId(genericParticipant.getPrefix());
				participant.setDescription(genericParticipant.getName());
				participant.setPrefix(genericParticipant.getPrefix());
			}
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
		tfUserNameLandscapeConfig.persist();
		tfPasswordLandscapeConfig.persist();
		tfParticipantUrlParticipantconfig.persist();
		if(participant.getSystemKind().equals(SystemKind.SWP)||participant.getSystemKind().equals(SystemKind.QC)){ // need to organize the loops
			urlParticipantconfig.persist();
			participantUserNameLandscapeConfig.persist();
			participantPasswordLandscapeConfig.persist();
		}

		//persist resync name and password for SWP
		if(participant.getSystemKind().equals(SystemKind.SWP)){
			LandscapeConfig participantResyncUserNameLandscapeConfig=landscapemodel.getParticipantResyncUserNameLandscapeConfig();
			LandscapeConfig participantResyncPasswordLandscapeConfig=landscapemodel.getParticipantResyncPasswordLandscapeConfig();
			participantResyncPasswordLandscapeConfig.setVal(Obfuscator.encodePassword(participantResyncPasswordLandscapeConfig.getVal()));
			participantResyncUserNameLandscapeConfig.persist();
			participantResyncPasswordLandscapeConfig.persist();
		}
		if(participant.getSystemKind().equals(SystemKind.GENERIC)){
			List<LandscapeConfig> landscapeCollection = GenericParticipantUtils.buildLandscapeConfig(landscapemodel.getLandscapeConfigList());
			List<ParticipantConfig> participantCollection =  GenericParticipantUtils.buildParticipantConfig(landscapemodel.getParticipantConfigList());
			for(LandscapeConfig config:landscapeCollection){
				config.setLandscape(landscape);
				config.persist();
			}
			for(ParticipantConfig participantConfig:participantCollection){
				participantConfig.setParticipant(participant);
				participantConfig.persist();
			}
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
		else if(participant.getSystemKind().equals(SystemKind.SWP)){
			forwardDirection.setDescription(forwardSwpDirectionDescription);
		}else{
			forwardDirection.setDescription("TF-GENERIC direction"); //hard coding 
		}
		forwardDirection.setDirection(Directions.FORWARD);
		forwardDirection.setLandscape(landscape);
		forwardDirection.setShouldStartAutomatically(false);
		forwardDirection.persist();

		Direction reverseDirection=new Direction();
		if(participant.getSystemKind().equals(SystemKind.QC)){
			reverseDirection.setDescription(reverseQcDirectionDescription);
		}
		else if(participant.getSystemKind().equals(SystemKind.SWP)){
			reverseDirection.setDescription(reverseSwpDirectionDescription);
		}else{
			reverseDirection.setDescription("GENERIC-TF direction");//hard coding
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

			directionConfigLogTemplateTFtoQC.setDirection(forwardDirection);
			directionConfigLogTemplateQCtoTF.setDirection(reverseDirection);
			directionConfigLogTemplateQCtoTF.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateTFtoQC.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateQCtoTF.setVal(newval);
			directionConfigLogTemplateTFtoQC.setVal(newval);

			directionConfigLogTemplateQCtoTF.persist();
			directionConfigLogTemplateTFtoQC.persist();
			
			if(!isQCMaxAttachmentExist(reverseDirection)) {
				DirectionConfig directionConfigQCMaxSize=new DirectionConfig();
				directionConfigQCMaxSize.setDirection(reverseDirection);
				directionConfigQCMaxSize.setName(ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE);
				directionConfigQCMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());
				directionConfigQCMaxSize.persist();
			}
		}

		if(participant.getSystemKind().equals(SystemKind.SWP)){

			DirectionConfig directionConfigLogTemplateSWPtoTF=new DirectionConfig();
			DirectionConfig directionConfigLogTemplateTFtoSWP=new DirectionConfig();

			directionConfigLogTemplateTFtoSWP.setDirection(forwardDirection);
			directionConfigLogTemplateSWPtoTF.setDirection(reverseDirection);
			directionConfigLogTemplateTFtoSWP.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateSWPtoTF.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateTFtoSWP.setVal(newval);
			directionConfigLogTemplateSWPtoTF.setVal(newval);

			directionConfigLogTemplateTFtoSWP.persist();
			directionConfigLogTemplateSWPtoTF.persist();
			
			if(!isSWPMaxAttachmentExist(reverseDirection)) {
				DirectionConfig directionConfigSWPMaxSize=new DirectionConfig();
				directionConfigSWPMaxSize.setDirection(reverseDirection);
				directionConfigSWPMaxSize.setName(ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE);
				directionConfigSWPMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());
				directionConfigSWPMaxSize.persist();
			}
		}
		
		if(participant.getSystemKind().equals(SystemKind.GENERIC)){
			
			DirectionConfig directionConfigLogTemplateGenerictoTF=new DirectionConfig(); // repeated code need to refactor later :'(
			DirectionConfig directionConfigLogTemplateTFtoGeneric=new DirectionConfig();

			directionConfigLogTemplateTFtoGeneric.setDirection(forwardDirection);
			directionConfigLogTemplateGenerictoTF.setDirection(reverseDirection);
			directionConfigLogTemplateTFtoGeneric.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateGenerictoTF.setName(ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE);
			directionConfigLogTemplateTFtoGeneric.setVal(newval);
			directionConfigLogTemplateGenerictoTF.setVal(newval);

			directionConfigLogTemplateTFtoGeneric.persist();
			directionConfigLogTemplateGenerictoTF.persist();
			
			// Creation for attachment size should be proper... that is handling genericparticipantloader(pass from contoller or have it is has instance variable)
			if(genericParticipant != null){
				DirectionConfig directionConfigTFMaxSize = new DirectionConfig(); 
				String configName = String.format("ccf.direction.%s.max.attachmentsize", genericParticipant.getPrefix());
				directionConfigTFMaxSize.setDirection(reverseDirection);
				directionConfigTFMaxSize.setName(configName);
				directionConfigTFMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());
				directionConfigTFMaxSize.persist();
			}
			
		}
		
		if (!isTFMaxAttachmentExist(forwardDirection)) {
			DirectionConfig directionConfigTFMaxSize = new DirectionConfig();
			directionConfigTFMaxSize.setDirection(forwardDirection);
			directionConfigTFMaxSize.setName(ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE);
			directionConfigTFMaxSize.setVal(ccfRuntimePropertyHolder.getMaxAttachmentSize());
			directionConfigTFMaxSize.persist();
		}
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
			else if(participant.getSystemKind().equals(SystemKind.GENERIC)){
				return false; //TODO: need to handle validation for generic participant
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
					isTFMaxAttachmentExist(forwardDirection)){
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
				isQCMaxAttachmentExist(reverseDirection)){
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
				isSWPMaxAttachmentExist(reverseDirection)){
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
	
	public static boolean isSWPMaxAttachmentExist(Direction reverseDirection) {
		return DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE).getResultList().size()!=0;
	}
	
	public static boolean isQCMaxAttachmentExist(Direction reverseDirection) {
		return DirectionConfig.findDirectionConfigsByDirectionAndName(reverseDirection,ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE).getResultList().size()!=0;
	}
	
	public static boolean isTFMaxAttachmentExist(Direction forwardDirection) {
		return DirectionConfig.findDirectionConfigsByDirectionAndName(forwardDirection,ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE).getResultList().size()!=0;
	}

}
