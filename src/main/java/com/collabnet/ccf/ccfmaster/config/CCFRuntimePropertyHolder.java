package com.collabnet.ccf.ccfmaster.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CCFRuntimePropertyHolder {
	
	static final Logger log = LoggerFactory.getLogger(CCFRuntimePropertyHolder.class);
	
	private String tfUrl;
	
	private String ccfBaseUrl;
	
	private String ccfHome;
	
	private String maxAttachmentSize;
	
	private String iafServiceEndpoint;
	
	private String ccfDBPort;
	
	private String jmxForwardPort;
	
	private String jmxReversePort;
	
	public String getTfUrl() {
		return tfUrl;
	}


	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
		log.debug("CCF Runtime Property TF-URL set to " + tfUrl);
	}


	public String getMaxAttachmentSize() {
		return maxAttachmentSize;
	}


	public void setMaxAttachmentSize(String maxAttachmentSize) {
		this.maxAttachmentSize = maxAttachmentSize;
		log.debug("CCF Runtime Property Default Max attachment size set to " + maxAttachmentSize);
	}


	public void setCcfBaseUrl(String ccfBaseUrl) {
		this.ccfBaseUrl = ccfBaseUrl;
		log.debug("CCF Runtime Property CCF Base URL set to " + ccfBaseUrl);
	}

	public String getCcfBaseUrl() {
		return ccfBaseUrl;
	}


	public void setCcfHome(String ccfHome) {
		this.ccfHome = ccfHome;
		log.debug("CCF Runtime Property CCF Home set to " + ccfHome);
	}


	public String getCcfHome() {
		return ccfHome;
	}


	public void setIafServiceEndpoint(String iafServiceEndpoint) {
		this.iafServiceEndpoint = iafServiceEndpoint;
		log.debug("CCF Runtime Property IAF Service Endpoint set to " + iafServiceEndpoint);
	}


	public String getIafServiceEndpoint() {
		return iafServiceEndpoint;
	}


	public void setCcfDBPort(String ccfDBPort) {
		this.ccfDBPort = ccfDBPort;
		log.debug("CCF Runtime Property CCF DB port set to " + ccfDBPort);
	}


	public String getCcfDBPort() {
		return ccfDBPort;
	}


	public void setJmxForwardPort(String jmxForwardPort) {
		this.jmxForwardPort = jmxForwardPort;
		log.debug("CCF Runtime Property JMX Forward Port set to " + jmxForwardPort);
	}


	public String getJmxForwardPort() {
		return jmxForwardPort;
	}


	public void setJmxReversePort(String jmxReversePort) {
		this.jmxReversePort = jmxReversePort;
		log.debug("CCF Runtime Property JMX Reverse Port set to " + jmxReversePort);
	}


	public String getJmxReversePort() {
		return jmxReversePort;
	}


}
