package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement(name="fieldMappingTemplateList")
public class FieldMappingExternalAppTemplateList extends ForwardingList<FieldMappingExternalAppTemplate> {

	private List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate;

	public FieldMappingExternalAppTemplateList() {
		this(new ArrayList<FieldMappingExternalAppTemplate>());
	}
	public FieldMappingExternalAppTemplateList(List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplates) {
		this.setFieldMappingTemplate(fieldMappingExternalAppTemplates);
	}

	@Override
	protected List<FieldMappingExternalAppTemplate> delegate() {
		return getFieldMappingTemplate();
	}

	public void setFieldMappingTemplate(List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate) {
		this.fieldMappingExternalAppTemplate = fieldMappingExternalAppTemplate;
	}

	public List<FieldMappingExternalAppTemplate> getFieldMappingTemplate() {
		return fieldMappingExternalAppTemplate;
	}

}
