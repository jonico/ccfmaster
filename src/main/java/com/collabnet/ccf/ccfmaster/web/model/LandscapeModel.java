package com.collabnet.ccf.ccfmaster.web.model;



import java.net.MalformedURLException;
import java.net.URL;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.google.common.base.Strings;

public class LandscapeModel extends AbstractGenericParticipantModel {
	 
	private static final Logger log = LoggerFactory.getLogger(LandscapeModel.class);

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
	

	public void normalizeParticipantUrl() {
		if (participantUrlParticipantConfig == null ||
			Strings.isNullOrEmpty(participantUrlParticipantConfig.getVal()) ||
			participant == null) {
			// do nothing.
			return;
		}
		switch (participant.getSystemKind()) {
		case SWP:
			participantUrlParticipantConfig.setVal(normalizeSwpUrl(participantUrlParticipantConfig.getVal()));
			break;
		case QC:
			participantUrlParticipantConfig.setVal(normalizeQcUrl(participantUrlParticipantConfig.getVal()));
			break;
		default:
			log.warn("unexpected system kind ({}) specified for participant, ignoring.", participant.getSystemKind());
			break;
		}
	}
	
	static String normalizeSwpUrl(String swpUrl) {
		return normalizeUrl(swpUrl, "/scrumworks-api/api2/scrumworks?wsdl");
	}

	static String normalizeQcUrl(String qcUrl) {
		return normalizeUrl(qcUrl, "/qcbin/");
	}


	/**
	 * Normalizes <code>participantUrl</code> to have <code>defaultPath</code> as the path component if none specified.
	 * If <code>participantUrl</code> has a path component or is not a valid URL, it is returned unmodified.
	 * 
	 * @param participantUrl
	 * @param defaultPath
	 * @return the normalized URL.
	 */
	static String normalizeUrl(final String participantUrl,
			final String defaultPath) {
		String res = participantUrl;
		try {
			URL url = new URL(res);
			final String path = url.getPath();
			if (path.isEmpty() || "/".equals(path)) {
				res = (new URL(url, defaultPath)).toString();
			}
		} catch (MalformedURLException ignored) {} 
		return res;
	}

	@Valid
	private Landscape landscape=new Landscape();
	
//	@Valid
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
