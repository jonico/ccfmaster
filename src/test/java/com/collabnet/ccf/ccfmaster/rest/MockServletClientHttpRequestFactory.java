package com.collabnet.ccf.ccfmaster.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class MockServletClientHttpRequestFactory implements
		ClientHttpRequestFactory {
	
	private static final Logger log = LoggerFactory.getLogger(MockServletClientHttpRequestFactory.class);
	
	public class MockServletClientHttpResponse implements ClientHttpResponse {
		
		private MockHttpServletResponse response;
		
		public MockServletClientHttpResponse (MockHttpServletResponse response) {
			this.response = response;
		}

		@Override
		public InputStream getBody() throws IOException {
			byte[] byteArray = response.getContentAsByteArray();
			if (log.isDebugEnabled()) {
				log.debug("Received payload: " + new String(byteArray));
			}
			return new ByteArrayInputStream(byteArray);
		}

		@Override
		public HttpHeaders getHeaders() {
			HttpHeaders headers = new HttpHeaders();
			for(String headerName: response.getHeaderNames()) {
				for (Object headerValue: response.getHeaders(headerName)) {
					headers.add(headerName, headerValue.toString());
				}
			}
			return headers;
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return HttpStatus.valueOf(response.getStatus()); 
		}

		@Override
		public String getStatusText() throws IOException {
			return HttpStatus.valueOf(response.getStatus()).toString(); 
		}

		@Override
		public void close() {
			// do nothing
		}
	}

	public class MockServletClientHttpRequest extends AbstractClientHttpRequest {
		
		private Servlet servlet;
		private URI uri;
		private HttpMethod httpMethod;
		private String servletName;
		
		public MockServletClientHttpRequest(URI uri, HttpMethod httpMethod,
				Servlet servlet, String servletName) {
			this.servlet = servlet;
			this.uri = uri;
			this.httpMethod = httpMethod;
			this.servletName = servletName;
		}

		@Override
		public HttpMethod getMethod() {
			return httpMethod;
		}

		@Override
		public URI getURI() {
			return uri;
		}

		@Override
		protected ClientHttpResponse executeInternal(HttpHeaders headers,
				byte[] bufferedOutput) throws IOException {
			// removes servlet path from the request path
			String requestPath = uri.getPath().substring(uri.getPath().indexOf(servletName) + servletName.length());
			
			// figure out whether URI contained a query
			String query = uri.getRawQuery();
			
			MockHttpServletRequest request = new MockHttpServletRequest(httpMethod.toString(), requestPath);
			Set<Entry<String, List<String>>> headerEntries = getHeaders().entrySet();
			for (Entry<String, List<String>> entry : headerEntries) {
				String headerName = entry.getKey();
				for (String headerValue : entry.getValue()) {
					request.addHeader(headerName, headerValue);
				}
			}
			
			// now add query parameters
			if (query != null) {
				String params[] = query.split("&");
			    for (String param : params) {
			       String temp[] = param.split("=");
			       if (temp.length == 2) {
			    	   request.addParameter(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
			       }
			    }
			}
			request.setContent(bufferedOutput);
			
			if (log.isDebugEnabled()) {
				log.debug("Going to send payload: " + new String(bufferedOutput));
			}
			
			MockHttpServletResponse response = new MockHttpServletResponse();
			try {
				servlet.service(request, response);
			} catch (ServletException e) {
				throw new IOException(e.getMessage());
			}
			
			return new MockServletClientHttpResponse(response);
		}
	}

	private Servlet servlet;
	private String servletName;

	public MockServletClientHttpRequestFactory (Servlet servlet, String servletName) {
		this.servlet = servlet;
		this.servletName = servletName;
	}
	
	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
			throws IOException {
		return new MockServletClientHttpRequestFactory.MockServletClientHttpRequest(uri, httpMethod, servlet, servletName);
	}

}
