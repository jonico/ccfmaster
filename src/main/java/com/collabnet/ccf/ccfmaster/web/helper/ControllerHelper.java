package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class ControllerHelper {
	
	public static Landscape findLandscape(){
		List<Landscape> landscapeList=Landscape.findAllLandscapes();		
		return landscapeList.isEmpty()? null: landscapeList.get(0);
	}

	public static String landscapeDirName(String ccfHome){
		String dirName = ccfHome;
		if(findLandscape()!= null){
			dirName = String.format("%s/landscape%d",ccfHome,findLandscape().getId());
		}
		return dirName;
	}
	
}
