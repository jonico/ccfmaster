package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class PropertiesDirectionConfigPersisterFactory implements DirectionConfigPersisterFactory {

    private static final Logger log    = LoggerFactory
                                               .getLogger(PropertiesDirectionConfigPersisterFactory.class);
    public static final String  PREFIX = "ccf.direction.";
    private final File          basePath;

    public PropertiesDirectionConfigPersisterFactory(File basePath) {
        Assert.notNull(basePath);
        Assert.isTrue(basePath.exists() && basePath.isDirectory(),
                "base path must exist and be a directory");
        this.basePath = basePath;
    }

    public PropertiesDirectionConfigPersisterFactory(String basePath) {
        this(new File(basePath));
    }

    /**
     * produces Persisters that write to property files.
     * 
     * The files are located at ${ccf.home}/landscapeN/X2Y.properties, where:
     * <ul>
     * <li>N == direction.landscape.id</li>
     * <li>X == direction.landscape.teamForge.systemKind</li>
     * <li>Y == direction.landscape.participant.systemKind</li>
     * </ul>
     * 
     * the directory is created if it doesn't exist.
     */
    @Override
    public Persister<DirectionConfig> get(Direction direction) {
        String fileName;
        String baseName;
        switch (direction.getDirection()) {
            case FORWARD:
                baseName = "%1$s2%2$s"; // e.g. TF2QC
                break;
            case REVERSE:
                baseName = "%2$s2%1$s"; // e.g. QC2TF
                break;
            default:
                throw new IllegalArgumentException("unknown direction: "
                        + direction.getDirection());
        }
        final Landscape landscape = direction.getLandscape();
        final File propDir = new File(basePath, "landscape" + landscape.getId());
        if (!propDir.exists()) {
            propDir.mkdir();
        }
        fileName = String.format(baseName + ".properties",
                landscape.getTeamForge().getSystemKind(),
                landscape.getParticipant().getSystemKind()).toLowerCase();
        final File propFile = new File(propDir, fileName);
        log.debug("persisting to: {}", propFile);
        final PropertiesConfigItemPersister<DirectionConfig> persister = new PropertiesConfigItemPersister<DirectionConfig>(
                propFile);
        persister.setPropertyPrefix(PREFIX);
        return persister;
    }

}
