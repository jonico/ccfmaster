package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "fieldmappingvaluemapentrys", formBackingObject = FieldMappingValueMapEntry.class)
@RequestMapping("/fieldmappingvaluemapentrys")
@Controller
public class FieldMappingValueMapEntryController {
}
