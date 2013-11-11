package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

/**
 * no-op implementation of {@link LandscapeCreationListener} that prints a log
 * message at trace level.
 */
public abstract class AbstractLandscapeModificationListener implements LandscapeModificationListener {

    private static Logger log = LoggerFactory
                                      .getLogger(AbstractLandscapeModificationListener.class);

    @Override
    public Landscape beforeMerge(Landscape landscape) {
        log.trace("beforeCreate: %s", landscape);
        return landscape;
    }

    @Override
    public void afterMerge(Landscape landscape) {
        log.trace("afterCreate: %s", landscape);
        // do nothing.
    }

    @Override
    public void onException(Exception e) {
        log.trace("onException: %s", e);
        // do nothing.
    }

}
