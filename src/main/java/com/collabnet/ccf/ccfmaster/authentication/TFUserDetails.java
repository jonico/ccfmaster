package com.collabnet.ccf.ccfmaster.authentication;

// import static
// com.collabnet.ce.soap50.webservices.cemain.UserSoapDO.STATUS_DISABLED;
// import static
// com.collabnet.ce.soap50.webservices.cemain.UserSoapDO.STATUS_PENDING;
// import static
// com.collabnet.ce.soap50.webservices.cemain.UserSoapDO.STATUS_REMOVED;
import static com.collabnet.ce.soap50.webservices.cemain.UserSoapDO.STATUS_ACTIVE;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;

import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.UserDO;

public class TFUserDetails extends User {
    private static final long            serialVersionUID = 1L;
    public static final GrantedAuthority USER             = new GrantedAuthorityImpl(
                                                                  "ROLE_TF_USER");
    public static final GrantedAuthority SUPER_USER       = new GrantedAuthorityImpl(
                                                                  "ROLE_TF_SUPER_USER");
    public static final GrantedAuthority RESTRICTED_USER  = new GrantedAuthorityImpl(
                                                                  "ROLE_TF_RESTRICTED_USER");
    private final String                 sessionId;
    private final String                 serverUrl;
    private final String                 fullName;
    private final String                 eMail;
    private final String                 teamForgeUsername;

    public TFUserDetails(String username, String teamForgeUsername,
            String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<GrantedAuthority> authorities, String sessionId,
            String teamForgeUrl, String fullName, String eMail) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
        this.teamForgeUsername = teamForgeUsername;
        this.sessionId = sessionId;
        this.serverUrl = teamForgeUrl;
        this.fullName = fullName;
        this.eMail = eMail;
    }

    /**
     * re-creates a TeamForge connection from the sessionID and serverURL stored
     * in this object.
     * 
     * @return
     * @throws TFSessionExpiredException
     *             if the connection has expired.
     * @throws RemoteException
     */
    public Connection getConnection() throws RemoteException {
        Connection connection = Connection.builder(getServerUrl())
                .sessionId(sessionId).build();
        try {
            // keepAlive is not wrapped yet, so we have to figure out the appropriate version ourselves
            if (connection.supports60()) {
                connection.getTeamForgeClient().keepAlive60();
            } else {
                connection.getTeamForgeClient().keepAlive50();
            }
        } catch (RemoteException e) {
            // our CTF session has timed out
            throw new TFSessionExpiredException(
                    "Error retrieving TeamForge connection: " + e.getMessage(),
                    e);
        }
        return connection;
    }

    public String getEmail() {
        return eMail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    // TODO expose the other user properties.
    /**
     * the user name as reported by the TeamForge API. Note that this may differ
     * from {@link TFUserDetails#getUsername()}, which is the user name used
     * during authentication, i.e. "admin/prpl1234" or "IAF/". This distinction
     * is necessary because Spring security re-checks the credentials for each
     * request when http-basic auth is used and getUsername() != the username
     * from the request.
     */
    public String getTeamForgeUsername() {
        return teamForgeUsername;
    }

    public static TFUserDetails fromConnection(Connection connection,
            String password, Collection<GrantedAuthority> authorities)
            throws RemoteException {
        return fromConnection(connection, "UNUSED", password, authorities);
    }

    /**
     * @see User
     * @param connection
     * @param password
     * @param password2
     * @param authorities
     * @throws RemoteException
     */
    public static TFUserDetails fromConnection(Connection connection,
            String username, String password,
            Collection<GrantedAuthority> authorities) throws RemoteException {
        UserDO userDO = connection.getTeamForgeClient().getCurrentUserData();
        String tfURL = connection.getServerUrl();
        String sessionId = connection.getSessionId();
        String fullName = userDO.getFullName();
        String eMail = userDO.getEmail();
        Set<GrantedAuthority> authList = new HashSet<GrantedAuthority>(
                authorities);
        authList.add(USER);
        if (userDO.getSuperUser()) {
            authList.add(SUPER_USER);
        }
        if (userDO.getRestrictedUser()) {
            authList.add(RESTRICTED_USER);
        }
        return new TFUserDetails(username, userDO.getUsername(),
                password,
                STATUS_ACTIVE.equals(userDO.getStatus()), // enabled
                STATUS_ACTIVE.equals(userDO.getStatus()), // accountNonExpired
                STATUS_ACTIVE.equals(userDO.getStatus()), // credentialsNonExpired
                STATUS_ACTIVE.equals(userDO.getStatus()), // accountNonLocked,
                Collections.unmodifiableCollection(authList), sessionId, tfURL,
                fullName, eMail);
    }
}
