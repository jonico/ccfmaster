package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import org.springframework.ui.Model;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class ControllerHelper {
	
	public static Landscape findLandscape(Model model){
		List landscapeList=Landscape.findAllLandscapes();		
		Landscape landscape=(Landscape)landscapeList.get(0);
		return landscape;
	}

}
