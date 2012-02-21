package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class ControllerHelper {
	
	public static Landscape findLandscape(){
		List<Landscape> landscapeList=Landscape.findAllLandscapes();		
		return landscapeList.get(0);
	}

	
}
