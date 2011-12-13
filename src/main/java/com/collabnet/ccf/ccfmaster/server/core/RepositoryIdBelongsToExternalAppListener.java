package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;

public interface RepositoryIdBelongsToExternalAppListener {
	public void beforeCreate(RepositoryMapping rMapping);
	public void beforeMerge(RepositoryMapping rMapping);
}
