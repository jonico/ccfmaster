package com.collabnet.ccf.ccfmaster.controller.api;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.server.core.update.CoreProperties;
import com.collabnet.ccf.ccfmaster.server.domain.Capabilities;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;

@Controller
@RequestMapping(value = Paths.CAPABILITIES)
public class ApiCapabilitiesController {
	
	@Value(value="#{CCFRunTimeProperties.ccfHome}")
	private String ccfhome;
	
	@Autowired(required=true)
	private Capabilities capabilities;

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Capabilities get() {
		String landscapeDir = ControllerHelper.landscapeDirName(ccfhome);
		capabilities.setCoreVersion(CoreProperties.ofDirectory(new File(landscapeDir)).getVersion());
		return capabilities;
	}
	
}
