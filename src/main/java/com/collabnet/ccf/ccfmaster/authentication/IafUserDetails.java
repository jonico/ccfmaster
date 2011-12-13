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
			String sessionId, String teamForgeUrl, String fullName, String eMail, String linkId, String projectPath) throws RemoteException {
		super(username, teamForgeUsername, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities, sessionId, teamForgeUrl, fullName, eMail);
		this.linkId = linkId;
		this.projectPath = projectPath;
	}
	private static final long serialVersionUID = 1L;
	private String linkId;
	private String projectPath;
	
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
	
	public static IafUserDetails fromConnection(Connection connection, String username, String password, String linkId, String projectPath) throws RemoteException {
		ImmutableSet.Builder<GrantedAuthority> authorities = ImmutableSet.builder();
		String projectId = connection.getTeamForgeClient().getProjectDataByPath(projectPath).getId();
		authorities.addAll(iafRoles(connection, linkId, projectId));
		TFUserDetails user = TFUserDetails.fromConnection(connection, username, password, authorities.build());
		IafUserDetails ret = new IafUserDetails(username, user.getUsername(),
				user.getPassword(), user.isEnabled(),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isAccountNonLocked(), user.getAuthorities(),
				connection.getSessionId(), connection.getServerUrl(),
				user.getFullName(), user.getEmail(), linkId, projectPath);
		return ret;
	}

	public String getProjectPath() {
		return projectPath;
	}
	
	public String getLinkId() {
		return linkId;
	}
}
