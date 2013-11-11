package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Template;

public class FieldMappingExternalAppTemplatePersister extends AbstractPersister<Template<ExternalApp>> implements Persister<FieldMappingExternalAppTemplate> {

    public FieldMappingExternalAppTemplatePersister(File baseDir,
            ConversionResult conversionResult) {
        super(baseDir, conversionResult);
    }

    @Override
    public void delete(FieldMappingExternalAppTemplate cfg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(FieldMappingExternalAppTemplate cfg) {
        doSave(cfg);
    }

}