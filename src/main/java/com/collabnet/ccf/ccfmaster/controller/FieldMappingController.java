package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "fieldmappings", formBackingObject = FieldMapping.class)
@RequestMapping("/fieldmappings")
@Controller
public class FieldMappingController {
}
