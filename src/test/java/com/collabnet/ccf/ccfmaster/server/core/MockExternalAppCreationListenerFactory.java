package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

public class MockExternalAppCreationListenerFactory implements ExternalAppCreationListenerFactory {
	
	static class MockExternalAppCreationListener implements ExternalAppCreationListener {
		private static final Logger log = LoggerFactory.getLogger(ExternalAppCreationListener.class);
		@Override
		public void beforeCreate(ExternalApp externalApp) {
			log.debug("called beforeCreate({})", externalApp);
		}
	}
	
	@Override
	public ExternalAppCreationListener get() {
		return new MockExternalAppCreationListener();
	}

}
