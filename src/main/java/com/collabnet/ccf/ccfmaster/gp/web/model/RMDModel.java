package com.collabnet.ccf.ccfmaster.gp.web.model;


public class RMDModel {
	
	public String direction ;
	
	public String forwardConfilictPolicies;
	
	public String reversedConfilictPolicies;
	
	public String teamForgeMappingType; //planningfolder,tracker,metadata
	
	public String teamforgeProjectId;
	
	public String teamforgeTracker;
	
	public String participantMappingType;
	
	public String participantDomainName;
	
	public String participantProjectId;
	
	public String[] participantMappingTypes;
	
	public String[] participantDomainNames;
	
	public String[] participantProjectIds;

	public String getDirection() {
		return direction;
	}

	public String getForwardConfilictPolicies() {
		return forwardConfilictPolicies;
	}

	public String getReversedConfilictPolicies() {
		return reversedConfilictPolicies;
	}

	public String getTeamForgeMappingType() {
		return teamForgeMappingType;
	}

	public String getTeamforgeProjectId() {
		return teamforgeProjectId;
	}

	public String getTeamforgeTracker() {
		return teamforgeTracker;
	}

	public String getParticipantMappingType() {
		return participantMappingType;
	}

	public String getParticipantDomainName() {
		return participantDomainName;
	}

	public String getParticipantProjectId() {
		return participantProjectId;
	}

	public String[] getParticipantMappingTypes() {
		return participantMappingTypes;
	}

	public String[] getParticipantDomainNames() {
		return participantDomainNames;
	}

	public String[] getParticipantProjectIds() {
		return participantProjectIds;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setForwardConfilictPolicies(String forwardConfilictPolicies) {
		this.forwardConfilictPolicies = forwardConfilictPolicies;
	}

	public void setReversedConfilictPolicies(String reversedConfilictPolicies) {
		this.reversedConfilictPolicies = reversedConfilictPolicies;
	}

	public void setTeamForgeMappingType(String teamForgeMappingType) {
		this.teamForgeMappingType = teamForgeMappingType;
	}

	public void setTeamforgeProjectId(String teamforgeProjectId) {
		this.teamforgeProjectId = teamforgeProjectId;
	}

	public void setTeamforgeTracker(String teamforgeTracker) {
		this.teamforgeTracker = teamforgeTracker;
	}

	public void setParticipantMappingType(String participantMappingType) {
		this.participantMappingType = participantMappingType;
	}

	public void setParticipantDomainName(String participantDomainName) {
		this.participantDomainName = participantDomainName;
	}

	public void setParticipantProjectId(String participantProjectId) {
		this.participantProjectId = participantProjectId;
	}

	public void setParticipantMappingTypes(String[] participantMappingTypes) {
		this.participantMappingTypes = participantMappingTypes;
	}

	public void setParticipantDomainNames(String[] participantDomainNames) {
		this.participantDomainNames = participantDomainNames;
	}

	public void setParticipantProjectIds(String[] participantProjectIds) {
		this.participantProjectIds = participantProjectIds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RepositoryMappingDirectionModel [direction=");
		builder.append(direction);
		builder.append(", forwardConfilictPolicies=");
		builder.append(forwardConfilictPolicies);
		builder.append(", reversedConfilictPolicies=");
		builder.append(reversedConfilictPolicies);
		builder.append(", teamForgeMappingType=");
		builder.append(teamForgeMappingType);
		builder.append(", teamforgeProjectId=");
		builder.append(teamforgeProjectId);
		builder.append(", teamforgeTracker=");
		builder.append(teamforgeTracker);
		builder.append(", participantMappingType=");
		builder.append(participantMappingType);
		builder.append(", participantDomainName=");
		builder.append(participantDomainName);
		builder.append(", participantProjectId=");
		builder.append(participantProjectId);
		builder.append("]");
		return builder.toString();
	}
	

}
