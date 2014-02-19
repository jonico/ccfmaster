package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

@ContextConfiguration
public class StorageDirectoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private FieldMappingDataOnDemand                    fmDod;
    @Autowired
    private FieldMappingExternalAppTemplateDataOnDemand fmeatDod;
    @Autowired
    private FieldMappingLandscapeTemplateDataOnDemand   fmltDod;

    private File                                        baseDir = null;

    @Before
    public void createTempDir() throws IOException {
        baseDir = File.createTempFile(StorageDirectoryTest.class.getName(),
                "dir");
        baseDir.delete();
        baseDir.mkdir();
    }

    @After
    public void removeTempDir() throws IOException {
        FileUtils.forceDelete(baseDir);
    }

    @Test
    public void testFieldMappingDirectory() {
        final FieldMapping fm = fmDod.getNewTransientFieldMapping(0);
        final RepositoryMappingDirection rmd = fm.getParent();
        final File dir = mappingStorageDirectoryTests(fm);
        assertTrue("RMD direction not in directory",
                dir.toString().contains(rmd.getDirection().toString()));
    }

    @Test
    public void testFieldMappingExternalAppTemplateDirectory() {
        final FieldMappingExternalAppTemplate fmeat = fmeatDod
                .getNewTransientFieldMappingExternalAppTemplate(0);
        final File dir = mappingStorageDirectoryTests(fmeat);
        assertTrue(
                "template direction not in directory " + dir,
                dir.toString()
                        .toLowerCase()
                        .contains(fmeat.getDirection().toString().toLowerCase()));
        assertTrue("linkId not in directory " + dir, dir.toString()
                .toLowerCase().matches(".*?prpl\\d+.*"));
    }

    @Test
    public void testFieldMappingLandscapeTemplateDirectory() {
        final FieldMappingLandscapeTemplate fmeat = fmltDod
                .getNewTransientFieldMappingLandscapeTemplate(0);
        final File dir = mappingStorageDirectoryTests(fmeat);
        assertTrue(
                "template direction not in directory " + dir,
                dir.toString()
                        .toLowerCase()
                        .contains(fmeat.getDirection().toString().toLowerCase()));
    }

    private File mappingStorageDirectoryTests(Mapping<?> mapping) {
        File dir = mapping.getStorageDirectory(baseDir);
        System.out.println(dir);
        //		assertTrue(dir + " doesn't exist " + dir, dir.exists());
        //		assertTrue(dir + " is not a directory " + dir, dir.isDirectory());
        assertTrue("landscape not in directory path " + dir, dir.toString()
                .toLowerCase().matches(".*?landscape\\d+.*"));
        return dir;
    }
}
