package com.collabnet.ccf.ccfmaster.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

public class LinkedAppAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    public class LinkedAppCredentials {
        private final String loginToken;
        private final String userName;

        public LinkedAppCredentials(String loginToken, String userName) {
            this.loginToken = loginToken;
            this.userName = userName;
        }

        public String getLoginToken() {
            return loginToken;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public String toString() {
            return loginToken;
        }
    }

    private static final String SF_LOGIN_TOKEN = "sfLoginToken";

    private static final String SF_USERNAME    = "sfUsername";

    public LinkedAppAuthenticationFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return an LinkedAppCredentials instance containing the String value of
     *         the request parameter SF_LOGIN_TOKEN or null if it's missing.
     * @throws PreAuthenticatedCredentialsNotFoundException
     *             if the parameter is missing and exceptionIfParameterMissing
     *             is set to true.
     */
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String loginToken = request.getParameter(SF_LOGIN_TOKEN);
        if (loginToken != null) {
            return new LinkedAppCredentials(loginToken,
                    request.getParameter(SF_USERNAME));
        } else {
            return null;
        }
    }

    /**
     * @return the String value of the request parameter SF_USERNAME
     * @throws PreAuthenticatedCredentialsNotFoundException
     *             if the parameter is missing and exceptionIfParameterMissing
     *             is set to true.
     */
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        boolean isLinkedAppRequest = hasParam(SF_LOGIN_TOKEN, request)
                && hasParam(SF_USERNAME, request);
        if (isLinkedAppRequest) {
            return request.getParameter(SF_LOGIN_TOKEN);
        } else {
            // now we determine the currently logged in user
            Authentication currentUser = SecurityContextHolder.getContext()
                    .getAuthentication();

            if (currentUser != null) {
                // if there is a user logged in and we do not have a preauthenticated user, return the currently logged in user
                // to indicate that no change happened
                return currentUser.getName();
            } else {
                // if nobody is logged in and we have no preauthenticated user, proceed with next filter
                return null;
            }
        }
    }

    private boolean hasParam(String paramName, HttpServletRequest request) {
        return request.getParameterMap().containsKey(paramName);
    }
}
