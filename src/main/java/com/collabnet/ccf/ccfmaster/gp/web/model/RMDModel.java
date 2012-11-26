package com.collabnet.ccf.ccfmaster.gp.web.model;

import java.util.List;
import java.util.Map;

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
	
	public Map<String,String> participantConfigMap;
	
	public Map<String,String> landscapeConfigMap;

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

	public Map<String, String> getParticipantConfigMap() {
		return participantConfigMap;
	}

	public Map<String, String> getLandscapeConfigMap() {
		return landscapeConfigMap;
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

	public void setParticipantConfigMap(Map<String, String> participantConfigMap) {
		this.participantConfigMap = participantConfigMap;
	}

	public void setLandscapeConfigMap(Map<String, String> landscapeConfigMap) {
		this.landscapeConfigMap = landscapeConfigMap;
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
		builder.append(", participantSelectorFieldList=");
		builder.append(participantSelectorFieldList);
		builder.append(", participantConfigMap=");
		builder.append(participantConfigMap);
		builder.append(", landscapeConfigMap=");
		builder.append(landscapeConfigMap);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result	+ ((forwardConfilictPolicies == null) ? 0 : forwardConfilictPolicies.hashCode());
		result = prime * result	+ ((forwardFieldMappingTemplateName == null) ? 0 : forwardFieldMappingTemplateName.hashCode());
		result = prime * result	+ ((landscapeConfigMap == null) ? 0 : landscapeConfigMap.hashCode());
		result = prime * result	+ ((participantConfigMap == null) ? 0 : participantConfigMap.hashCode());
		result = prime * result	+ ((participantSelectorFieldList == null) ? 0 : participantSelectorFieldList.hashCode());
		result = prime * result	+ ((reverseFieldMappingTemplateName == null) ? 0 : reverseFieldMappingTemplateName.hashCode());
		result = prime * result	+ ((reversedConfilictPolicies == null) ? 0 : reversedConfilictPolicies.hashCode());
		result = prime * result	+ ((teamForgeMappingType == null) ? 0 : teamForgeMappingType.hashCode());
		result = prime * result	+ ((teamforgeProjectId == null) ? 0 : teamforgeProjectId.hashCode());
		result = prime * result	+ ((teamforgeTracker == null) ? 0 : teamforgeTracker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RMDModel other = (RMDModel) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (forwardConfilictPolicies == null) {
			if (other.forwardConfilictPolicies != null)
				return false;
		} else if (!forwardConfilictPolicies
				.equals(other.forwardConfilictPolicies))
			return false;
		if (forwardFieldMappingTemplateName == null) {
			if (other.forwardFieldMappingTemplateName != null)
				return false;
		} else if (!forwardFieldMappingTemplateName
				.equals(other.forwardFieldMappingTemplateName))
			return false;
		if (landscapeConfigMap == null) {
			if (other.landscapeConfigMap != null)
				return false;
		} else if (!landscapeConfigMap.equals(other.landscapeConfigMap))
			return false;
		if (participantConfigMap == null) {
			if (other.participantConfigMap != null)
				return false;
		} else if (!participantConfigMap.equals(other.participantConfigMap))
			return false;
		if (participantSelectorFieldList == null) {
			if (other.participantSelectorFieldList != null)
				return false;
		} else if (!participantSelectorFieldList
				.equals(other.participantSelectorFieldList))
			return false;
		if (reverseFieldMappingTemplateName == null) {
			if (other.reverseFieldMappingTemplateName != null)
				return false;
		} else if (!reverseFieldMappingTemplateName
				.equals(other.reverseFieldMappingTemplateName))
			return false;
		if (reversedConfilictPolicies == null) {
			if (other.reversedConfilictPolicies != null)
				return false;
		} else if (!reversedConfilictPolicies
				.equals(other.reversedConfilictPolicies))
			return false;
		if (teamForgeMappingType == null) {
			if (other.teamForgeMappingType != null)
				return false;
		} else if (!teamForgeMappingType.equals(other.teamForgeMappingType))
			return false;
		if (teamforgeProjectId == null) {
			if (other.teamforgeProjectId != null)
				return false;
		} else if (!teamforgeProjectId.equals(other.teamforgeProjectId))
			return false;
		if (teamforgeTracker == null) {
			if (other.teamforgeTracker != null)
				return false;
		} else if (!teamforgeTracker.equals(other.teamforgeTracker))
			return false;
		return true;
	}
	

}
