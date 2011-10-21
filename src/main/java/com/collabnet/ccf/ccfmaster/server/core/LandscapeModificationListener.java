package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public interface LandscapeModificationListener {
	Landscape beforeMerge(Landscape landscape);
	void afterMerge(Landscape landscape);
	void onException(Exception e);
}
