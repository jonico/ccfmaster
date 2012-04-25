package com.collabnet.ccf.ccfmaster.web.model;


import javax.validation.Valid;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;


public class TFSettingsModel {

	
	
	@Valid
	private Participant teamforge=new Participant(); 

	
	private Landscape landscape=new Landscape();
	
	@Valid
	private  LandscapeConfig tfUserNameLandscapeConfig=new LandscapeConfig();

	@Valid
	private LandscapeConfig tfPasswordLandscapeConfig=new LandscapeConfig();



	public TFSettingsModel() {
		tfUserNameLandscapeConfig.setLandscape(landscape);
		tfPasswordLandscapeConfig.setLandscape(landscape);
	}
	
	public Participant getTeamforge() {
		return teamforge;
	}

	public void setTeamforge(Participant teamforge) {
		this.teamforge = teamforge;
	}


	public Landscape getLandscape() {
		return landscape;
	}


	public void setLandscape(Landscape landscape) {
		this.landscape = landscape;
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

}
