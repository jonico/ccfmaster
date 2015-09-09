package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

public class FieldMappingHelper {

    private static final String RMD_ID_REQUEST_PARAM = "rmdid";

    public static List<FieldMappingExternalAppTemplate> getfieldMappingExternalAppTemplateNames(
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd) {

        //Get external app templates by external app and direction - project templatessave
        List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplatesNames = FieldMappingExternalAppTemplate
                .findFieldMappingExternalAppTemplateNamesByParentAndDirection(
                        rmd.getRepositoryMapping().getExternalApp(),
                        rmd.getDirection()).getResultList();
        return fieldMappingExternalAppTemplatesNames;
    }

    public static List<FieldMappingLandscapeTemplate> getFieldMappingLandscapeTemplateNames(
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd) {

        Landscape landscape = ControllerHelper.findLandscape();
        //Get landscape templates by landscape and direction - connector templates
        List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplateNames = FieldMappingLandscapeTemplate
                .findFieldMappingLandscapeTemplateNamesByParentAndDirection(
                        landscape, rmd.getDirection()).getResultList();
        return fieldMappingLandscapeTemplateNames;
    }
}
