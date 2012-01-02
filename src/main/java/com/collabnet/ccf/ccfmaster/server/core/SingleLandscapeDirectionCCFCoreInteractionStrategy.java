package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Configurable;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

/**
 * This strategy is responsible to create a properties file with the
 * characteristics of the direction update this property file during direction
 * updates
 * 
 * @author jnicolai
 * 
 */
@Configurable
public class SingleLandscapeDirectionCCFCoreInteractionStrategy extends
		DirectionCCFCoreInteractionStrategy {

	@Override
	public void create(Direction context) {
		File ccfLandscapeDirectory = new File(getCcfHome(), "landscape"
				+ context.getLandscape().getId());
		try {
			FileUtils.forceMkdir(ccfLandscapeDirectory);
		} catch (IOException e) {
			throw new CoreConfigurationException("Could not create directory to write wrapper properties: " + e.getMessage(), e);
		}
		File directionWrapperFile = new File(ccfLandscapeDirectory,
				getImmutableDirectionWrapperFileName(context));

		createProperties(context, directionWrapperFile);
	}

	@Override
	public void delete(Direction context) {
		// do nothing so far
	}

	@Override
	public synchronized void update(Direction context) {
		File ccfLandscapeDirectory = new File(getCcfHome(), "landscape"
				+ context.getLandscape().getId());
		File directionWrapperFile = new File(ccfLandscapeDirectory,
				getImmutableDirectionWrapperFileName(context));

		updateProperties(context, directionWrapperFile);
	}

	public String getImmutableDirectionWrapperFileName(Direction direction) {
		String baseName = "immutable";
		switch (direction.getDirection()) {
		case FORWARD:
			baseName += "%1$s2%2$s"; // e.g. TF2QC
			break;
		case REVERSE:
			baseName += "%2$s2%1$s"; // e.g. QC2TF
			break;
		default:
			throw new IllegalArgumentException("unknown direction: "
					+ direction.getDirection());
		}
		final Landscape landscape = direction.getLandscape();
		return String.format(baseName + ".conf",
				landscape.getTeamForge().getSystemKind(),
				landscape.getParticipant().getSystemKind()).toLowerCase();
	}
}
