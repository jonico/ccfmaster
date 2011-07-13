package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

public interface ExternalAppCreationListener {
	public void beforeCreate(ExternalApp externalApp);
}
