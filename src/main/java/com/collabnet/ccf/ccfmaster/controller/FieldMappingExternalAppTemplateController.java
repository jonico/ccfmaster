package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "fieldmappingexternalapptemplates", formBackingObject = FieldMappingExternalAppTemplate.class)
@RequestMapping("/fieldmappingexternalapptemplates")
@Controller
public class FieldMappingExternalAppTemplateController {
}
