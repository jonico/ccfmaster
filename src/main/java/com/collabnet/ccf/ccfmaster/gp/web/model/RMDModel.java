package com.collabnet.ccf.ccfmaster.gp.web.model;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;


public class RMDModel {
	
	public String direction ;
	
	public String forwardConfilictPolicies;
	
	public String reversedConfilictPolicies;
	
	public String forwardFieldMappingTemplateName;
	
	public String reverseFieldMappingTemplateName;
	
	public String teamForgeMappingType; //planningfolder,tracker,metadata
	
	public String teamforgeProjectId;
	
	public String teamforgeTracker;
	
	public List<CCFCoreProperty> participantSelectorFieldList;

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

	public String getForwardFieldMappingTemplateName() {
		return forwardFieldMappingTemplateName;
	}

	public String getReverseFieldMappingTemplateName() {
		return reverseFieldMappingTemplateName;
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

	public void setForwardFieldMappingTemplateName(String forwardFieldMappingTemplateName) {
		this.forwardFieldMappingTemplateName = forwardFieldMappingTemplateName;
	}

	public void setReverseFieldMappingTemplateName(String reverseFieldMappingTemplateName) {
		this.reverseFieldMappingTemplateName = reverseFieldMappingTemplateName;
	}

	public List<CCFCoreProperty> getParticipantSelectorFieldList() {
		return participantSelectorFieldList;
	}

	public void setParticipantSelectorFieldList(List<CCFCoreProperty> participantSelectorFieldList) {
		this.participantSelectorFieldList = participantSelectorFieldList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RMDModel [direction=");
		builder.append(direction);
		builder.append(", forwardConfilictPolicies=");
		builder.append(forwardConfilictPolicies);
		builder.append(", reversedConfilictPolicies=");
		builder.append(reversedConfilictPolicies);
		builder.append(", forwardFieldMappingTemplateName=");
		builder.append(forwardFieldMappingTemplateName);
		builder.append(", reverseFieldMappingTemplateName=");
		builder.append(reverseFieldMappingTemplateName);
		builder.append(", teamForgeMappingType=");
		builder.append(teamForgeMappingType);
		builder.append(", teamforgeProjectId=");
		builder.append(teamforgeProjectId);
		builder.append(", teamforgeTracker=");
		builder.append(teamforgeTracker);
		builder.append(", participantSelectorFields=");
		builder.append(participantSelectorFieldList);
		builder.append("]");
		return builder.toString();
	}
	

}
