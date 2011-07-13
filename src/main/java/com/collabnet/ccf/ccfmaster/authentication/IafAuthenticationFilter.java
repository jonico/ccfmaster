package com.collabnet.ccf.ccfmaster.authentication;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.collabnet.ccf.ccfmaster.util.HttpUtils;

public class IafAuthenticationFilter extends
	AbstractPreAuthenticatedProcessingFilter {
	private static final Logger log = LoggerFactory.getLogger(IafAuthenticationFilter.class);
	private static final String PARAM_LOGGED_IN = "isLoggedIn";
	// private static final String PARAM_PROJECT_ID = "sfProjId";
	private static final String PARAM_PROJECT_PATH = "sfProj";
	// private static final String PARAM_OBJECT_ID = "id";
	// private static final Pattern pattern = Pattern.compile(".*(prpl[0-9]{4,})");

	public static final String PARAM_LOGIN_TOKEN = "sfLoginToken";
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		if (isFirstRequest(request)) {
			boolean loggedIn = isLoggedIn(request);
			if (!loggedIn) {
				/*
				 * since the user is not logged into teamforge, we don't do
				 * anything here and rely on the rest of the filter chain to
				 * authenticate or reject the user somehow.
				 */
				return null;
			}
			return request.getParameter(PARAM_LOGIN_TOKEN);
			
			//Connection connection = Connection.builder(serverUrl).oneTimeToken(ssoToken).build();
//			String integratedAppId = request.getParameter("sfId");
//			String integratedAppObjectId = request.getParameter("id");
//			String projectId = request.getParameter("sfProjId");
		} else {
			// String integratedAppId = findIntegratedAppId(request);
			// TODO: probably nothing.
		}
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean isLoggedIn(HttpServletRequest request) {
		return "true".equals(request.getParameter(PARAM_LOGGED_IN));
	}
	
/*	private static String findIntegratedAppId(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String,String> params = request.getParameterMap();
		String ret = params.get("sfId");
		if (ret != null)
			return ret;
		ret = params.get("linkid");
		if (ret != null)
			return ret;
		String path = request.getPathInfo() != null ?
					request.getPathInfo() : request.getServletPath();
		Matcher matcher = pattern.matcher(path);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
*/
	private boolean isFirstRequest(HttpServletRequest request) {
		return request.getParameterMap().containsKey(PARAM_LOGIN_TOKEN);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		if (isFirstRequest(request)){
			return createCredentials(request);
		} else {
			return null;
		}
	}
	
	private static Credentials createCredentials(HttpServletRequest request) {
		Credentials ret = new FirstRequestCredentials(request);
//				request.getParameter(PARAM_PROJECT_ID),
//				findIntegratedAppId(request),
//				request.getParameter(PARAM_OBJECT_ID)
		return ret;
	}

	public static class FirstRequestCredentials extends Credentials implements Serializable {
		private static final long serialVersionUID = 1L;
		private final String projectPath;
		private final boolean loggedIn;
		public FirstRequestCredentials(HttpServletRequest request) {
			super(request);
			this.projectPath = request.getParameter(PARAM_PROJECT_PATH); // e.g. "brokerage_system_sample"
			this.loggedIn = IafAuthenticationFilter.isLoggedIn(request);
		}
		public String getProjectPath() {
			// this is deep voodoo, see IntegratedAppSupport.java, line 246.
			return "projects." + projectPath;
		}
		public boolean isLoggedIn() {
			return loggedIn;
		}
	}
	public static class Credentials {
		private String baseUrl;
		private Credentials(HttpServletRequest request) {
			this.baseUrl = makeBaseUrl(request);
		}
		private String makeBaseUrl(HttpServletRequest request) {
			String url = request.getRequestURL().toString();
			String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
			url = url.substring(0, url.length() - pathInfo.length());
			log.debug("BaseURL: " + url);
			return url;
		}
		public String getBaseUrl() {
			return baseUrl;
		}
	}
}
