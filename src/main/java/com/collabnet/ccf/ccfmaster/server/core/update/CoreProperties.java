package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.collabnet.ccf.ccfmaster.config.Version;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public final class CoreProperties implements Supplier<Properties>, Serializable {

    private static final long serialVersionUID          = 1L;
    static final String       META_INFORMATION_FILENAME = "ccfcoreversion.properties";
    static final String       CCFCORE_DESCRIPTION       = "ccfcore.description";

    private Properties        props                     = null;

    @VisibleForTesting
    CoreProperties(Properties props) {
        this.props = Preconditions.checkNotNull(props);
    }

    @Override
    public Properties get() {
        return props;
    }

    /**
     * 
     * @return the core description, as contained in the core properties file
     */
    public String getDescription() {
        return get().getProperty(CCFCORE_DESCRIPTION);
    }

    public Version getVersion() {
        return Version.of(get());
    }

    public static CoreProperties of(File file) throws ZipException {
        Properties props = new Properties();
        try {
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file);
                ZipEntry entry = zipFile.getEntry(META_INFORMATION_FILENAME);
                if (entry != null) {
                    props.load(zipFile.getInputStream(entry));
                }
            } finally {
                if (zipFile != null)
                    zipFile.close();
            }
        } catch (ZipException e) {
            throw e;
        } catch (IOException ignored) {
        }
        return new CoreProperties(props);
    }

    public static CoreProperties ofDirectory(File landscapeDir) {
        Properties props = new Properties();
        File propFile = new File(landscapeDir, META_INFORMATION_FILENAME);
        if (propFile.exists()) {
            try {
                FileInputStream is = null;
                try {
                    is = new FileInputStream(propFile);
                    props.load(is);
                } finally {
                    if (is != null)
                        is.close();
                }
            } catch (IOException ignored) {
            }
        }
        return new CoreProperties(props);
    }
}