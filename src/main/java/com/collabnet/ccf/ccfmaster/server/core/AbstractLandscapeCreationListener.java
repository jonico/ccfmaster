package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

/**
 * no-op implementation of {@link LandscapeCreationListener} that prints a log message at trace level.
 */
public abstract class AbstractLandscapeCreationListener implements
		LandscapeCreationListener {
	
	private static Logger log = LoggerFactory.getLogger(AbstractLandscapeCreationListener.class);

	@Override
	public Landscape beforeCreate(Landscape landscape) {
		log.trace("beforeCreate: %s", landscape);
		return landscape;
	}

	@Override
	public void afterCreate(Landscape landscape) {
		log.trace("afterCreate: %s", landscape);
		// do nothing.
	}

	@Override
	public void onException(Exception e) {
		log.trace("onException: %s", e);
		// do nothing.
	}

}
