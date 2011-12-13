package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement(name="fieldMappingTemplateList")
public class FieldMappingLandscapeTemplateList extends ForwardingList<FieldMappingLandscapeTemplate> {

	private List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate;

	public FieldMappingLandscapeTemplateList() {
		this(new ArrayList<FieldMappingLandscapeTemplate>());
	}
	public FieldMappingLandscapeTemplateList(List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplates) {
		this.setFieldMappingTemplate(fieldMappingLandscapeTemplates);
	}

	@Override
	protected List<FieldMappingLandscapeTemplate> delegate() {
		return getFieldMappingTemplate();
	}

	public void setFieldMappingTemplate(List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate) {
		this.fieldMappingLandscapeTemplate = fieldMappingLandscapeTemplate;
	}

	public List<FieldMappingLandscapeTemplate> getFieldMappingTemplate() {
		return fieldMappingLandscapeTemplate;
	}

}
