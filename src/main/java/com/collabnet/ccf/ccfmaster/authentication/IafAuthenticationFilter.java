package com.collabnet.ccf.ccfmaster.authentication;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class IafAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    public static class Credentials {
        private String baseUrl;

        private Credentials(HttpServletRequest request) {
            this.baseUrl = makeBaseUrl(request);
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        private String makeBaseUrl(HttpServletRequest request) {
            String url = request.getRequestURL().toString();
            String pathInfo = request.getPathInfo() == null ? "" : request
                    .getPathInfo();
            url = url.substring(0, url.length() - pathInfo.length());
            log.debug("BaseURL: " + url);
            return url;
        }
    }

    public static class FirstRequestCredentials extends Credentials implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String      projectPath;
        private final boolean     loggedIn;

        public FirstRequestCredentials(HttpServletRequest request) {
            super(request);
            this.projectPath = request.getParameter(PARAM_PROJECT_PATH); // e.g. "brokerage_system_sample"
            this.loggedIn = IafAuthenticationFilter
                    .loggedInParamIsTrue(request);
        }

        public String getProjectPath() {
            // this is deep voodoo, see IntegratedAppSupport.java, line 246.
            return "projects." + projectPath;
        }

        public boolean isLoggedIn() {
            return loggedIn;
        }
    }

    private static final Logger log                = LoggerFactory
                                                           .getLogger(IafAuthenticationFilter.class);
    private static final String PARAM_LOGGED_IN    = "isLoggedIn";

    // private static final String PARAM_PROJECT_ID = "sfProjId";
    private static final String PARAM_PROJECT_PATH = "sfProj";

    // private static final String PARAM_OBJECT_ID = "id";
    // private static final Pattern pattern = Pattern.compile(".*(prpl[0-9]{4,})");
    public static final String  PARAM_LOGIN_TOKEN  = "sfLoginToken";

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        if (hasLoginTokenParam(request)) {
            return createCredentials(request);
        } else {
            return null;
        }
    }

    /**
     * this method must return a string, or
     * {@link AbstractPreAuthenticatedProcessingFilter} will always re-create
     * the session. if the string is equal to the current user, nothing happens.
     * If null is returned, the next filter is tried. If any other string is
     * returned, a new session will be created.
     */
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // first check whether we actually have a pre authenticated user
        // we need to check for PARAM_PROJECT_PATH, because TF Linked
        // Application SSO also uses PARAM_LOGIN_TOKEN for its SSO token.
        boolean isIafRequest = hasParam(PARAM_LOGIN_TOKEN, request)
                && hasParam(PARAM_PROJECT_PATH, request);

        if (isIafRequest) {
            // we return the login token to ensure that a new session is started (i.e. it won't equal any current user name)
            return request.getParameter(PARAM_LOGIN_TOKEN);
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

    private boolean hasLoginTokenParam(HttpServletRequest request) {
        return hasParam(PARAM_LOGIN_TOKEN, request);
    }

    /*
     * private static String findIntegratedAppId(HttpServletRequest request) {
     * @SuppressWarnings("unchecked") Map<String,String> params =
     * request.getParameterMap(); String ret = params.get("sfId"); if (ret !=
     * null) return ret; ret = params.get("linkid"); if (ret != null) return
     * ret; String path = request.getPathInfo() != null ? request.getPathInfo()
     * : request.getServletPath(); Matcher matcher = pattern.matcher(path); if
     * (matcher.find()) { return matcher.group(1); } return null; }
     */
    private boolean hasParam(String paramName, HttpServletRequest request) {
        return request.getParameterMap().containsKey(paramName);
    }

    private static Credentials createCredentials(HttpServletRequest request) {
        Credentials ret = new FirstRequestCredentials(request);
        //				request.getParameter(PARAM_PROJECT_ID),
        //				findIntegratedAppId(request),
        //				request.getParameter(PARAM_OBJECT_ID)
        return ret;
    }

    private static boolean loggedInParamIsTrue(HttpServletRequest request) {
        return "true".equals(request.getParameter(PARAM_LOGGED_IN));
    }
}
