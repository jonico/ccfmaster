package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

@ContextConfiguration
public class PropertiesDirectionConfigPersisterFactoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private DirectionDataOnDemand dod;

    @Test
    public void checkFileName() {
        final Direction direction = dod.getNewTransientDirection(42);
        direction.setDirection(Directions.FORWARD);
        final Landscape landscape = direction.getLandscape();
        final File baseDir = new File(System.getProperty("java.io.tmpdir"));
        PropertiesDirectionConfigPersisterFactory factory = new PropertiesDirectionConfigPersisterFactory(
                baseDir);

        final PropertiesConfigItemPersister<DirectionConfig> persister = (PropertiesConfigItemPersister<DirectionConfig>) factory
                .get(direction);
        final File propFile = persister.getPropFile();
        final File propDir = new File(baseDir, "landscape" + landscape.getId());
        final String fileName = landscape.getTeamForge().getSystemKind() + "2"
                + landscape.getParticipant().getSystemKind() + ".properties";
        assertEquals(new File(propDir, fileName), propFile);
        assertTrue("directory wasn't created", propDir.exists());
        assertTrue("directory contains data", propDir.delete());
    }
}
