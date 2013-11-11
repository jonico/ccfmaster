package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Template;

public class FieldMappingLandscapeTemplatePersister extends AbstractPersister<Template<Landscape>> implements Persister<FieldMappingLandscapeTemplate> {

    public FieldMappingLandscapeTemplatePersister(File baseDir,
            ConversionResult conversionResult) {
        super(baseDir, conversionResult);
    }

    @Override
    public void delete(FieldMappingLandscapeTemplate cfg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(FieldMappingLandscapeTemplate cfg) {
        doSave(cfg);
    }

}