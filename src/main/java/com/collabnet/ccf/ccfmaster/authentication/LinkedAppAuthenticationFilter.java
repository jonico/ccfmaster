package com.collabnet.ccf.ccfmaster.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

public class LinkedAppAuthenticationFilter extends
		AbstractPreAuthenticatedProcessingFilter {

	private static final String SF_LOGIN_TOKEN = "sfLoginToken";
	private static final String SF_USERNAME = "sfUsername";
	private boolean exceptionIfHeaderMissing = true;

	public LinkedAppAuthenticationFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the String value of the request parameter SF_USERNAME
	 * @throws PreAuthenticatedCredentialsNotFoundException
	 *             if the parameter is missing and exceptionIfParameterMissing
	 *             is set to true.
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String param = request.getParameter(SF_USERNAME);
		if (exceptionIfHeaderMissing && param == null)
			throw new PreAuthenticatedCredentialsNotFoundException(SF_USERNAME);
		return param;
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
		String param = request.getParameter(SF_LOGIN_TOKEN);
		LinkedAppCredentials ret = null;
		if (param == null) {
			if (exceptionIfHeaderMissing) {
				throw new PreAuthenticatedCredentialsNotFoundException(SF_LOGIN_TOKEN);
			}
		} else {
			ret = new LinkedAppCredentials(param);
		}
		return ret;
	}

	public void setExceptionIfParameterMissing(boolean exceptionIfHeaderMissing) {
		this.exceptionIfHeaderMissing = exceptionIfHeaderMissing;
	}

	public class LinkedAppCredentials {
		private final String loginToken;

		public LinkedAppCredentials(String loginToken) {
			this.loginToken = loginToken;
			
		}

		public String getLoginToken() {
			return loginToken;
		}
		
		@Override
		public String toString() {
			return loginToken;
		}
	}
}
