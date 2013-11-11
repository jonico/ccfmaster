package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.ConfigItem;

public class PropertiesConfigItemPersister<T extends ConfigItem> implements Persister<T> {

    private final File propFile;
    private String     propertyPrefix = "";

    /**
     * Stores ConfigItems in property files located in the rootPath directory.
     * 
     * The file names of the property files are the lower-case systemKind of the
     * Participant of the ConfigItem that is passed to the add/update/delete
     * methods.
     * 
     * @param propFile
     *            the directory in which we store the property files.
     * @throws IllegalArgumentException
     *             if rootPath is null, doesn't exist or isn't a directory.
     */
    public PropertiesConfigItemPersister(final File propFile) {
        Assert.notNull(propFile);
        this.propFile = propFile;
    }

    @Override
    public void delete(T conf) {
        try {
            Properties props = loadProperties(getPropFile());
            props.remove(conf.getName());
            saveProperties(props, getPropFile());
        } catch (IOException e) {
            throw new CoreConfigurationException(String.format(
                    "error removing '%s' to %s: %s", conf.getName(),
                    getPropFile().getAbsolutePath(), e.getMessage()), e);
        }
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public File getPropFile() {
        return propFile;
    }

    @Override
    public void save(T conf) {
        Assert.isTrue(conf.getName().startsWith(propertyPrefix), String.format(
                "setting name must start with '%s', was: ", propertyPrefix,
                conf.getName()));
        try {
            Properties props = loadProperties(getPropFile());
            props.setProperty(conf.getName(), conf.getVal());
            saveProperties(props, getPropFile());
        } catch (IOException e) {
            throw new CoreConfigurationException(String.format(
                    "error saving '%s'='%s' to %s: %s", conf.getName(),
                    conf.getVal(), getPropFile().getAbsolutePath(),
                    e.getMessage()), e);
        }
    }

    /**
     * sets the propertyPrefix. If set, calls to save() will validate that the
     * property name starts with propertyPrefix.
     * 
     * @param propertyPrefix
     *            may not be null. Use
     */
    public void setPropertyPrefix(String propertyPrefix) {
        Assert.notNull(propertyPrefix, "prefix must not be null");
        this.propertyPrefix = propertyPrefix;
    }

    Properties loadProperties(File propFile) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        try {
            if (propFile.exists()) {
                in = new FileInputStream(propFile);
                props.load(in);
                in.close();
            }
        } finally {
            if (in != null)
                in.close();
        }
        return props;
    }

    void saveProperties(Properties props, File propFile) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(propFile);
            props.store(out, null);
        } finally {
            if (out != null)
                out.close();
        }
    }

}
