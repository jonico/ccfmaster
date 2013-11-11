package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;

public class FieldMappingExternalAppTemplatePersisterFactoryImpl implements FieldMappingExternalAppTemplatePersisterFactory {

    private final File baseDir;

    public FieldMappingExternalAppTemplatePersisterFactoryImpl(
            final File baseDir) {
        Assert.notNull(baseDir, "baseDir cannot be null");
        Assert.isTrue(baseDir.exists(), "baseDir must exist");
        Assert.isTrue(baseDir.isDirectory(), "baseDir must be a directory");
        this.baseDir = baseDir;
    }

    public FieldMappingExternalAppTemplatePersisterFactoryImpl(
            final String baseDirName) {
        this(new File(baseDirName));
    }

    @Override
    public Persister<FieldMappingExternalAppTemplate> get(
            ConversionResult conversionResult) {
        return new FieldMappingExternalAppTemplatePersister(baseDir,
                conversionResult);
    }

}
