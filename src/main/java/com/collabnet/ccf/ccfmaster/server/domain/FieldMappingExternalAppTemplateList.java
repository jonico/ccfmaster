package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class FieldMappingExternalAppTemplateList extends ForwardingList<FieldMappingExternalAppTemplate> {

	private List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate;

	public FieldMappingExternalAppTemplateList() {
		this(new ArrayList<FieldMappingExternalAppTemplate>());
	}
	public FieldMappingExternalAppTemplateList(List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplates) {
		this.setFieldMapping(fieldMappingExternalAppTemplates);
	}

	@Override
	protected List<FieldMappingExternalAppTemplate> delegate() {
		return getFieldMapping();
	}

	public void setFieldMapping(List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate) {
		this.fieldMappingExternalAppTemplate = fieldMappingExternalAppTemplate;
	}

	public List<FieldMappingExternalAppTemplate> getFieldMapping() {
		return fieldMappingExternalAppTemplate;
	}

}
