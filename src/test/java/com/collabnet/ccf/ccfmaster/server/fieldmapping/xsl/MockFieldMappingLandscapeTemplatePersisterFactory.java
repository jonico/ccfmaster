package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

public class MockFieldMappingLandscapeTemplatePersisterFactory implements FieldMappingLandscapeTemplatePersisterFactory {
    private static Logger log        = LoggerFactory
                                             .getLogger(MockFieldMappingLandscapeTemplatePersisterFactory.class);
    public boolean        calledSave = false;

    @Override
    public Persister<FieldMappingLandscapeTemplate> get(
            ConversionResult conversionResult) {
        return new Persister<FieldMappingLandscapeTemplate>() {
            @Override
            public void delete(FieldMappingLandscapeTemplate cfg) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void save(FieldMappingLandscapeTemplate cfg) {
                calledSave = true;
                log.debug("called save({})", cfg);
            }
        };
    }

}
