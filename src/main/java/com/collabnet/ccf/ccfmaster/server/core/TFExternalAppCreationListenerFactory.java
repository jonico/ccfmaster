package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.NamedValues;
import com.collabnet.teamforge.api.main.ProjectDO;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentParameterDO;

public class TFExternalAppCreationListenerFactory implements ExternalAppCreationListenerFactory {

	final String baseUrl;

	public TFExternalAppCreationListenerFactory(String baseUrl) {
		// ensure thath the Base URL ends with a slash.
		if (!baseUrl.endsWith("/")) {
			baseUrl = baseUrl + "/";
		}
		this.baseUrl = baseUrl;
		
	}
	
	static class TFExternalAppCreationListener implements ExternalAppCreationListener {
		private static final Logger log = LoggerFactory.getLogger(TFExternalAppCreationListener.class);
		private final IntegratedApplicationClient client;
		private final TeamForgeClient tfc;
		private final String baseUrl;

		public TFExternalAppCreationListener(String baseUrl, IntegratedApplicationClient client, TeamForgeClient teamForgeClient) {
			this.baseUrl = baseUrl;
			this.client = client;
			this.tfc = teamForgeClient;
		}

		@Override
		public void beforeCreate(ExternalApp externalApp) {
			try {
				final Landscape landscape = externalApp.getLandscape();
				final String plugId = landscape.getPlugId();
				final String projectPath = externalApp.getProjectPath();
				String linkId = getLinkId(projectPath);
				if (linkId == null) {
					ProjectDO project = tfc.getProjectDataByPath(projectPath);
					final String prefix = CreateIntegratedAppStrategy.prefix + landscape.getId();
					NamedValues ret = client.enablePluggableComponent(project.getId(), plugId, new PluggableComponentParameterDO[0], prefix);
					if (log.isDebugEnabled()) {
						final String[] names = ret.getNames();
						final String[] values = ret.getValues();
						log.debug("result of enabling IA for project {} ({} names, {} values):", new Object[]{projectPath, names.length, values.length});
						for (int i = 0; i < names.length && i < values.length; i++) {
							log.debug("{} = {}", names[i], values[i]);
						}
					}
					linkId = getLinkId(projectPath);
					if(linkId == null) { // if dummyservice is down
						throw new CoreConfigurationException("IAF dummy service is down. Please contact TeamForge administrator");
					}
				}
				externalApp.setLinkId(linkId);
			} catch (RemoteException e) {
				throw new CoreConfigurationException(e);
			}
		}

		/**
		 * 
		 * @param projectPath
		 * @return if the IA is associated to projectPath, returns the linkId, null otherwise.
		 * @throws RemoteException if something goes wrong.
		 */
		private String getLinkId(String projectPath) throws RemoteException {
			try {
				String linkId = client.getLinkPlugId(projectPath, baseUrl);
				return linkId;
			} catch (RemoteException e) {
				if (e.getMessage().matches("No such object: Integrated application 'plug\\d+' is not associated to project 'proj\\d+'")) {
					// the IA is not associated to the project
					return null;
				} else {
					throw e;
				}
			}
		}

	}

	@Override
	public TFExternalAppCreationListener get() {
		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Assert.isInstanceOf(TFUserDetails.class, user, "User is not logged in via TeamForge");
		try {
			final Connection connection = ((TFUserDetails) user).getConnection();
			return new TFExternalAppCreationListener(baseUrl, connection.getIntegratedAppClient(), connection.getTeamForgeClient());
		} catch (RemoteException e) {
			throw new CoreConfigurationException(e);
		}
	}

}
