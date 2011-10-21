package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

public interface FieldMappingLandscapeTemplatePersisterFactory {
	Persister<FieldMappingLandscapeTemplate> get(ConversionResult conversionResult);
}
