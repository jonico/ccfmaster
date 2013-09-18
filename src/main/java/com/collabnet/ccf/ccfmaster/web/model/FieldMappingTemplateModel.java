package com.collabnet.ccf.ccfmaster.web.model;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;





public class FieldMappingTemplateModel {

	private List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate;
	
	private List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate;
	
	private String templateType="connector";
	
	private String fieldmappingName="";
	
	private boolean linktoTemplate=false;

	public List<FieldMappingLandscapeTemplate> getFieldMappingLandscapeTemplate() {
		return fieldMappingLandscapeTemplate;
	}

	public void setFieldMappingLandscapeTemplate(
			List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate) {
		this.fieldMappingLandscapeTemplate = fieldMappingLandscapeTemplate;
	}

	public List<FieldMappingExternalAppTemplate> getFieldMappingExternalAppTemplate() {
		return fieldMappingExternalAppTemplate;
	}

	public void setFieldMappingExternalAppTemplate(
			List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate) {
		this.fieldMappingExternalAppTemplate = fieldMappingExternalAppTemplate;
	}

	public String getFieldmappingName() {
		return fieldmappingName;
	}

	public void setFieldmappingName(String fieldmappingName) {
		this.fieldmappingName = fieldmappingName;
	}

	public boolean isLinktoTemplate() {
		return linktoTemplate;
	}

	public void setLinktoTemplate(boolean linktoTemplate) {
		this.linktoTemplate = linktoTemplate;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	
	
	
	
	
}
