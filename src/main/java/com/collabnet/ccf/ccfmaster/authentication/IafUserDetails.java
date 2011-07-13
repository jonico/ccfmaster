package com.collabnet.ccf.ccfmaster.authentication;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.rbac.RbacClient;
import com.google.common.collect.ImmutableSet;

public class IafUserDetails extends TFUserDetails {

	private static final GrantedAuthorityImpl IAF_USER = new GrantedAuthorityImpl("ROLE_IAF_USER");

	private IafUserDetails(String username, String teamForgeUsername, String password,
			boolean enabled,
			boolean accountNonExpired,
			boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<GrantedAuthority> authorities,
			String sessionId, String teamForgeUrl, String fullName, String eMail, String linkId) {
		super(username, teamForgeUsername, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities, sessionId, teamForgeUrl, fullName, eMail);
		this.linkId = linkId;
	}
	private static final long serialVersionUID = 1L;
	private String linkId;

	/**
	 * creates a new IafUserDetails instance from another user.
	 * 
	 * In addition to the roles of {@code user}, the created user will have the
	 * permissions defined for the user in {@code projectId} for the integrated
	 * application {@code appId} added as roles. Spring Security convention
	 * dictates that roles start with "ROLE_". Thus, the role name is computed
	 * from the permission name by prepending "ROLE_" and uppercasing the
	 * resulting string.
	 * 
	 * In order to identify the user as an Integrated Application user, an
	 * additional role "ROLE_IAPP_USER" is added.
	 * 
	 * @param user
	 *            the pre-existing user instance
	 * @param connection
	 *            an authenticated connection to TeamForge.
	 * @param projectId
	 *            the projectId for which the permissions should be retrieved.
	 * @param linkId
	 *            the prplXXXX ID identifying the project-linkedApp combination
	 * @return a new IafUserDetails instance
	 * @throws RemoteException
	 *             on communication errors with TeamForge
	 */
	public static IafUserDetails fromUser(TFUserDetails user, Connection connection, String projectId, String linkId) throws RemoteException {
		Collection<GrantedAuthority> userAuth = user.getAuthorities();
		ImmutableSet.Builder<GrantedAuthority> authorities = ImmutableSet.builder();
		authorities.addAll(userAuth);
		authorities.addAll(iafRoles(connection, linkId, projectId));
		final String username = user.getUsername();
		IafUserDetails ret = new IafUserDetails(username, username,
				user.getPassword(), user.isEnabled(),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(), authorities.build(),
				connection.getSessionId(), connection.getServerUrl(),
				user.getFullName(), user.getEmail(), linkId);
		return ret;
	}
	private static Collection<GrantedAuthority> iafRoles(Connection connection, String linkId) throws RemoteException {
		String projectPath = connection.getIntegratedAppClient().getProjectPathByIntegratedAppId(linkId);
		String projectId = connection.getTeamForgeClient().getProjectDataByPath(projectPath).getId();
		return iafRoles(connection, linkId, projectId);
	}
	private static Collection<GrantedAuthority> iafRoles(Connection connection, String linkId, String projectId) throws RemoteException {
		ImmutableSet.Builder<GrantedAuthority> authorities = ImmutableSet.builder();
		RbacClient rbac = connection.getRbacClient();
		for (String permission : rbac.getIntegratedAppPermissionsForProject(projectId, linkId)) {
			permission = "ROLE_" + permission.toUpperCase(Locale.ROOT).replaceAll("\\s", "_");
			authorities.add(new GrantedAuthorityImpl(permission));
		}
		authorities.add(IAF_USER);
		return authorities.build();
	}
	
	public static IafUserDetails fromConnection(Connection connection, String linkId) throws RemoteException {
		return fromConnection(connection, "UNUSED", "UNUSED", linkId);
	}
	
	public static IafUserDetails fromConnection(Connection connection, String username, String linkId) throws RemoteException {
		return fromConnection(connection, username, "UNUSED", linkId);
	}
	
	public static IafUserDetails fromConnection(Connection connection, String username, String password, String linkId) throws RemoteException {
		ImmutableSet.Builder<GrantedAuthority> authorities = ImmutableSet.builder();
		authorities.addAll(iafRoles(connection, linkId));
		TFUserDetails user = TFUserDetails.fromConnection(connection, username, password, authorities.build());
		IafUserDetails ret = new IafUserDetails(username, user.getUsername(),
				user.getPassword(), user.isEnabled(),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(), user.getAuthorities(),
				connection.getSessionId(), connection.getServerUrl(),
				user.getFullName(), user.getEmail(), linkId);
		return ret;
	}

	public String getProjectPath() throws RemoteException {
		return getConnection().getIntegratedAppClient().getProjectPathByIntegratedAppId(linkId);
	}
	
	public String getLinkId() {
		return linkId;
	}
}
