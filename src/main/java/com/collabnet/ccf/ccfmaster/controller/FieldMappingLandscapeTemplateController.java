package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "fieldmappinglandscapetemplates", formBackingObject = FieldMappingLandscapeTemplate.class)
@RequestMapping("/fieldmappinglandscapetemplates")
@Controller
public class FieldMappingLandscapeTemplateController {
}
