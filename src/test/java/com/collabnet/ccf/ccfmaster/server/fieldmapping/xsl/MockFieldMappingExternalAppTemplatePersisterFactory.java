package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;

public class MockFieldMappingExternalAppTemplatePersisterFactory implements FieldMappingExternalAppTemplatePersisterFactory {
	private static Logger log = LoggerFactory.getLogger(MockFieldMappingExternalAppTemplatePersisterFactory.class);
	public boolean calledSave = false;
	@Override
	public Persister<FieldMappingExternalAppTemplate> get(ConversionResult conversionResult) {
		return new Persister<FieldMappingExternalAppTemplate>() {
			
			@Override
			public void save(FieldMappingExternalAppTemplate cfg) {
				calledSave = true;
				log.debug("called save({})", cfg);
			}
			
			@Override
			public void delete(FieldMappingExternalAppTemplate cfg) {
				throw new UnsupportedOperationException();
			}
		};
	}

}
