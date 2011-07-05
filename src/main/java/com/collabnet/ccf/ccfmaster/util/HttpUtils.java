package com.collabnet.ccf.ccfmaster.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {
	private HttpUtils() {
		//prevent instantiation.
	}

	/**
	 * extract the URL of the context from the request.
	 * @param request the request to inspect
	 * @return the URL of the context
	 */
	public static String buildContextUrl(HttpServletRequest request) {
		String scheme = request.getScheme().toLowerCase();
		StringBuilder sb = new StringBuilder()
			.append(scheme)
			.append("://")
			.append(request.getServerName());
		if (!isDefaultPort(request)) {
			sb.append(":").append(request.getServerPort());
		}
		return sb.append(request.getContextPath()).toString(); 
	}

	public static boolean isDefaultPort(HttpServletRequest request) {
		String scheme = request.getScheme().toLowerCase();
		return (("http".equals(scheme)  && request.getServerPort() == 80) ||
			  ("https".equals(scheme) && request.getServerPort() == 443));
	}
	
	
}
