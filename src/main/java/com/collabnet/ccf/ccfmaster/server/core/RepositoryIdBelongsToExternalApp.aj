package com.collabnet.ccf.ccfmaster.server.core;

import org.springframework.beans.factory.annotation.Autowired;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;

public aspect RepositoryIdBelongsToExternalApp {

	@Autowired
	private transient RepositoryIdBelongsToExternalAppListenerFactory RepositoryMapping.repositoryIdequalsExternalAppListenerFactory;

	before(RepositoryMapping rMapping) : execution(void RepositoryMapping.persist()) && target(rMapping) {
		RepositoryIdBelongsToExternalAppListener listener=rMapping.repositoryIdequalsExternalAppListenerFactory.get();
		listener.beforeCreate(rMapping);		  
	}


	before(RepositoryMapping rMapping ) : execution(RepositoryMapping RepositoryMapping.merge()) && target(rMapping) {
		RepositoryIdBelongsToExternalAppListener listener=rMapping.repositoryIdequalsExternalAppListenerFactory.get();
		listener.beforeMerge(rMapping);

	}
}
