// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import java.lang.String;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

privileged aspect FieldMappingLandscapeTemplateController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByDirection", "form" }, method = RequestMethod.GET)
    public String FieldMappingLandscapeTemplateController.findFieldMappingLandscapeTemplatesByDirectionForm(Model uiModel) {
        uiModel.addAttribute("directionses", java.util.Arrays.asList(Directions.class.getEnumConstants()));
        return "fieldmappinglandscapetemplates/findFieldMappingLandscapeTemplatesByDirection";
    }
    
    @RequestMapping(params = { "find=ByParent", "form" }, method = RequestMethod.GET)
    public String FieldMappingLandscapeTemplateController.findFieldMappingLandscapeTemplatesByParentForm(Model uiModel) {
        uiModel.addAttribute("landscapes", Landscape.findAllLandscapes());
        return "fieldmappinglandscapetemplates/findFieldMappingLandscapeTemplatesByParent";
    }
    
    @RequestMapping(params = { "find=ByParentAndDirection", "form" }, method = RequestMethod.GET)
    public String FieldMappingLandscapeTemplateController.findFieldMappingLandscapeTemplatesByParentAndDirectionForm(Model uiModel) {
        uiModel.addAttribute("landscapes", Landscape.findAllLandscapes());
        uiModel.addAttribute("directionses", java.util.Arrays.asList(Directions.class.getEnumConstants()));
        return "fieldmappinglandscapetemplates/findFieldMappingLandscapeTemplatesByParentAndDirection";
    }
    
    @RequestMapping(params = { "find=ByParentAndNameAndDirection", "form" }, method = RequestMethod.GET)
    public String FieldMappingLandscapeTemplateController.findFieldMappingLandscapeTemplatesByParentAndNameAndDirectionForm(Model uiModel) {
        uiModel.addAttribute("landscapes", Landscape.findAllLandscapes());
        uiModel.addAttribute("directionses", java.util.Arrays.asList(Directions.class.getEnumConstants()));
        return "fieldmappinglandscapetemplates/findFieldMappingLandscapeTemplatesByParentAndNameAndDirection";
    }
    
}
