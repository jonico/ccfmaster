package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "directionconfigs", formBackingObject = DirectionConfig.class)
@RequestMapping("/directionconfigs")
@Controller
public class DirectionConfigController {
}
