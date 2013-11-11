package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;

public class PropertiesLandscapeConfigPersisterFactory implements LandscapeConfigPersisterFactory {

    private final File         basePath;
    public static final String PREFIX = "ccf.landscape.";

    public PropertiesLandscapeConfigPersisterFactory(File basePath) {
        Assert.notNull(basePath, "basePath may not be null");
        Assert.isTrue(basePath.exists() && basePath.isDirectory(),
                "basePath must exist and be a directory");
        this.basePath = basePath;
    }

    public PropertiesLandscapeConfigPersisterFactory(String basePath) {
        this(new File(basePath));
    }

    /**
     * produces Persisters that write to property files.
     * 
     * The files are located at ${ccf.home}/landscapeN/landscape.properties,
     * where:
     * <ul>
     * <li>N == landscape.id</li>
     * </ul>
     * 
     * the landscapeN directory is created if it doesn't exist.
     */
    @Override
    public Persister<LandscapeConfig> get(Landscape landscape) {
        String fileName = "landscape.properties";
        File propDir = new File(basePath, "landscape" + landscape.getId());
        if (!propDir.exists()) {
            propDir.mkdir();
        }
        final PropertiesConfigItemPersister<LandscapeConfig> persister = new PropertiesConfigItemPersister<LandscapeConfig>(
                new File(propDir, fileName));
        persister.setPropertyPrefix(PREFIX);
        return persister;
    }

}
