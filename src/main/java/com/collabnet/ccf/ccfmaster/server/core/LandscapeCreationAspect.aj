package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public aspect LandscapeCreationAspect {
    @Autowired
    private transient LandscapeCreationListenerFactory Landscape.creationListenerFactory;
/*
    public void Landscape.setCreationListenerFactory(LandscapeCreationListenerFactory creationListenerFactory) {
        this.creationListenerFactory = creationListenerFactory;
    }

    public LandscapeCreationListenerFactory Landscape.getCreationListenerFactory() {
        return creationListenerFactory;
    }
*/
	before(Landscape landscape) : execution(void Landscape.persist()) && target(landscape) {
		LandscapeCreationListener listener = landscape.creationListenerFactory.get();
		listener.beforeCreate(landscape);
	}

	
}
