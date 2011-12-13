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

import com.collabnet.ccf.ccfmaster.authentication.LinkedAppAuthenticationFilter.LinkedAppCredentials;
import com.collabnet.teamforge.api.Connection;

public class LinkedAppUserDetailsService implements
		AuthenticationUserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(LinkedAppUserDetailsService.class);
	
	private Proxy proxy;
	private String httpAuthUser;
	private String httpAuthPass;
	private String serverUrl;

	public LinkedAppUserDetailsService(String serverUrl) {
		this(serverUrl, null, null, null);
	}
	public LinkedAppUserDetailsService(String serverUrl, Proxy proxy,
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
		if(!(token.getCredentials() instanceof LinkedAppCredentials))
			throw new BadCredentialsException("passed credentials had wrong type");
		LinkedAppCredentials linkedAppCredentials = (LinkedAppCredentials) token.getCredentials();
		final String oneTimeToken = linkedAppCredentials.getLoginToken(); 
		final Connection.Builder builder = Connection.builder(serverUrl)
			.oneTimeToken(linkedAppCredentials.getUserName(), oneTimeToken);
		if (proxy != null) {
			builder.proxy(proxy).httpAuth(httpAuthUser, httpAuthPass);
		}
		final Connection connection = builder.build();
		try {
			connection.login(); // sets connectionHelper.sessionId as a side-effect
			return TFUserDetails.fromConnection(connection, oneTimeToken, token.getAuthorities());
		} catch (RemoteException e) {
			log.debug("exception logging in to TeamForge", e);
			throw new BadCredentialsException("Bad credentials supplied: " + e.getMessage(), e);
		}
	}
}
