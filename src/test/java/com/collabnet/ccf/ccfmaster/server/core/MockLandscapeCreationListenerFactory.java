package com.collabnet.ccf.ccfmaster.server.core;

public class MockLandscapeCreationListenerFactory implements LandscapeCreationListenerFactory {

    @Override
    public LandscapeCreationListener get() {
        return new AbstractLandscapeCreationListener() {
        };
    }

}
