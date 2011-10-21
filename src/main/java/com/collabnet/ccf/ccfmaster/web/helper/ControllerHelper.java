package com.collabnet.ccf.ccfmaster.web.helper;

import java.util.List;

import org.springframework.ui.Model;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public class ControllerHelper {
	
	public static Landscape findLandscape(Model model){
		List landscapeList=Landscape.findAllLandscapes();		
		Landscape landscape=(Landscape)landscapeList.get(0);
		return landscape;
	}

	public static Directions parseDirections(String paramdirection){
		Directions directions=null;
		if(paramdirection.equals(ControllerConstants.FORWARD)){
		 directions=Directions.FORWARD;
		}
		else{
		 directions=Directions.REVERSE;
		}
		return directions;
}
	
}
