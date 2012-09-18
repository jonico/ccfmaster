package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;



@RequestMapping("/admin/**")
@Controller
public class LandscapeFieldMappingController extends AbstractLandscapeController {

	
	private static final String FIELD_MAPPING_ID = "fieldmappingid";
	private static final String RMD_ID_REQUEST_PARAM = "rmdid";
	
	
	/**
	 * Controller method to display associated field mappings for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD, method = RequestMethod.GET)
	public String listForRepositoryMappingDirection(
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
			Model model) {
		getFieldMappingForRMD(rmd, model);
		return UIPathConstants.VIEW_ASSOCIATED_FIELD_MAPPINGS;

	}

	
	private List<FieldMapping> getFieldMappingForRMD(
			RepositoryMappingDirection rmd, Model model) {
		List<FieldMapping> fieldMapping=FieldMapping.findFieldMappingsByParent(rmd).getResultList();
		if( "FORWARD".equalsIgnoreCase(rmd.getDirection().name()))
		{
			model.addAttribute("rmdname", rmd.getRepositoryMapping().getTeamForgeRepositoryId()+"=>"+rmd.getRepositoryMapping().getParticipantRepositoryId());
		}
		else{
			model.addAttribute("rmdname", rmd.getRepositoryMapping().getParticipantRepositoryId()+"=>"+rmd.getRepositoryMapping().getTeamForgeRepositoryId());
		}
		model.addAttribute("rmdid", rmd.getId());
		model.addAttribute("direction", rmd.getDirection().name());
		model.addAttribute("activeFieldMappingid", rmd.getActiveFieldMapping().getId());
		populateFieldMappingModel(fieldMapping,model);
		return fieldMapping;
	}

	/**
	 * Controller method to set active field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.SETAS_ACTIVE_FIELD_MAPPING, method = RequestMethod.POST)
	public String setasActiveFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			@RequestParam(FIELD_MAPPING_ID) FieldMapping fieldMapping,
			Model model,HttpServletRequest request) {
		try{
			rmd.setActiveFieldMapping(fieldMapping);
			rmd.merge();
			FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_SUCCESS_MESSAGE);
		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute("rmdid", rmd.getId());
		return "redirect:/" + UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD;

	}
	
	/**
	 * Controller method to set delete the field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.DELETE_ASSOCIATED_FIELD_MAPPINGS, method = RequestMethod.POST)
	public String deleteFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			@RequestParam(FIELD_MAPPING_ID) String[] items,
			Model model,HttpServletRequest request) {
		if (items == null) 
			items = new String[0];
		try {
			for (String fieldMappingId: items ) {
				if(rmd.getActiveFieldMapping().getId() == Long.valueOf(fieldMappingId).longValue()){
					FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_NODELETE_ACTIVE_FAILURE_MESSAGE);
				}
				else{
					FieldMapping fieldMappingEntry=FieldMapping.findFieldMapping(new Long(fieldMappingId));
					fieldMappingEntry.remove();
					FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_DELETE_SUCCESS_MESSAGE);
				}
			}
			
		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_DELETE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute(RMD_ID_REQUEST_PARAM, rmd.getId());
		return "redirect:/" + UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD;

	}
	
	void populateFieldMappingModel(List<FieldMapping> fieldMappingEntrys,  Model model) {
		Landscape landscape=ControllerHelper.findLandscape();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		model.addAttribute("associatedfmmodel", fieldMappingEntrys);
	}

	
	
	
}
