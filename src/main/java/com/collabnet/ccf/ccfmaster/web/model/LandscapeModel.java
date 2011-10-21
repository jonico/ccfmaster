package com.collabnet.ccf.ccfmaster.web.model;



import javax.validation.Valid;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

public class LandscapeModel {
	 
	public LandscapeModel(){
		landscape.setParticipant(participant);
		landscape.setTeamForge(teamforge);
		participantUrlParticipantConfig.setParticipant(participant);
		participantUserNameLandscapeConfig.setLandscape(landscape);
		participantPasswordLandscapeConfig.setLandscape(landscape);
		participantResyncUserNameLandscapeConfig.setLandscape(landscape);
		participantResyncPasswordLandscapeConfig.setLandscape(landscape);
		tfUserNameLandscapeConfig.setLandscape(landscape);
		tfPasswordLandscapeConfig.setLandscape(landscape);
	}

	@Valid
	private Landscape landscape=new Landscape();
	
	@Valid
	private LandscapeConfig participantUserNameLandscapeConfig=new LandscapeConfig();

	
	private LandscapeConfig participantPasswordLandscapeConfig=new LandscapeConfig();

	
	private LandscapeConfig participantResyncUserNameLandscapeConfig=new LandscapeConfig();

	
	private LandscapeConfig participantResyncPasswordLandscapeConfig=new LandscapeConfig();
	
	@Valid
	private LandscapeConfig tfUserNameLandscapeConfig=new LandscapeConfig();

	@Valid
	private LandscapeConfig tfPasswordLandscapeConfig=new LandscapeConfig();

	
	private ParticipantConfig participantUrlParticipantConfig=new ParticipantConfig();
	
	@Valid
	private Participant participant=new Participant(); 
	
	
	@Valid
	private Participant teamforge=new Participant();


	public Landscape getLandscape() {
		return landscape;
	}


	public void setLandscape(Landscape landscape) {
		this.landscape = landscape;
	}


	public LandscapeConfig getParticipantUserNameLandscapeConfig() {
		return participantUserNameLandscapeConfig;
	}


	public void setParticipantUserNameLandscapeConfig(
			LandscapeConfig participantUserNameLandscapeConfig) {
		this.participantUserNameLandscapeConfig = participantUserNameLandscapeConfig;
	}


	public LandscapeConfig getParticipantPasswordLandscapeConfig() {
		return participantPasswordLandscapeConfig;
	}


	public void setParticipantPasswordLandscapeConfig(
			LandscapeConfig participantPasswordLandscapeConfig) {
		this.participantPasswordLandscapeConfig = participantPasswordLandscapeConfig;
	}


	public LandscapeConfig getParticipantResyncUserNameLandscapeConfig() {
		return participantResyncUserNameLandscapeConfig;
	}


	public void setParticipantResyncUserNameLandscapeConfig(
			LandscapeConfig participantResyncUserNameLandscapeConfig) {
		this.participantResyncUserNameLandscapeConfig = participantResyncUserNameLandscapeConfig;
	}


	public LandscapeConfig getParticipantResyncPasswordLandscapeConfig() {
		return participantResyncPasswordLandscapeConfig;
	}


	public void setParticipantResyncPasswordLandscapeConfig(
			LandscapeConfig participantResyncPasswordLandscapeConfig) {
		this.participantResyncPasswordLandscapeConfig = participantResyncPasswordLandscapeConfig;
	}


	public LandscapeConfig getTfUserNameLandscapeConfig() {
		return tfUserNameLandscapeConfig;
	}


	public void setTfUserNameLandscapeConfig(
			LandscapeConfig tfUserNameLandscapeConfig) {
		this.tfUserNameLandscapeConfig = tfUserNameLandscapeConfig;
	}


	public LandscapeConfig getTfPasswordLandscapeConfig() {
		return tfPasswordLandscapeConfig;
	}


	public void setTfPasswordLandscapeConfig(
			LandscapeConfig tfPasswordLandscapeConfig) {
		this.tfPasswordLandscapeConfig = tfPasswordLandscapeConfig;
	}


	public ParticipantConfig getParticipantUrlParticipantConfig() {
		return participantUrlParticipantConfig;
	}


	public void setParticipantUrlParticipantConfig(
			ParticipantConfig participantUrlParticipantConfig) {
		this.participantUrlParticipantConfig = participantUrlParticipantConfig;
	}


	public Participant getParticipant() {
		return participant;
	}


	public void setParticipant(Participant participant) {
		this.participant = participant;
	}


	public Participant getTeamforge() {
		return teamforge;
	}


	public void setTeamforge(Participant teamforge) {
		this.teamforge = teamforge;
	} 
	
	
	
	
	
	
}
