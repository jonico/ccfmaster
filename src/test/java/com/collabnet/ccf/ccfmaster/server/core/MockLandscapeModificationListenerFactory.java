package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class MockLandscapeModificationListenerFactory implements LandscapeModificationListenerFactory {
    private static Logger log = LoggerFactory
                                      .getLogger(MockLandscapeModificationListenerFactory.class);

    @Override
    public LandscapeModificationListener get() {
        return new LandscapeModificationListener() {
            @Override
            public void afterMerge(Landscape landscape) {
                log.trace("called afterMerge({})", landscape);
            }

            @Override
            public Landscape beforeMerge(Landscape landscape) {
                log.trace("called beforeMerge({})", landscape);
                return landscape;
            }

            @Override
            public void onException(Exception e) {
                log.trace("called onException({})", e);
            }
        };
    }

}
