package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "fieldmappingvaluemaps", formBackingObject = FieldMappingValueMap.class)
@RequestMapping("/fieldmappingvaluemaps")
@Controller
public class FieldMappingValueMapController {
}
