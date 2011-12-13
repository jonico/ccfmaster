package com.collabnet.ccf.ccfmaster.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;

@RequestMapping("/linkid/{linkId}")
@Controller
public class LinkIdController extends AbstractLinkIdController{
	
	private static final Logger log = LoggerFactory.getLogger(LinkIdController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(@ModelAttribute(EXTERNAL_APP_MODELATTRIBUTE_NAME) ExternalApp ea) {
		// just show the default index page. We might want to provide a custom one later.
		return "index";
	}
}
