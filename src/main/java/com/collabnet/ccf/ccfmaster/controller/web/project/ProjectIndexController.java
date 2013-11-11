package com.collabnet.ccf.ccfmaster.controller.web.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(ProjectIndexController.PROJECT_INDEX_PATH)
public class ProjectIndexController extends AbstractProjectController {

    public static final String PROJECT_INDEX_PATH = "/project/";
    public static final String PROJECT_INDEX_NAME = "project/index";

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return PROJECT_INDEX_NAME;
    }
}
