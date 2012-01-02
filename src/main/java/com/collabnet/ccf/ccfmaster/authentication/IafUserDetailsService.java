package com.collabnet.ccf.ccfmaster.authentication;

import java.net.Proxy;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
		if(!(token.getCredentials() instanceof FirstRequestCredentials))
			throw new BadCredentialsException("passed credentials had wrong type");
		String oneTimeToken = token.getPrincipal().toString();
		final Builder connectionBuilder = Connection.builder(serverUrl).oneTimeToken(oneTimeToken);
		if (proxy != null)
			connectionBuilder.proxy(proxy);
		if (httpAuthUser != null && httpAuthPass != null)
			connectionBuilder.httpAuth(httpAuthUser, httpAuthPass);
		Connection connection = connectionBuilder.build();
		FirstRequestCredentials credentials = (FirstRequestCredentials) token.getCredentials();
		try {
			connection.login();
			if (credentials.isLoggedIn()) {
				// request had a logged in header, logged in user clicked on IAF button in menu bar.
				String linkId = connection.getIntegratedAppClient().getLinkPlugId(credentials.getProjectPath(), credentials.getBaseUrl());
				return IafUserDetails.fromConnection(connection, "UNUSED", "UNUSED", linkId, credentials.getProjectPath());
			} else {
				// no logged in header -> site admin clicked "configure" in IAF administration
				final Collection<GrantedAuthority> authorities = Collections.emptySet();
				return TFUserDetails.fromConnection(connection, "UNUSED", authorities);
			}
		} catch (RemoteException e) {
			log.debug("exception logging in to TeamForge", e);
			throw new BadCredentialsException("Bad credentials supplied: " + e.getMessage(), e);
		}
	}

}
