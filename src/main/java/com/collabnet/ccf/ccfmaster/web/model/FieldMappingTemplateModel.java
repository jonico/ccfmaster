package com.collabnet.ccf.ccfmaster.web.model;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

public class FieldMappingTemplateModel {

    private List<FieldMappingLandscapeTemplate>   fieldMappingLandscapeTemplate;

    private String                                fmLandscapeTemplate   = "";
    private String                                fmExternalAppTemplate = "";

    private List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate;

    private String                                templateType          = "connector";

    private String                                fieldmappingName      = "";

    private boolean                               linktoTemplate        = false;

    public List<FieldMappingExternalAppTemplate> getFieldMappingExternalAppTemplate() {
        return fieldMappingExternalAppTemplate;
    }

    public List<FieldMappingLandscapeTemplate> getFieldMappingLandscapeTemplate() {
        return fieldMappingLandscapeTemplate;
    }

    public String getFieldmappingName() {
        return fieldmappingName;
    }

    public String getFmExternalAppTemplate() {
        return fmExternalAppTemplate;
    }

    public String getFmLandscapeTemplate() {
        return fmLandscapeTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public boolean isLinktoTemplate() {
        return linktoTemplate;
    }

    public void setFieldMappingExternalAppTemplate(
            List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate) {
        this.fieldMappingExternalAppTemplate = fieldMappingExternalAppTemplate;
    }

    public void setFieldMappingLandscapeTemplate(
            List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate) {
        this.fieldMappingLandscapeTemplate = fieldMappingLandscapeTemplate;
    }

    public void setFieldmappingName(String fieldmappingName) {
        this.fieldmappingName = fieldmappingName.trim();
    }

    public void setFmExternalAppTemplate(String fmExternalAppTemplate) {
        this.fmExternalAppTemplate = fmExternalAppTemplate;
    }

    public void setFmLandscapeTemplate(String fmLandscapeTemplate) {
        this.fmLandscapeTemplate = fmLandscapeTemplate;
    }

    public void setLinktoTemplate(boolean linktoTemplate) {
        this.linktoTemplate = linktoTemplate;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

}
