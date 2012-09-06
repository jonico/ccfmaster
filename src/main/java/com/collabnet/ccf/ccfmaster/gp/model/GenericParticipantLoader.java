package com.collabnet.ccf.ccfmaster.gp.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.core.utils.SerializationUtil;

/**
 * GenericParticipantLoader loads the GenericParticipant configuration information
 * This loading happens at runtime of the server i.e. while statup of the server
 * 
 * @author kbalaji
 *
 */
public class GenericParticipantLoader {

	private String configLocation;

	private GenericParticipant genericParticipant;
	
	public GenericParticipant getGenericParticipant() {
		return genericParticipant;
	}

	void setGenericParticipant(GenericParticipant genericParticipant) {
		this.genericParticipant = genericParticipant;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void initLoader() {
		if (configLocation != null) {
			try {
				String filePath = String.format("%s/genericParticipant.xml", configLocation);
				File configFile = new File(filePath);
				if(configFile.exists()){
					GenericParticipant participant = SerializationUtil.deSerialize(configFile, GenericParticipant.class);
					if(participant != null)
						setGenericParticipant(participant);
				}
			} catch (JAXBException e) {
				// TODO: need to handle it properly
			} catch (IOException e) {
				// TODO: need to handle it properly
			}
		}
	}
	
	public static List<LandscapeConfig> buildLandscapeConfig(List<CCFCoreProperty> landscapeConfigList) {
		List<LandscapeConfig> configCollection = new ArrayList<LandscapeConfig>();
		for (CCFCoreProperty property : landscapeConfigList) {// null validation check
			LandscapeConfig config = new LandscapeConfig();
			config.setName(property.getName());
			config.setVal(property.getValue());
			configCollection.add(config);
		}
		return configCollection;
	}
	
	public static List<ParticipantConfig> buildParticipantConfig(List<CCFCoreProperty> participantConfigList) {
		List<ParticipantConfig> configCollection = new ArrayList<ParticipantConfig>();
		for (CCFCoreProperty property : participantConfigList) { // null validation check
			ParticipantConfig config = new ParticipantConfig();
			config.setName(property.getName());
			config.setVal(property.getValue());
			configCollection.add(config);
		}
		return configCollection;
	}
	
	public static List<DirectionConfig> buildDirectionConfig(List<CCFCoreProperty> directionConfigList){
		List<DirectionConfig> configCollection = new ArrayList<DirectionConfig>();
		for (CCFCoreProperty property : directionConfigList) { // null validation check
			DirectionConfig config = new DirectionConfig();
			config.setName(property.getName());
			config.setVal(property.getValue());
			configCollection.add(config);
		}
		return configCollection;
	}
	

}
