package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.collabnet.ccf.ccfmaster.config.Version;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import static org.junit.Assert.*;

public class CoreZipFileTest {

    private static final String VALID_ZIP_FILE_NAME   = "valid.zip";
    private static final String INVALID_ZIP_FILE_NAME = "invalid.zip";
    private static final String BAD_ZIP_FILE_NAME     = "bad.zip";

    @Rule
    public TemporaryFolder      folder                = new TemporaryFolder();

    private File                propFile              = null;

    @Before
    public void init() throws IOException {
        propFile = folder.newFile(CoreProperties.META_INFORMATION_FILENAME);
    }

    @Test(expected = ZipException.class)
    public void unzipBadZipFileThrowsZipFileException() throws ZipException,
            IOException {
        CoreZipFile czf = null;
        try {
            czf = createCoreZipFile(BAD_ZIP_FILE_NAME);
        } finally {
            Closeables.closeQuietly(czf);
        }
    }

    @Test
    public void unzipCreatesNewFiles() throws IOException {
        writeCorePropFile(propFile, new Version(0, 0, 0, ""), "foo");
        CoreZipFile czf = null;
        boolean threw = true;
        try {
            czf = createCoreZipFile(VALID_ZIP_FILE_NAME);
            assertNotNull("creating CoreZipFile failed.", czf);
            assertTrue("validating valid zip file failed", czf.validate());
            final File root = folder.getRoot();
            int before = root.list().length;
            czf.unzipTo(root);
            int after = root.list().length;
            assertTrue("no new files in " + root, before < after);
            threw = false;
        } finally {
            Closeables.close(czf, threw);
        }
    }

    @Test(expected = CoreUpdateException.class)
    public void unzipDoesntOverwriteNewerLandscape() throws IOException {
        writeCorePropFile(propFile, new Version(200, 0, 0, ""), "foo");
        CoreZipFile czf = null;
        boolean threw = true;
        try {
            czf = createCoreZipFile(VALID_ZIP_FILE_NAME);
            czf.unzipTo(folder.getRoot());
            threw = false;
        } finally {
            Closeables.close(czf, threw);
        }
    }

    @Test
    public void unzipInvalidZipFileDoesNotValidate() throws ZipException,
            IOException {
        CoreZipFile czf = null;
        boolean threw = true;
        try {
            czf = createCoreZipFile(INVALID_ZIP_FILE_NAME);
            assertNotNull("creating CoreZipFile failed", czf);
            assertFalse("validating invalid zip should fail", czf.validate());
            threw = false;
        } finally {
            Closeables.close(czf, threw);
        }
    }

    private void writeCorePropFile(File propFile, Version version, String desc)
            throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.setProperty(Version.CCFCORE_MAJOR_VERSION,
                Integer.toString(version.getMajor()));
        props.setProperty(Version.CCFCORE_MINOR_VERSION,
                Integer.toString(version.getMinor()));
        props.setProperty(Version.CCFCORE_PATCH_VERSION,
                Integer.toString(version.getPatch()));
        props.setProperty(Version.CCFCORE_REVISION_STRING,
                version.getRevision());
        props.setProperty(CoreProperties.CCFCORE_DESCRIPTION, desc);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(propFile);
            props.store(out, null);
        } finally {
            if (out != null)
                out.close();
        }
    }

    CoreZipFile createCoreZipFile(String fileName) throws IOException,
            ZipException {
        URL testCoreZip = Resources
                .getResource(CoreZipFileTest.class, fileName);
        MultipartFile upload = new MockMultipartFile("file", Resources
                .newInputStreamSupplier(testCoreZip).getInput());
        return CoreZipFile.fromMultipartFile(upload);
    }

}
