// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingScope;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import java.lang.String;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

privileged aspect FieldMappingController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByNameAndParentAndScope", "form" }, method = RequestMethod.GET)
    public String FieldMappingController.findFieldMappingsByNameAndParentAndScopeForm(Model uiModel) {
        uiModel.addAttribute("repositorymappingdirections", RepositoryMappingDirection.findAllRepositoryMappingDirections());
        uiModel.addAttribute("fieldmappingscopes", java.util.Arrays.asList(FieldMappingScope.class.getEnumConstants()));
        return "fieldmappings/findFieldMappingsByNameAndParentAndScope";
    }
    
    @RequestMapping(params = { "find=ByParent", "form" }, method = RequestMethod.GET)
    public String FieldMappingController.findFieldMappingsByParentForm(Model uiModel) {
        uiModel.addAttribute("repositorymappingdirections", RepositoryMappingDirection.findAllRepositoryMappingDirections());
        return "fieldmappings/findFieldMappingsByParent";
    }
    
}
