package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "ccfcorestatuses", formBackingObject = CcfCoreStatus.class)
@RequestMapping("/ccfcorestatuses")
@Controller
public class CcfCoreStatusController {
}
