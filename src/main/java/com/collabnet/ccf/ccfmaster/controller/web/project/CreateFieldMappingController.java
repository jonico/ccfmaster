package com.collabnet.ccf.ccfmaster.controller.web.project;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.controller.web.LandscapeFieldMappingController;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.model.FieldMappingTemplateModel;

@Controller
@RequestMapping(CreateFieldMappingController.PROJECT_FIELD_MAPPING_PATH)
public class CreateFieldMappingController extends AbstractProjectController {

	public static final String PROJECT_FIELD_MAPPING_NAME = "project/fieldmapping";
	public static final String PROJECT_FIELD_MAPPING_PATH = "/" + PROJECT_FIELD_MAPPING_NAME;
	
	private static final Logger log = LoggerFactory.getLogger(CreateFieldMappingController.class);
	private static final String DIRECTION_REQUEST_PARAM = "direction";
	private static final String RMD_ID_REQUEST_PARAM = "rmdid";
	private static final String FIELD_MAPPING_ID = "fieldmappingid";
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String list(
			@ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
			@RequestParam(value=DIRECTION_REQUEST_PARAM, defaultValue="FORWARD") Directions direction,
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
			@RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
			@RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
			Model model,HttpSession session) {
			LandscapeFieldMappingController.getFieldMappingForRMD(rmd, model);
			return PROJECT_FIELD_MAPPING_NAME + "/viewassociatefm";
	}

	
	/**
	 * Controller method to set active field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value="/setactivefm", method=RequestMethod.POST)
	public String setasActiveFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			@RequestParam(FIELD_MAPPING_ID) FieldMapping fieldMapping,
			Model model,HttpServletRequest request) {
		try{
			rmd.setActiveFieldMapping(fieldMapping);
			rmd.merge();
			FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_SUCCESS_MESSAGE);
		} catch (Exception exception) {
			log.debug("Error setting up active field mapping: " + exception.getMessage(), exception);
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute("rmdid", rmd.getId());
		return "redirect:" + PROJECT_FIELD_MAPPING_PATH;
	}
	
	/**
	 * Controller method to set delete the field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value="/delete", method=RequestMethod.POST)
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
			log.debug("Error deleting field mapping: " + exception.getMessage(), exception);
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_DELETE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute(RMD_ID_REQUEST_PARAM, rmd.getId());
		return "redirect:" + PROJECT_FIELD_MAPPING_PATH;

	}
	
	/**
	 * Controller method to create field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value="/createfm", method=RequestMethod.POST)
	public String createFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			Model model,HttpServletRequest request) {
		
		Landscape landscape=ControllerHelper.findLandscape();
		FieldMappingTemplateModel fieldMappingTemplateModel=new FieldMappingTemplateModel();
		//Get landscape templates by landscape and direction - connector templates
				List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndDirection(landscape, rmd.getDirection()).getResultList();
		//Get external app templates by external app and direction - project templates
				List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate = 
						FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParentAndDirection(rmd.getRepositoryMapping().getExternalApp(), rmd.getDirection()).getResultList();
				
		fieldMappingTemplateModel.setFieldMappingLandscapeTemplate(fieldMappingLandscapeTemplate);
		fieldMappingTemplateModel.setFieldMappingExternalAppTemplate(fieldMappingExternalAppTemplate);
		model.addAttribute("rmdid", rmd.getId());
		model.addAttribute("direction", rmd.getDirection().name());
		model.addAttribute("fieldMappingTemplateModel",fieldMappingTemplateModel);
		model.addAttribute("selectedLink", "repositorymappings");
		return  PROJECT_FIELD_MAPPING_NAME + "/displaycreatenewfm";
	}
	
	

	
}
