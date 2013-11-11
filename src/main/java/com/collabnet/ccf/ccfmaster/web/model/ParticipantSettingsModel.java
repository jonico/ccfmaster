package com.collabnet.ccf.ccfmaster.web.model;

import javax.validation.Valid;

import com.collabnet.ccf.ccfmaster.gp.web.model.AbstractGenericParticipantModel;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

public class ParticipantSettingsModel extends AbstractGenericParticipantModel {

    @Valid
    private Participant       participant                              = new Participant();

    private Landscape         landscape                                = new Landscape();

    private ParticipantConfig participantUrlParticipantConfig          = new ParticipantConfig();

    //	@Valid
    private LandscapeConfig   participantUserNameLandscapeConfig       = new LandscapeConfig();

    private LandscapeConfig   participantPasswordLandscapeConfig       = new LandscapeConfig();

    private LandscapeConfig   participantResyncUserNameLandscapeConfig = new LandscapeConfig();

    private LandscapeConfig   participantResyncPasswordLandscapeConfig = new LandscapeConfig();

    public ParticipantSettingsModel() {
        participantUrlParticipantConfig.setParticipant(participant);
        participantUserNameLandscapeConfig.setLandscape(landscape);
        participantPasswordLandscapeConfig.setLandscape(landscape);
        participantResyncUserNameLandscapeConfig.setLandscape(landscape);
        participantResyncPasswordLandscapeConfig.setLandscape(landscape);
    }

    public Landscape getLandscape() {
        return landscape;
    }

    public Participant getParticipant() {
        return participant;
    }

    public LandscapeConfig getParticipantPasswordLandscapeConfig() {
        return participantPasswordLandscapeConfig;
    }

    public LandscapeConfig getParticipantResyncPasswordLandscapeConfig() {
        return participantResyncPasswordLandscapeConfig;
    }

    public LandscapeConfig getParticipantResyncUserNameLandscapeConfig() {
        return participantResyncUserNameLandscapeConfig;
    }

    public ParticipantConfig getParticipantUrlParticipantConfig() {
        return participantUrlParticipantConfig;
    }

    public LandscapeConfig getParticipantUserNameLandscapeConfig() {
        return participantUserNameLandscapeConfig;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setParticipantPasswordLandscapeConfig(
            LandscapeConfig participantPasswordLandscapeConfig) {
        this.participantPasswordLandscapeConfig = participantPasswordLandscapeConfig;
    }

    public void setParticipantResyncPasswordLandscapeConfig(
            LandscapeConfig participantResyncPasswordLandscapeConfig) {
        this.participantResyncPasswordLandscapeConfig = participantResyncPasswordLandscapeConfig;
    }

    public void setParticipantResyncUserNameLandscapeConfig(
            LandscapeConfig participantResyncUserNameLandscapeConfig) {
        this.participantResyncUserNameLandscapeConfig = participantResyncUserNameLandscapeConfig;
    }

    public void setParticipantUrlParticipantConfig(
            ParticipantConfig participantUrlParticipantConfig) {
        this.participantUrlParticipantConfig = participantUrlParticipantConfig;
    }

    public void setParticipantUserNameLandscapeConfig(
            LandscapeConfig participantUserNameLandscapeConfig) {
        this.participantUserNameLandscapeConfig = participantUserNameLandscapeConfig;
    }

}
