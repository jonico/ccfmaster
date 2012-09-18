package com.collabnet.ccf.ccfmaster.flow.web;

import java.io.Serializable;
import java.util.List;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.google.common.base.Preconditions;

public class RepositoryMappingDirectionHandler extends MultiAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RepositoryMappingDirectionModel rmdModel;
	
	
	public Landscape populateLandscape() {
		List<Landscape> landscapes = Landscape.findAllLandscapes();
		Preconditions.checkState(!landscapes.isEmpty(), "no landscape exists");
		return landscapes.get(0);
	}
	
	
	public Participant populateParticipant() {
		Landscape landscape = populateLandscape();
		return landscape.getParticipant();
	}
	 
	
	

	public Event saveRepositoryMappingDirection(RepositoryMappingDirectionModel rmdModel){	
		//System.out.println("In saveRepositoryMappingDirection "+rmdModel.getRepositoryMappingDirection());
		return success();
		
	}
	
	
	
	public void setrmdModel(RepositoryMappingDirectionModel rmdModel) {
		this.rmdModel = rmdModel;
	}

	public RepositoryMappingDirectionModel getrmdModel() {
		return rmdModel;
	}



	

	
	
	
}
