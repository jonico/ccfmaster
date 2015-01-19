package com.collabnet.ccf.ccfmaster.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class CCFRuntimePropertyHolder {

    /**
     * {@link RuntimePropertyNameEnum} holds collection of the ccfRuntime
     * property key names
     * 
     */
    public enum RuntimePropertyNameEnum {
        CCF_HOME("ccf.home"), CCF_BASE_URL("ccf.baseUrl"), CCF_DB_PORT(
                "ccf.db.port"), CCF_IAF_SERVICE_ENDPOINT(
                "ccf.iafServiceEndpoint"), CCF_FORWARD_JMXPORT(
                "ccf.forward.jmxport"), CCF_REVERSE_JMXPORT(
                "ccf.reverse.jmxport"), CCF_TF_URL("ccf.tf.url"), CCF_SAASMODE(
                "ccf.saasmode"), CCF_IS_ARCHIVE_REQUIRED(
                "ccf.isArchiveRequired"), CCF_MAX_ATTACHMENT_SIZE(
                "ccf.maxAttachment.size");

        private String propertyName;

        RuntimePropertyNameEnum(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    private static final String APPLICATION_CONTEXT_DEVELOPMENT_CCFRUNTIMEPROPERTIES_XML = "applicationContext-development-ccfruntimeproperties.xml";

    private static final String ENVIRONMENT_VARIABLE_CONFIGURATION                       = "environment/system property";

    private static final Logger log                                                      = LoggerFactory
                                                                                                 .getLogger(CCFRuntimePropertyHolder.class);

    private String              tfUrl;

    private String              ccfBaseUrl;

    private String              ccfHome;

    private String              maxAttachmentSize;

    private String              iafServiceEndpoint;

    private String              ccfDBPort;

    private String              jmxForwardPort;

    private String              jmxReversePort;

    private String              saasMode;

    private String              isArchiveRequired;

    private Properties          defaultRuntimePropvalues;

    public String getCcfBaseUrl() {
        return ccfBaseUrl;
    }

    public String getCcfDBPort() {
        return ccfDBPort;
    }

    public String getCcfHome() {
        return ccfHome;
    }

    public String getCCFHomeRuntimeConfigPath() {
        String ccfRuntimeConfigPath = String.format(
                "%s%sccfhomeruntimeconfig.properties", getCcfHome(),
                File.separator);
        File ccfRuntimeconfigFile = new File(ccfRuntimeConfigPath);
        if (!ccfRuntimeconfigFile.exists()) {
            String tfUrl = getConfiguredPropertyValue(RuntimePropertyNameEnum.CCF_TF_URL
                    .getPropertyName());
            return tfUrl == null ? APPLICATION_CONTEXT_DEVELOPMENT_CCFRUNTIMEPROPERTIES_XML
                    : ENVIRONMENT_VARIABLE_CONFIGURATION;
        }
        return ccfRuntimeConfigPath;
    }

    public Properties getDefaultRuntimePropvalues() {
        return defaultRuntimePropvalues;
    }

    public String getIafServiceEndpoint() {
        return iafServiceEndpoint;
    }

    public String getIsArchiveRequired() {
        return isArchiveRequired;
    }

    public String getJmxForwardPort() {
        return jmxForwardPort;
    }

    public String getJmxReversePort() {
        return jmxReversePort;
    }

    public String getMaxAttachmentSize() {
        return maxAttachmentSize;
    }

    public String getSaasMode() {
        return saasMode;
    }

    public String getTfUrl() {
        return tfUrl;
    }

    /**
     * Init method for {@link CCFRuntimePropertyHolder} . This method loads the
     * properties which are configured as system/environment variable. If
     * system/environment variable is not configured,looks up for fallback
     * ccfhome/ccf.conf file to load the properties.If any property missing from
     * the conf file, it loads the default value for the property.
     */
    public void initializeRuntimeProperties() {
        for (RuntimePropertyNameEnum propEnum : RuntimePropertyNameEnum
                .values()) {
            String propertyName = propEnum.getPropertyName();
            String runtimeValue = getConfiguredPropertyValue(propertyName);
            if (runtimeValue != null) {
                setPropertyValues(propEnum, runtimeValue);
            }
        }
        if (getCcfHome() == null) {
            String fallbackDir = getFallBackCcfHomePath();
            log.info("Set fallback directory as CCF home runtime property : "
                    + fallbackDir);
            setCcfHome(fallbackDir);
        }

        loadCcfConfProperties();
    }

    public void setCcfBaseUrl(String ccfBaseUrl) {
        this.ccfBaseUrl = ccfBaseUrl;
        log.info("CCF Runtime Property CCF Base URL set to " + ccfBaseUrl);
    }

    public void setCcfDBPort(String ccfDBPort) {
        this.ccfDBPort = ccfDBPort;
        log.info("CCF Runtime Property CCF DB port set to " + ccfDBPort);
    }

    public void setCcfHome(String ccfHome) {
        this.ccfHome = ccfHome;
        log.info("CCF Runtime Property CCF Home set to " + ccfHome);
    }

    public void setDefaultRuntimePropvalues(Properties defaultRuntimePropvalues) {
        this.defaultRuntimePropvalues = defaultRuntimePropvalues;
        log.info("CCF Runtime Property defaultRuntimePropvalues set to "
                + defaultRuntimePropvalues);
    }

    public void setIafServiceEndpoint(String iafServiceEndpoint) {
        this.iafServiceEndpoint = iafServiceEndpoint;
        log.info("CCF Runtime Property IAF Service Endpoint set to "
                + iafServiceEndpoint);
    }

    public void setIsArchiveRequired(String isArchiveRequired) {
        this.isArchiveRequired = isArchiveRequired;
        log.info("CCF Runtime Property isArchiveRequired set to "
                + isArchiveRequired);
    }

    public void setJmxForwardPort(String jmxForwardPort) {
        this.jmxForwardPort = jmxForwardPort;
        log.info("CCF Runtime Property JMX Forward Port set to "
                + jmxForwardPort);
    }

    public void setJmxReversePort(String jmxReversePort) {
        this.jmxReversePort = jmxReversePort;
        log.info("CCF Runtime Property JMX Reverse Port set to "
                + jmxReversePort);
    }

    public void setMaxAttachmentSize(String maxAttachmentSize) {
        this.maxAttachmentSize = maxAttachmentSize;
        log.info("CCF Runtime Property Default Max attachment size set to "
                + maxAttachmentSize);
    }

    public void setSaasMode(String saasMode) {
        this.saasMode = saasMode;
        log.info("CCF Runtime Property SAAS Mode set to " + saasMode);
    }

    public void setTfUrl(String tfUrl) {
        this.tfUrl = tfUrl;
        log.info("CCF Runtime Property TF-URL set to " + tfUrl);
    }

    private String getConfiguredPropertyValue(String propertyName) {
        String envValue = System.getenv(propertyName);
        return (envValue != null) ? envValue : System.getProperty(propertyName);
    }

    private String getFallBackCcfHomePath() {
        String ccfhomePath = null;
        String[] fallbackDir = {
                String.format("%s%sccfhome", FileUtils.getUserDirectoryPath(),
                        File.separator),
                String.format("%s%sccfhome", FileUtils.getTempDirectoryPath(),
                        File.separator),
                getDefaultRuntimePropvalues().getProperty(
                        RuntimePropertyNameEnum.CCF_HOME.getPropertyName()) };
        log.debug("Expected fallback directory list: {" + fallbackDir[0] + ", "
                + fallbackDir[1] + ", " + fallbackDir[2] + " }");
        for (String path : fallbackDir) {
            boolean isDefaultDirExist = new File(path).isDirectory();
            if (isDefaultDirExist) {
                ccfhomePath = path;
                break;
            }
        }
        return ccfhomePath;
    }

    /*
     * Loads properties available in ccfhome/ccf.conf file,if file not found.
     * Default properties are configured
     */
    private void loadCcfConfProperties() {
        Properties props = null;
        String ccfConfFilePath = String.format(
                "%s%sccfhomeruntimeconfig.properties", getCcfHome(),
                File.separator);
        Resource resource = new FileSystemResource(ccfConfFilePath);
        if (resource.exists()) {
            try {
                props = PropertiesLoaderUtils.loadProperties(resource);
                log.info("List of CCF runtime properties configured in the ccfhomeruntimeconfig.properties: "
                        + props.toString());
            } catch (IOException e) {
                log.error("Couldn't find ccfhomeruntimeconfig.properties file");
            }
        }
        for (RuntimePropertyNameEnum propEnum : RuntimePropertyNameEnum
                .values()) {
            String key = propEnum.getPropertyName();
            String newValue = null;
            if (props != null) {
                newValue = props.getProperty(key);
            }
            if (newValue == null) { //getting the default value
                newValue = getDefaultRuntimePropvalues().getProperty(key);
                log.debug("Default value for property- " + key + " : "
                        + newValue);
            }
            setPropertyValues(propEnum, newValue);
        }
    }

    /*
     * SetPropertyValues method assigns new property value only if it is null.
     * @param propName
     * @param value
     */
    private void setPropertyValues(RuntimePropertyNameEnum propName,
            String value) {
        if (propName == null)
            return;
        switch (propName) {
            case CCF_HOME:
                if (ccfHome == null) {
                    setCcfHome(value);
                }
                break;
            case CCF_BASE_URL:
                if (ccfBaseUrl == null) {
                    setCcfBaseUrl(value);
                }
                break;
            case CCF_DB_PORT:
                if (ccfDBPort == null) {
                    setCcfDBPort(value);
                }
                break;
            case CCF_TF_URL:
                if (tfUrl == null) {
                    setTfUrl(value);
                }
                break;
            case CCF_IAF_SERVICE_ENDPOINT:
                if (iafServiceEndpoint == null) {
                    setIafServiceEndpoint(value);
                }
                break;
            case CCF_FORWARD_JMXPORT:
                if (jmxForwardPort == null) {
                    setJmxForwardPort(value);
                }
                break;
            case CCF_REVERSE_JMXPORT:
                if (jmxReversePort == null) {
                    setJmxReversePort(value);
                }
                break;
            case CCF_SAASMODE:
                if (saasMode == null) {
                    setSaasMode(value);
                }
                break;
            case CCF_IS_ARCHIVE_REQUIRED:
                if (isArchiveRequired == null) {
                    setIsArchiveRequired(value);
                }
                break;
            case CCF_MAX_ATTACHMENT_SIZE:
                if (maxAttachmentSize == null) {
                    setMaxAttachmentSize(value);
                }
                break;
        }
    }

}
