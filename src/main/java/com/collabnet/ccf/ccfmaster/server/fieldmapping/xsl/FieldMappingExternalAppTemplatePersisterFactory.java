package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;

public interface FieldMappingExternalAppTemplatePersisterFactory {
	Persister<FieldMappingExternalAppTemplate> get(ConversionResult conversionResult);
}
