package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;

import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

public class FieldMappingLandscapeTemplatePersisterFactoryImpl implements FieldMappingLandscapeTemplatePersisterFactory {

	private final File baseDir;
	
	public FieldMappingLandscapeTemplatePersisterFactoryImpl(final String baseDirName) {
		this(new File(baseDirName));
	}

	public FieldMappingLandscapeTemplatePersisterFactoryImpl(final File baseDir) {
		Assert.notNull(baseDir, "baseDir cannot be null");
		Assert.isTrue(baseDir.exists(), "baseDir must exist");
		Assert.isTrue(baseDir.isDirectory(), "baseDir must be a directory");
		this.baseDir = baseDir;
	}

	@Override
	public Persister<FieldMappingLandscapeTemplate> get(ConversionResult conversionResult) {
		return new FieldMappingLandscapeTemplatePersister(baseDir, conversionResult);
	}

}
