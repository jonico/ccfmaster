package com.collabnet.ccf.ccfmaster.server.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFLandscapeCreationListenerFactoryTest {
	private static final String BASE_URL = "http://localhost:8080/CCFMaster";

	@Test
	public void slashIsAppendedToBaseUrlTFLCLF() {
		TFLandscapeCreationListenerFactory factory = new TFLandscapeCreationListenerFactory();
		factory.setBaseUrl(BASE_URL);
		assertTrue("baseUrl must end with '/'", factory.getBaseUrl().endsWith("/"));
		assertFalse("unnecessary extra slash was appended", factory.getBaseUrl().endsWith("//"));
	}

	@Test
	public void slashIsNotAppendedToBaseUrlUnlessNecessaryTFLCLF() {
		TFLandscapeCreationListenerFactory factory = new TFLandscapeCreationListenerFactory();
		factory.setBaseUrl(BASE_URL + "/");
		assertTrue("baseUrl must end with '/'", factory.getBaseUrl().endsWith("/"));
		assertFalse("unnecessary extra slash was appended", factory.getBaseUrl().endsWith("//"));
	}

	@Test
	public void slashIsAppendedToBaseUrlTFEACLF() {
		TFExternalAppCreationListenerFactory factory = new TFExternalAppCreationListenerFactory(BASE_URL);
		assertTrue("baseUrl must end with '/'", factory.baseUrl.endsWith("/"));
		assertFalse("unnecessary extra slash was appended", factory.baseUrl.endsWith("//"));
	}

	@Test
	public void slashIsNotAppendedToBaseUrlUnlessNecessaryTFEACLF() {
		TFExternalAppCreationListenerFactory factory = new TFExternalAppCreationListenerFactory(BASE_URL + "/");
		assertTrue("baseUrl must end with '/'", factory.baseUrl.endsWith("/"));
		assertFalse("unnecessary extra slash was appended", factory.baseUrl.endsWith("//"));
	}
}
