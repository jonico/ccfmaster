package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

public aspect ExternalAppCreationAspect {
    @Autowired
    private transient ExternalAppCreationListenerFactory ExternalApp.creationListenerFactory;

	before(ExternalApp externalApp) : execution(void ExternalApp.persist()) && target(externalApp) {
		ExternalAppCreationListener listener = externalApp.creationListenerFactory.get();
		listener.beforeCreate(externalApp);
	}

}
