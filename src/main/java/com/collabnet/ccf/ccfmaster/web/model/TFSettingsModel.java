package com.collabnet.ccf.ccfmaster.web.model;

import javax.validation.Valid;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

public class TFSettingsModel {

    @Valid
    private Participant       teamforge                 = new Participant();

    private Landscape         landscape                 = new Landscape();

    private ParticipantConfig tfUrlParticipantConfig    = new ParticipantConfig();

    @Valid
    private LandscapeConfig   tfUserNameLandscapeConfig = new LandscapeConfig();

    @Valid
    private LandscapeConfig   tfPasswordLandscapeConfig = new LandscapeConfig();

    public TFSettingsModel() {
        tfUrlParticipantConfig.setParticipant(teamforge);
        tfUserNameLandscapeConfig.setLandscape(landscape);
        tfPasswordLandscapeConfig.setLandscape(landscape);
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public Participant getTeamforge() {
        return teamforge;
    }

    public LandscapeConfig getTfPasswordLandscapeConfig() {
        return tfPasswordLandscapeConfig;
    }

    public ParticipantConfig getTfUrlParticipantConfig() {
        return tfUrlParticipantConfig;
    }

    public LandscapeConfig getTfUserNameLandscapeConfig() {
        return tfUserNameLandscapeConfig;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public void setTeamforge(Participant teamforge) {
        this.teamforge = teamforge;
    }

    public void setTfPasswordLandscapeConfig(
            LandscapeConfig tfPasswordLandscapeConfig) {
        this.tfPasswordLandscapeConfig = tfPasswordLandscapeConfig;
    }

    public void setTfUrlParticipantConfig(
            ParticipantConfig tfUrlParticipantConfig) {
        this.tfUrlParticipantConfig = tfUrlParticipantConfig;
    }

    public void setTfUserNameLandscapeConfig(
            LandscapeConfig tfUserNameLandscapeConfig) {
        this.tfUserNameLandscapeConfig = tfUserNameLandscapeConfig;
    }

}
