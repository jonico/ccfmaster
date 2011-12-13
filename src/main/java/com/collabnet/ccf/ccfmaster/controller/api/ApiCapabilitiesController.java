package com.collabnet.ccf.ccfmaster.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.domain.Capabilities;

@Controller
@RequestMapping(value = Paths.CAPABILITIES)
public class ApiCapabilitiesController {
	
	private final Capabilities capabilities = new Capabilities();

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Capabilities get() {
		return capabilities;
	}
	
}
