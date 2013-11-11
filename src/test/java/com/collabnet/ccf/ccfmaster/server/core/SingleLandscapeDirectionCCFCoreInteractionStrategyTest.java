package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;

@ContextConfiguration()
public class SingleLandscapeDirectionCCFCoreInteractionStrategyTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String                        TESTPROPERTY              = "testproperty";

    private static final String                        WRAPPER_JAVA_ADDITIONAL_2 = "wrapper.java.additional.2";

    private String[]                                   propertyNames             = {
            WRAPPER_JAVA_ADDITIONAL_2, TESTPROPERTY                             };

    @Autowired
    private DirectionDataOnDemand                      dod;

    private Direction                                  direction;

    File                                               directoryToDelete;

    private File                                       ccfHomeDirectory;

    @Autowired
    SingleLandscapeDirectionCCFCoreInteractionStrategy wiredInStrategy;

    private File                                       wrapperFile;

    @After
    public void deleteDirectoryIfNecessary() throws IOException {
        if (directoryToDelete != null) {
            FileUtils.deleteDirectory(directoryToDelete);
        }
    }

    @Before
    public void setDirectoryToDeleteToNull() {
        directoryToDelete = null;
    }

    public void testCreateWithDB(String[] expectedProperties,
            String[] expectedValues) throws IOException {
        ccfHomeDirectory = new File(wiredInStrategy.getCcfHome());
        directoryToDelete = ccfHomeDirectory;
        wrapperFile = new File(
                ccfHomeDirectory,
                "landscape"
                        + direction.getLandscape().getId()
                        + File.separator
                        + wiredInStrategy
                                .getImmutableDirectionWrapperFileName(direction));
        Properties wrapperProperties = new Properties();
        FileInputStream inStream = new FileInputStream(wrapperFile);
        wrapperProperties.load(inStream);
        inStream.close();
        for (int i = 0; i < expectedProperties.length; ++i) {
            assertEquals(expectedValues[i],
                    wrapperProperties.get(propertyNames[i]));
        }
    }

    @Test
    public void testCreateWithDBForward() throws IOException {
        direction = dod.getSpecificDirection(0);
        testCreateWithDB(propertyNames,
                new String[] { "-Dcom.sun.management.jmxremote.port=8001",
                        direction.getDescription() });
    }

    @Test
    public void testCreateWithDBReverse() throws IOException {
        direction = dod.getSpecificDirection(1);
        testCreateWithDB(propertyNames,
                new String[] { "-Dcom.sun.management.jmxremote.port=8002",
                        direction.getDescription() });
    }

    @Test
    public void testUpdateWithDBForward() throws IOException {
        testCreateWithDBForward();
        String updatedDescription = "updatedDescriptionForward";
        direction.setDescription(updatedDescription);
        direction.merge();
        testCreateWithDB(propertyNames,
                new String[] { "-Dcom.sun.management.jmxremote.port=8001",
                        updatedDescription });
    }

    @Test
    public void testUpdateWithDBReverse() throws IOException {
        testCreateWithDBReverse();
        String updatedDescription = "updatedDescriptionReverse";
        direction.setDescription(updatedDescription);
        direction.merge();
        testCreateWithDB(propertyNames,
                new String[] { "-Dcom.sun.management.jmxremote.port=8002",
                        updatedDescription });
    }

    /*
     * @Test public void testUpdateWithDB() throws IOException {
     * testCreateWithDB(); landscape.setDescription("Updated description");
     * landscape.setPlugId("plug1234"); landscape.merge(); FileInputStream
     * inStream = new FileInputStream(landscapePropertiesFile); Properties
     * landscapeIdProperties = new Properties();
     * landscapeIdProperties.load(inStream); inStream.close();
     * assertEquals(landscape.getId().toString(),
     * landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy
     * .CCF_LANDSCAPE_ID)); assertEquals(landscape.getPlugId(),
     * landscapeIdProperties
     * .get(SingleLandscapeCCFCoreInteractionStrategy.CCF_LANDSCAPE_PLUG_ID));
     * assertEquals(landscape.getDescription(),
     * landscapeIdProperties.get(SingleLandscapeCCFCoreInteractionStrategy
     * .CCF_LANDSCAPE_DESCRIPTION)); }
     * @Test public void testDeleteWithDB() throws IOException {
     * testCreateWithDB(); landscape.remove(); // File samplesDirectory = new
     * File (ccfHomeDirectory, "landscape" + // landscape.getId() +
     * File.separator + "samples"); // assertTrue(samplesDirectory +
     * " does still exist.", // samplesDirectory.exists()); File
     * archivedSamplesDirectory = new File(ccfHomeDirectory, "archive" +
     * File.separator + "landscape" + landscape.getId() + File.separator +
     * "samples"); assertTrue(archivedSamplesDirectory + " doesn't exist.",
     * archivedSamplesDirectory.exists()); }
     */

}
