package com.collabnet.ccf.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;

/**
 * GenericParticipantUtils provides util methods to handle participant to config objects
 * 
 * @author kbalaji
 *
 */
public class GenericParticipantUtils {
	
	private static final String PASSWORD_PROPERTY_TYPE = "PASSWORD";
	
	private GenericParticipantUtils(){ }

	public static List<LandscapeConfig> buildLandscapeConfig(List<CCFCoreProperty> landscapeConfigList) {
		List<LandscapeConfig> configCollection = new ArrayList<LandscapeConfig>();
		for (CCFCoreProperty property : landscapeConfigList) {
			LandscapeConfig config = new LandscapeConfig();
			config.setName(property.getName());
			if(PASSWORD_PROPERTY_TYPE.equals(property.getType().toString())) {
				config.setVal(Obfuscator.encodePassword(property.getValue()));
			}
			else {
				config.setVal(property.getValue());
			}
			configCollection.add(config);
		}
		return configCollection;
	}
	
	public static List<ParticipantConfig> buildParticipantConfig(List<CCFCoreProperty> participantConfigList) {
		List<ParticipantConfig> configCollection = new ArrayList<ParticipantConfig>();
		for (CCFCoreProperty property : participantConfigList) {
			ParticipantConfig config = new ParticipantConfig();
			config.setName(property.getName());
			config.setVal(property.getValue());
			configCollection.add(config);
		}
		return configCollection;
	}
	
	public static List<DirectionConfig> buildDirectionConfig(List<CCFCoreProperty> directionConfigList){
		List<DirectionConfig> configCollection = new ArrayList<DirectionConfig>();
		for (CCFCoreProperty property : directionConfigList) {
			DirectionConfig config = new DirectionConfig();
			config.setName(property.getName());
			config.setVal(property.getValue());
			configCollection.add(config);
		}
		return configCollection;
	}
	

}
