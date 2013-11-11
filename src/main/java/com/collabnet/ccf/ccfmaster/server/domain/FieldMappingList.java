package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class FieldMappingList extends ForwardingList<FieldMapping> {

    private List<FieldMapping> fieldMapping;

    public FieldMappingList() {
        this(new ArrayList<FieldMapping>());
    }

    public FieldMappingList(List<FieldMapping> fieldMappings) {
        this.setFieldMapping(fieldMappings);
    }

    public List<FieldMapping> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(List<FieldMapping> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    @Override
    protected List<FieldMapping> delegate() {
        return getFieldMapping();
    }

}
