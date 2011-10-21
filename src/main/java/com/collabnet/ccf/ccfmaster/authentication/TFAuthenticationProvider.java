package com.collabnet.ccf.ccfmaster.authentication;

import java.net.Proxy;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.Connection.Builder;

/**
 * The TFAuthentication provider plugs into Spring Security's forms
 * authentication support and authenticates the supplied username and password
 * against the TeamForge instance configured in {@code serverUrl}.
 * 
 * @author ctaylor
 * 
 */
public class TFAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	private static Logger log = LoggerFactory.getLogger(TFAuthenticationProvider.class);

	private String serverUrl;
	private Proxy proxy;
	private String httpAuthUser;
	private String httpAuthPass;


	public TFAuthenticationProvider(String serverUrl) {
		this(serverUrl, null, null, null);
	}
	
	public TFAuthenticationProvider(String serverUrl, Proxy proxy,
			String httpAuthUser, String httpAuthPass) {
		super();
		this.serverUrl = serverUrl;
		this.proxy = proxy;
		this.httpAuthUser = httpAuthUser;
		this.httpAuthPass = httpAuthPass;
//		setUserCache(new MapUserCache());
	}
	//public TFAuthenticationProvider(String)

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
		if (!userDetails.getPassword().equals(token.getCredentials())) {
			throw new BadCredentialsException("weird, this shouldn't happen");
		}
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
		Builder connectionBuilder = Connection.builder(serverUrl)
			.proxy(proxy)
			.httpAuth(httpAuthUser, httpAuthPass);
		try {
			if (username.startsWith("SESSIONID/")) {
				/*
				 * user previously logged in, assume a valid SOAP SessionID is
				 * supplied as the password.
				 */
				String sessionId = token.getCredentials().toString();
				Connection connection = connectionBuilder.sessionId(sessionId).build();
				return createTFUserDetails(username, token, connection, null);
			} else if (username.startsWith("IAF/")) {
				/*
				 * TODO: we're logging in with a pre-existing SOAP SessionID, but we
				 * pretend we're logging in via IAF and restricting access to the
				 * linkID (prplXXXX) supplied in the remainder of the username.
				 */
				String [] userParts = username.split("/", 2);
				Assert.isTrue(userParts.length == 2);
				String linkId = userParts[1];
				String sessionId = token.getCredentials().toString();
				Connection connection = connectionBuilder.sessionId(sessionId).build();
				return createIafUserDetails(username, connection, linkId);
			} else {
				/*
				 * regular TeamForge username/password supplied. Try to login
				 * using these. Optionally, a ProjectId can be specified by
				 * supplying "user/linkId" (e.g. "admin/prpl1234") as the user
				 * name. The IAF permissions will be added to the returned principal.
				 */
				String [] userParts = username.split("/", 2);
				// don't modify the passed-in username because this causes
				// Spring security to re-check authentication on every request.
				// See {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter.authenticationIsRequired(String)}
				String tfUsername = username;
				String linkId = null;
				if (userParts.length > 1) {
					tfUsername = userParts[0];
					linkId = userParts[1];
				}
				String password = token.getCredentials().toString(); 
				Connection connection = connectionBuilder
					.userNamePassword(tfUsername, password)
					.connectionCached(false)
					.build();
				return createTFUserDetails(username, token, connection, linkId);
			}
		} catch (RemoteException e) {
			log.debug("exception logging in to TeamForge", e);
			throw new BadCredentialsException("Bad credentials supplied: " + e.getMessage(), e);
		}
	}
	
	private IafUserDetails createIafUserDetails(String username, Connection connection, String linkId) throws RemoteException {
		String projectPath = connection.getIntegratedAppClient().getProjectPathByIntegratedAppId(linkId);
		return IafUserDetails.fromConnection(connection, username, "UNUSED", linkId, projectPath);
	}	

	private TFUserDetails createTFUserDetails(
			String username, UsernamePasswordAuthenticationToken token, Connection connection, String linkId) throws RemoteException {
		connection.login();
		String password = token.getCredentials().toString(); 
		if (linkId != null) {
			String projectPath = connection.getIntegratedAppClient().getProjectPathByIntegratedAppId(linkId);
			return IafUserDetails.fromConnection(connection, username, password, linkId, projectPath);
		} else {
			return TFUserDetails.fromConnection(connection, username, password, token.getAuthorities());
		}
	}

/*	
	public static class MapUserCache implements UserCache {

		private static final Logger log = LoggerFactory.getLogger(MapUserCache.class);
		private Map<String, UserDetails> userCache = new ConcurrentHashMap<String, UserDetails>();
		
		@Override
		public UserDetails getUserFromCache(String username) {
			final UserDetails userDetails = userCache.get(username);
			log.debug("retrieved user details for username {}: '{}'", username, userDetails);
			return userDetails;
		}

		@Override
		public void putUserInCache(UserDetails user) {
			final String username = user.getUsername();
			log.debug("adding details for {} to cache", username);
			userCache.put(username, user);
		}

		@Override
		public void removeUserFromCache(String username) {
			log.debug("removing {} from cache", username);
			userCache.remove(username);
		}
		
	}
*/
}
