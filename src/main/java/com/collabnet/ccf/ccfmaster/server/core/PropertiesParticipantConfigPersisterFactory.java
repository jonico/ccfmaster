package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;

public class PropertiesParticipantConfigPersisterFactory implements PersisterFactory<ParticipantConfig, Participant>, ParticipantConfigPersisterFactory {

    private final File         basePath;
    public static final String PREFIX = "ccf.participant.";

    public PropertiesParticipantConfigPersisterFactory(File basePath) {
        Assert.notNull(basePath);
        Assert.isTrue(basePath.exists() && basePath.isDirectory());
        this.basePath = basePath;
    }

    public PropertiesParticipantConfigPersisterFactory(String basePath) {
        this(new File(basePath));
    }

    /**
     * produces Persisters that write to property files.
     * 
     * The files are located at ${ccf.home}/K-participant.properties, where:
     * <ul>
     * <li>K == participant.systemKind</li>
     * </ul>
     */
    @Override
    public Persister<ParticipantConfig> get(Participant participant) {
        String fileName = (participant.getSystemKind() + "-participant.properties")
                .toLowerCase();
        final PropertiesConfigItemPersister<ParticipantConfig> persister = new PropertiesConfigItemPersister<ParticipantConfig>(
                new File(basePath, fileName));
        persister.setPropertyPrefix(PREFIX);
        return persister;
    }

}
