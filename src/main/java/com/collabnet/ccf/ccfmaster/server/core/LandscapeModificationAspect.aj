package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public aspect LandscapeModificationAspect {
    @Autowired
    private transient LandscapeModificationListenerFactory Landscape.modificationListenerFactory;
/*
    public void Landscape.setCreationListenerFactory(LandscapeCreationListenerFactory creationListenerFactory) {
        this.creationListenerFactory = creationListenerFactory;
    }

    public LandscapeCreationListenerFactory Landscape.getCreationListenerFactory() {
        return creationListenerFactory;
    }
*/
	before(Landscape landscape) : execution(Landscape Landscape.merge()) && target(landscape) {
		LandscapeModificationListener listener = landscape.modificationListenerFactory.get();
		listener.beforeMerge(landscape);
	}

	
}
