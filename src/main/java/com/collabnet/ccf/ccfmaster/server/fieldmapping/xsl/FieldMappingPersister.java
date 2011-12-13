package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingScope;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;

public class FieldMappingPersister extends AbstractPersister<Mapping<RepositoryMappingDirection>> implements Persister<FieldMapping>{

	public FieldMappingPersister(File baseDir, ConversionResult conversionResult) {
		super(baseDir, conversionResult);
	}

	@Override
	public void save(FieldMapping cfg) {
		if (cfg.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION) {
			// templates and static mappings are handled elsewhere.
			doSave(cfg);
		}
	}

	@Override
	public void delete(FieldMapping cfg) {
		throw new UnsupportedOperationException();
	}
	
}