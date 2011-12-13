package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;

public class MockRepositoryIdBelongsToExternalAppListenerFactory implements RepositoryIdBelongsToExternalAppListenerFactory {
	
	static class MockRepositoryIdBelongsToExternalAppListener implements RepositoryIdBelongsToExternalAppListener {
		private static final Logger log = LoggerFactory.getLogger(RepositoryIdBelongsToExternalAppListener.class);
		@Override
		public void beforeCreate(RepositoryMapping rMapping) {
			log.debug("called beforeCreate({})", rMapping);
		}
		
		@Override
		public void beforeMerge(RepositoryMapping rMapping) {
			log.debug("called beforeMerge({})", rMapping);
		}
		
	}
	
	@Override
	public RepositoryIdBelongsToExternalAppListener get() {
		return new MockRepositoryIdBelongsToExternalAppListener();
	}

}
