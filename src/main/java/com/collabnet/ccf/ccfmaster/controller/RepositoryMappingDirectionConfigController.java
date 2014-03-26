package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "repositorymappingdirectionconfigs", formBackingObject = RepositoryMappingDirectionConfig.class)
@RequestMapping("/repositorymappingdirectionconfigs")
@Controller
public class RepositoryMappingDirectionConfigController {
}
