package com.collabnet.ccf.ccfmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;

@Controller
@RequestMapping(SessionTimeoutController.SESSION_TIMEOUT_PATH)
public class SessionTimeoutController {

    private static final String     SESSION_TIMEOUT_NAME   = "sessionTimeout";
    public static final String      SESSION_TIMEOUT_PATH   = "/"
                                                                   + SESSION_TIMEOUT_NAME;

    private static final String     TF_URL_MODEL_ATTRIBUTE = "tfUrl";

    @Autowired
    public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;

    @ModelAttribute(TF_URL_MODEL_ATTRIBUTE)
    public String populateTfUrl() {
        return ccfRuntimePropertyHolder.getTfUrl();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String sessionTimeout() {
        return SESSION_TIMEOUT_NAME;
    }
}
