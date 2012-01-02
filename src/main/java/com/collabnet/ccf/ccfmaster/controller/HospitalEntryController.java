package com.collabnet.ccf.ccfmaster.controller;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;

@RooWebScaffold(path = "hospitalentrys", formBackingObject = HospitalEntry.class)
@RequestMapping("/hospitalentrys")
@Controller
public class HospitalEntryController {
}
