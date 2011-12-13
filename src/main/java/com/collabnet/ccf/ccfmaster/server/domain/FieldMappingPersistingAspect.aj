package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResultFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingExternalAppTemplatePersisterFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingLandscapeTemplatePersisterFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingPersisterFactory;

public aspect FieldMappingPersistingAspect {

	private interface CanPersistToXsl {}
	@Autowired
	private transient ConversionResultFactory CanPersistToXsl.conversionResultFactory;
	public ConversionResultFactory CanPersistToXsl.getConversionResultFactory() {
		return conversionResultFactory;
	}
	
	declare parents: FieldMapping implements CanPersistToXsl;
	
//	@Autowired
//	public transient FieldMappingPersisterFactory CanPersistToXsl.persisterFactory;
//	@Autowired
//	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingExternalAppTemplate.persisterFactory;
//
//	@Autowired
//	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingLandscapeTemplate.persisterFactory;

	@Autowired
	private transient FieldMappingPersisterFactory FieldMapping.persisterFactory;
	
	public FieldMappingPersisterFactory FieldMapping.getPersisterFactory() {
		return persisterFactory;
	}
	
	public void FieldMapping.setPersisterFactory(FieldMappingPersisterFactory factory) {
		persisterFactory = factory;
	}
	
	void around(FieldMapping cfg) : execution(void FieldMapping.persist()) && target(cfg) {
		proceed(cfg);
		if (cfg.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION) {
			ConversionResult cr = cfg.conversionResultFactory.get(cfg);
			Persister<FieldMapping> persister = cfg.persisterFactory.get(cr);
			persister.save(cfg);
		}
	}
	FieldMapping around(FieldMapping cfg) : execution(FieldMapping FieldMapping.merge()) && target(cfg) {
		FieldMapping result = proceed(cfg);
		if (result.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION) {
			ConversionResult cr = result.conversionResultFactory.get(result);
			Persister<FieldMapping> persister = result.persisterFactory.get(cr);
			persister.save(result);
		}
		return result;
	}

	@Autowired
	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingLandscapeTemplate.persisterFactory;
	public void FieldMappingLandscapeTemplate.setPersisterFactory(FieldMappingLandscapeTemplatePersisterFactory fmltpf) {
		persisterFactory = fmltpf;
	}
	@Autowired
	private transient FieldMappingExternalAppTemplatePersisterFactory FieldMappingExternalAppTemplate.persisterFactory;
	public void FieldMappingExternalAppTemplate.setPersisterFactory(FieldMappingExternalAppTemplatePersisterFactory fmeatpf) {
		persisterFactory = fmeatpf;
	}
	
	declare parents: FieldMappingLandscapeTemplate implements CanPersistToXsl;
	declare parents: FieldMappingExternalAppTemplate implements CanPersistToXsl;
	
	void around(FieldMappingLandscapeTemplate template) : execution(void FieldMappingLandscapeTemplate.persist()) && target(template) {
		proceed(template);
		ConversionResult cr = template.conversionResultFactory.get(template);
		Persister<FieldMappingLandscapeTemplate> persister = template.persisterFactory.get(cr);
		persister.save(template);
	}
	FieldMappingLandscapeTemplate around(FieldMappingLandscapeTemplate template) : execution(FieldMappingLandscapeTemplate FieldMappingLandscapeTemplate.merge()) && target(template) {
		FieldMappingLandscapeTemplate result = proceed(template);
		ConversionResult cr = result.conversionResultFactory.get(result);
		Persister<FieldMappingLandscapeTemplate> persister = result.persisterFactory.get(cr);
		persister.save(result);
		return result;
	}
	
	void around(FieldMappingExternalAppTemplate template) : execution(void FieldMappingExternalAppTemplate.persist()) && target(template) {
		proceed(template);
		ConversionResult cr = template.conversionResultFactory.get(template);
		Persister<FieldMappingExternalAppTemplate> persister = template.persisterFactory.get(cr);
		persister.save(template);
	}
	FieldMappingExternalAppTemplate around(FieldMappingExternalAppTemplate template) : execution(FieldMappingExternalAppTemplate FieldMappingExternalAppTemplate.merge()) && target(template) {
		FieldMappingExternalAppTemplate result = proceed(template);
		ConversionResult cr = result.conversionResultFactory.get(result);
		Persister<FieldMappingExternalAppTemplate> persister = result.persisterFactory.get(cr);
		persister.save(result);
		return result;
	}
}
