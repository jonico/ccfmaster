package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class FieldMappingLandscapeTemplateList extends ForwardingList<FieldMappingLandscapeTemplate> {

	private List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate;

	public FieldMappingLandscapeTemplateList() {
		this(new ArrayList<FieldMappingLandscapeTemplate>());
	}
	public FieldMappingLandscapeTemplateList(List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplates) {
		this.setFieldMapping(fieldMappingLandscapeTemplates);
	}

	@Override
	protected List<FieldMappingLandscapeTemplate> delegate() {
		return getFieldMapping();
	}

	public void setFieldMapping(List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate) {
		this.fieldMappingLandscapeTemplate = fieldMappingLandscapeTemplate;
	}

	public List<FieldMappingLandscapeTemplate> getFieldMapping() {
		return fieldMappingLandscapeTemplate;
	}

}
