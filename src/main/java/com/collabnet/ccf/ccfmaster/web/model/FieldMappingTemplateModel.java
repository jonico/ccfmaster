package com.collabnet.ccf.ccfmaster.web.model;


public class FieldMappingTemplateModel {

    private String  fmLandscapeTemplate   = "";

    private String  fmExternalAppTemplate = "";

    private String  templateType          = "connector";

    private String  fieldmappingName      = "";

    private boolean linktoTemplate        = false;

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
