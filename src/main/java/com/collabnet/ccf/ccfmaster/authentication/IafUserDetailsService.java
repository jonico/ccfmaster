package com.collabnet.ccf.ccfmaster.authentication;

import java.net.Proxy;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.collabnet.ccf.ccfmaster.authentication.IafAuthenticationFilter.FirstRequestCredentials;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.Connection.Builder;

public class IafUserDetailsService implements AuthenticationUserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(IafUserDetailsService.class);
	
	private String serverUrl;
	private final Proxy proxy;
	private final String httpAuthUser;
	private final String httpAuthPass;

	public IafUserDetailsService(String serverUrl) {
		this(serverUrl, null, null, null);
	}
	public IafUserDetailsService(String serverUrl, Proxy proxy,
			String httpAuthUser, String httpAuthPass) {
		super();
		this.serverUrl = serverUrl;
		this.proxy = proxy;
		this.httpAuthUser = httpAuthUser;
		this.httpAuthPass = httpAuthPass;
	}

	@Override
	public UserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException {
		String oneTimeToken = token.getPrincipal().toString();
		if(!(token.getCredentials() instanceof FirstRequestCredentials))
			throw new BadCredentialsException("passed credentials had wrong type");
		FirstRequestCredentials credentials = (FirstRequestCredentials) token.getCredentials();
		final Builder connectionBuilder = Connection.builder(serverUrl).oneTimeToken(oneTimeToken);
		if (proxy != null)
			connectionBuilder.proxy(proxy);
		if (httpAuthUser != null && httpAuthPass != null)
			connectionBuilder.httpAuth(httpAuthUser, httpAuthPass);
		Connection connection = connectionBuilder.build();
		try {
			connection.login();
			String linkPlugId = connection.getIntegratedAppClient().getLinkPlugId(credentials.getProjectPath(), credentials.getBaseUrl());
			IafUserDetails ret = IafUserDetails.fromConnection(connection, linkPlugId);
			return ret;
		} catch (RemoteException e) {
			log.debug("exception logging in to TeamForge", e);
			throw new BadCredentialsException("Bad credentials supplied: " + e.getMessage(), e);
		}
	}

}
