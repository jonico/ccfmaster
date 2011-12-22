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
	
	private String saasMode;
	
	private String isArchiveRequired;
	
	public String getTfUrl() {
		return tfUrl;
	}


	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
		log.info("CCF Runtime Property TF-URL set to " + tfUrl);
	}


	public String getMaxAttachmentSize() {
		return maxAttachmentSize;
	}


	public void setMaxAttachmentSize(String maxAttachmentSize) {
		this.maxAttachmentSize = maxAttachmentSize;
		log.info("CCF Runtime Property Default Max attachment size set to " + maxAttachmentSize);
	}


	public void setCcfBaseUrl(String ccfBaseUrl) {
		this.ccfBaseUrl = ccfBaseUrl;
		log.info("CCF Runtime Property CCF Base URL set to " + ccfBaseUrl);
	}

	public String getCcfBaseUrl() {
		return ccfBaseUrl;
	}


	public void setCcfHome(String ccfHome) {
		this.ccfHome = ccfHome;
		log.info("CCF Runtime Property CCF Home set to " + ccfHome);
	}


	public String getCcfHome() {
		return ccfHome;
	}


	public void setIafServiceEndpoint(String iafServiceEndpoint) {
		this.iafServiceEndpoint = iafServiceEndpoint;
		log.info("CCF Runtime Property IAF Service Endpoint set to " + iafServiceEndpoint);
	}


	public String getIafServiceEndpoint() {
		return iafServiceEndpoint;
	}


	public void setCcfDBPort(String ccfDBPort) {
		this.ccfDBPort = ccfDBPort;
		log.info("CCF Runtime Property CCF DB port set to " + ccfDBPort);
	}


	public String getCcfDBPort() {
		return ccfDBPort;
	}


	public void setJmxForwardPort(String jmxForwardPort) {
		this.jmxForwardPort = jmxForwardPort;
		log.info("CCF Runtime Property JMX Forward Port set to " + jmxForwardPort);
	}


	public String getJmxForwardPort() {
		return jmxForwardPort;
	}


	public void setJmxReversePort(String jmxReversePort) {
		this.jmxReversePort = jmxReversePort;
		log.info("CCF Runtime Property JMX Reverse Port set to " + jmxReversePort);
	}


	public String getJmxReversePort() {
		return jmxReversePort;
	}


	public String getSaasMode() {
		return saasMode;
	}


	public void setSaasMode(String saasMode) {
		this.saasMode = saasMode;
		log.info("CCF Runtime Property SAAS Mode set to " + saasMode);
	}
	
	public String getIsArchiveRequired() {
		return isArchiveRequired;
	}


	public void setIsArchiveRequired(String isArchiveRequired) {
		this.isArchiveRequired = isArchiveRequired;
		log.info("CCF Runtime Property isArchiveRequired set to " + isArchiveRequired);
	}

}
