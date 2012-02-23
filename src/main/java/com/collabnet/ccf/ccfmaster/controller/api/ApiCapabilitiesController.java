package com.collabnet.ccf.ccfmaster.controller.api;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.config.Version;
import com.collabnet.ccf.ccfmaster.server.core.update.CoreProperties;
import com.collabnet.ccf.ccfmaster.server.domain.Capabilities;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;

@Controller
@RequestMapping(value = Paths.CAPABILITIES)
public class ApiCapabilitiesController {
		
	private CCFRuntimePropertyHolder runtimeProperties;
		
	private Capabilities capabilities;

	@Autowired
	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
	}

	@Autowired
	public void setCCFRunTimeProperties(CCFRuntimePropertyHolder runtimeProperties) {
		this.runtimeProperties = runtimeProperties;
	}

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Capabilities get() {
		String ccfhome = runtimeProperties.getCcfHome();
		String landscapeDir = ControllerHelper.landscapeDirName(ccfhome);
		Version coreVersion = CoreProperties.ofDirectory(new File(landscapeDir)).getVersion();
		Capabilities newCapabilities = new Capabilities(capabilities.getVersion());
		newCapabilities.setCoreVersion(coreVersion);
		return newCapabilities;
	}
	
}
