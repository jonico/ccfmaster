package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "participantconfigs", formBackingObject = ParticipantConfig.class)
@RequestMapping("/participantconfigs")
@Controller
public class ParticipantConfigController {
}
