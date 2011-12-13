package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public interface LandscapeCreationListener {
	Landscape beforeCreate(Landscape landscape);
	void afterCreate(Landscape landscape);
	void onException(Exception e);
}
