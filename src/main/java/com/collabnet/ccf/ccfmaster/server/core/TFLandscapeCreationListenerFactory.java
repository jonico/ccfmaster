package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;

public class TFLandscapeCreationListenerFactory implements
		LandscapeCreationListenerFactory {

	private String baseUrl;
	private String iafServiceEndpoint = "http://localhost:8090/services/DummyService";
	
	@Override
	public CreateIntegratedAppStrategy get() {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Assert.isInstanceOf(TFUserDetails.class, user, "User is not logged in via TeamForge");
		try {
			IntegratedApplicationClient integratedAppClient = ((TFUserDetails) user).getConnection().getIntegratedAppClient();
			return new CreateIntegratedAppStrategy(baseUrl, iafServiceEndpoint, integratedAppClient);
		} catch (RemoteException e) {
			throw new CoreConfigurationException(e);
		}
	}

	public void setBaseUrl(String baseUrl) {
		// ensure thath the Base URL ends with a slash.
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setIafServiceEndpoint(String iafServiceEndpoint) {
		this.iafServiceEndpoint = iafServiceEndpoint;
	}

	public String getIafServiceEndpoint() {
		return iafServiceEndpoint;
	}

}
