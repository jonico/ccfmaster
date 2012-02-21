package com.collabnet.ccf.ccfmaster.controller.web;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplateList;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.XmlWebHelper;
import com.collabnet.ccf.ccfmaster.web.model.FileUpload;
import com.collabnet.ccf.core.utils.FieldMappingUtil;
import com.collabnet.ccf.core.utils.SerializationUtil;

@RequestMapping("/admin/**")
@Controller
public class LandscapeFieldMappingTemplatesController extends AbstractLandscapeController {


	private static final Logger log = LoggerFactory.getLogger(LandscapeFieldMappingTemplatesController.class);

	private static final String TEMPLATE_NAME_ALREADY_EXISTS_OVERRIDDEN = "<font color='red'> * Template Name Already Exists(will be overridden on import)</font>";
	private static final String FROMSESSION = "fromsession";
	private static final String FIELD_MAPPING_TEMPLATE_NAME_ALREADY_EXISTS = "  (Field Mapping Template Name Already Exists)";
	private static final String IMPORTED_SUCCESSFULLY = "Imported Successfully";
	private static final String IMPORTED_FAILED = "Import Failed";
	private static final String UPDATED_SUCCESSFULLY = "Updated Successfully";
	private static final String UPDATED_FAILED = "Update Failed";
	private static final String FMTID = "fmtid";
	private static final String FMTNAME = "fmtname";
	private static final String NULL_VALUE = "-1";

	/**
	 * Controller method to display field mapping templates for TF to
	 * Participant
	 * 
	 */
	@RequestMapping(value = "/"+ UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESTFTOPART, method = RequestMethod.GET)
	public String displayFieldMappingTftoPart(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model model,HttpServletRequest request, HttpSession session) {
		doList(Directions.FORWARD, page, size, model,session);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESTFTOPART;
	}
	/**
	 * @param direction
	 * @param page
	 * @param size
	 * @param model
	 */
	private void doList(Directions directions, Integer page, Integer size, Model model,HttpSession session) {
		Landscape landscape=ControllerHelper.findLandscape();

		List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate = paginate(
				FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndDirection(landscape, directions),
				FieldMappingLandscapeTemplate.countFieldMappingLandscapeTemplatesByDirection(directions),
				page, size, model)
				.getResultList();
		model.addAttribute("fieldMappingLandscapeTemplate",	fieldMappingLandscapeTemplate);
		session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
		session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
		populateFieldMappingTemplatesModel(model);
	}
	/**
	 * Controller method to display field mapping templates for Participant to TF
	 * 
	 * 
	 */
	@RequestMapping(value = "/"+ UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESPARTTOTF, method = RequestMethod.GET)
	public String displayFieldMappingParttoTf(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model model,HttpServletRequest request, HttpSession session) {
		doList(Directions.REVERSE, page, size, model,session);
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESPARTTOTF;
	}


	/**
	 * Helper method to populate Field Mapping Templates Model
	 * 
	 */
	public void  populateFieldMappingTemplatesModel(Model model) {
		Landscape landscape = ControllerHelper.findLandscape();
		model.addAttribute("participant", landscape.getParticipant());
		model.addAttribute("landscape", landscape);
		model.addAttribute("selectedLink", "fieldmappingtemplates");
	}



	/**
	 * Controller method to delete field mapping templates
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_DELETEFIELDMAPPINGTEMPLATES, method = RequestMethod.POST)
	public String deleteFieldMappingTemplate(@RequestParam(ControllerConstants.DIRECTION) String paramdirection,
			@RequestParam(FMTID) String[] items,
			@ModelAttribute("fieldMappingLandscapeTemplate") FieldMappingLandscapeTemplate fieldMappingLandscapeTemplateModel,
			Model model, HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size,HttpSession session) {
		String[] newfmtId=null;
		try {
			for (String fmtId:items) {
				newfmtId = Pattern.compile("-").split(fmtId);
				fieldMappingLandscapeTemplateModel = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplate(new Long(newfmtId[1]));
				fieldMappingLandscapeTemplateModel.remove();
			}
			FlashMap.setSuccessMessage(ControllerConstants.FMTDELETESUCCESSMESSAGE);

		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FMTDELETEFAILUREMESSAGE,exception.getMessage());
		}
		model.asMap().clear();
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		populatePageSizetoModel(directions,model, session);
		if (paramdirection.equals(ControllerConstants.FORWARD)) {
			return  "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESTFTOPART;
		} else {
			return  "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESPARTTOTF;
		}
	}

	@InitBinder
	public void myInitBinder(WebDataBinder binder) {
		// do not bind these fields
		binder.setDisallowedFields(new String[] {"fieldMappingLandscapeTemplate", "direction" });
	}

	/**
	 * Controller method to export field mapping templates
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_EXPORTFIELDMAPPINGTEMPLATES, method = RequestMethod.POST)
	public void exportFieldMappingTemplate(
			@ModelAttribute("fieldMappingLandscapeTemplate") FieldMappingLandscapeTemplate fieldMappingLandscapeTemplateModel,
			@RequestParam(FMTID) String[] items,
			HttpServletResponse response) {
		String defaultFileName = null;
		String[] newfmtId=null;
		FieldMappingLandscapeTemplateList fieldMappingLandscapeTemplateList=new FieldMappingLandscapeTemplateList();
		try {
			for (String fmtId:items) {
				newfmtId = Pattern.compile("-").split(fmtId);
				fieldMappingLandscapeTemplateModel =FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplate(new Long(newfmtId[1]));
				fieldMappingLandscapeTemplateList.add(fieldMappingLandscapeTemplateModel);
			}
			if(fieldMappingLandscapeTemplateList.size()==1){
				defaultFileName = fieldMappingLandscapeTemplateModel.getName()+ ".xml";
			}
			else{
				defaultFileName = "field_mapping_templates"+ ".xml";
			}
			XmlWebHelper.createJAXBExport(response, defaultFileName, fieldMappingLandscapeTemplateList, FieldMappingLandscapeTemplateList.class);
		} catch (Exception exception) {
			log.debug("Error exporting field mapping template: " + exception.getMessage(), exception);
			FlashMap.setErrorMessage(ControllerConstants.FMTEXPORTFAILUREMESSAGE, exception.getMessage());
		}
	}

	/**
	 * Controller method to upload field mapping xml rule files
	 * 
	 */
	@RequestMapping(value = "/"+ UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES)
	public String uploadFieldMappingTemplate(@RequestParam(ControllerConstants.DIRECTION) String paramdirection,Model model, HttpSession session) {
		Landscape landscape = ControllerHelper.findLandscape();
		FileUpload fileUpload = new FileUpload();
		model.addAttribute("fileUpload", fileUpload);
		model.addAttribute("participant", landscape.getParticipant());
		model.addAttribute("landscape", landscape);
		model.addAttribute("direction", paramdirection);
		model.addAttribute("selectedLink", "fieldmappingtemplates");
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		populatePageSizetoModel(directions,model, session);
		return UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES;
	}

	/**
	 * Controller method to import field mapping templates
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_IMPORTFIELDMAPPINGTEMPLATES, method = RequestMethod.POST)
	public String importFieldMappingTemplate(@RequestParam(ControllerConstants.DIRECTION) String paramdirection,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("fileUpload") FileUpload fileUpload,
			Model model,HttpServletRequest request, HttpSession session) {
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		CommonsMultipartFile commonsmultipartFile = fileUpload.getFile();
		byte[] xmlContent = commonsmultipartFile.getFileItem().get();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent);
		FieldMappingLandscapeTemplateList fieldMappingLandscapeTemplateList = null;
		try {
			//un-marshal JAXB object
			fieldMappingLandscapeTemplateList = SerializationUtil.deSerialize(inputStream, FieldMappingLandscapeTemplateList.class);
		} catch (JAXBException exception) {
			log.debug("Error importing field mapping template: " + exception.getMessage(), exception);
			FlashMap.setErrorMessage(ControllerConstants.FMTIMPORTFAILUREMESSAGE,exception.getMessage());
			model.asMap().clear();
			populatePageSizetoModel(directions,model, session);
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES+"?direction="+paramdirection;
		}
		try {
			List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplatelist=fieldMappingLandscapeTemplateList.getFieldMappingTemplate();
			List<String> alreadyExists=new ArrayList<String>();
			for(FieldMappingLandscapeTemplate fieldMappingTemplate : fieldMappingLandscapeTemplatelist){
				if (isTemplateExists(fieldMappingTemplate.getName(), directions)) {
					fieldMappingTemplate.setName(fieldMappingTemplate.getName());
					alreadyExists.add(TEMPLATE_NAME_ALREADY_EXISTS_OVERRIDDEN);
				}
				else{
					fieldMappingTemplate.setName(fieldMappingTemplate.getName());
					alreadyExists.add("");
				}
			}
			model.addAttribute("alreadyExists", alreadyExists);
			model.addAttribute("fieldMappingLandscapeTemplatelist", fieldMappingLandscapeTemplatelist);
			model.addAttribute("direction", paramdirection);
			populateFieldMappingTemplatesModel(model);
			populatePageSizetoModel(directions,model, session);
			session.setAttribute(FROMSESSION, fieldMappingLandscapeTemplateList);
			return  UIPathConstants.LANDSCAPESETTINGS_LISTFIELDMAPPINGTEMPLATES;

		} catch (Exception exception) {
			log.debug("Import failed due to following exception "+exception.getMessage());
			FlashMap.setErrorMessage(ControllerConstants.FMTIMPORTFAILUREMESSAGE,exception.getMessage());
			model.asMap().clear();
			populatePageSizetoModel(directions,model, session);
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES+"?direction="+paramdirection;
		}
	}

	
	
	
	/**
	 * Controller method to import bulk field mapping templates
	 * 
	 */
	@RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_BULKIMPORTFIELDMAPPINGTEMPLATES, method = RequestMethod.POST)
	public String bulkImportFieldMappingTemplate(@RequestParam(ControllerConstants.DIRECTION) String paramdirection,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model model,HttpServletRequest request,HttpSession session) {
		String[] items = request.getParameterValues(FMTNAME);
		Directions directions = ControllerConstants.FORWARD.equals(paramdirection) ? Directions.FORWARD : Directions.REVERSE;
		Map<String,String> idNameMap = mapIdandName(request, items);
		Map<String,String> importStatus=new HashMap<String,String>();
		String nameFromMap=null;
		try {
			List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplateSession = getFMTFromSession(session);
			Landscape parent = ControllerHelper.findLandscape();
			for(FieldMappingLandscapeTemplate fieldMappingTemplate:fieldMappingLandscapeTemplateSession){
				List<FieldMappingRule> newrules = FieldMappingUtil.createFieldMappingRule(fieldMappingTemplate.getRules());
				List<FieldMappingValueMap> newValuemaps=FieldMappingUtil.createFieldMappingValueMap(fieldMappingTemplate.getValueMaps());
				for (String fmtName:items) {
					nameFromMap=idNameMap.get(fmtName);
					if(fieldMappingTemplate.getName().equals(fmtName)){
						if (isTemplateExists(nameFromMap, directions)) { 
							FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatemerge=FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndNameAndDirection(parent, nameFromMap, directions).getSingleResult();
							mergeFieldMappingTemplate(parent,fieldMappingTemplate,fieldMappingLandscapeTemplatemerge,newrules,newValuemaps,importStatus,nameFromMap,model,directions);
						}
						else{
							persistFieldMappingTemplate(parent,nameFromMap,fieldMappingTemplate,newrules,newValuemaps,importStatus);
						}
					}
				}
			}

		} catch (Exception exception) {
			log.debug("Error importing field mapping template: " + exception.getMessage(), exception.getMessage());
			FlashMap.setErrorMessage(ControllerConstants.FMTIMPORTFAILUREMESSAGE,exception.getMessage());
		}
		model.addAttribute("status",importStatus);
		model.addAttribute("directions",paramdirection);
		populatePageSizetoModel(directions,model, session);
		populateFieldMappingTemplatesModel(model);
		return UIPathConstants.LANDSCAPESETTINGS_FIELDMAPPINGTEMPLATESIMPORTSTATUS;
	}

	/**
	 * @param idNameMap
	 * @param parent
	 * @param fieldMappingTemplate
	 * @param fieldMappingLandscapeTemplatenew
	 * @param newrules
	 * @param fmtName
	 */
	private void persistFieldMappingTemplate(Landscape parent,String nameFromMap, 
			FieldMappingLandscapeTemplate fieldMappingTemplate,
			List<FieldMappingRule> newrules,List<FieldMappingValueMap> newValueMap,Map<String,String> importStatus) {
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatenew = new FieldMappingLandscapeTemplate();
		try{
			fieldMappingLandscapeTemplatenew.setDirection(fieldMappingTemplate.getDirection());
			fieldMappingLandscapeTemplatenew.setName(nameFromMap);
			fieldMappingLandscapeTemplatenew.setParent(parent);
			fieldMappingLandscapeTemplatenew.getRules().clear();
			fieldMappingLandscapeTemplatenew.getRules().addAll(newrules);
			fieldMappingLandscapeTemplatenew.setKind(fieldMappingTemplate.getKind());
			fieldMappingLandscapeTemplatenew.getValueMaps().clear();
			fieldMappingLandscapeTemplatenew.setValueMaps(newValueMap);
			fieldMappingLandscapeTemplatenew.persist();
			importStatus.put(nameFromMap, IMPORTED_SUCCESSFULLY);

		}
		catch (Exception exception){
			log.debug("Error saving field mapping template: " + exception.getMessage(), exception);
			importStatus.put(nameFromMap, IMPORTED_FAILED);
		}
	}

	/**
	 * @param parent
	 * @param fieldMappingTemplate
	 * @param fieldMappingLandscapeTemplatenew
	 * @param newrules
	 * @param fieldMappingLandscapeTemplatemerge
	 */
	private void mergeFieldMappingTemplate(Landscape parent,FieldMappingLandscapeTemplate fieldMappingTemplate,
			FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatemerge,List<FieldMappingRule> newrules,List<FieldMappingValueMap> newValueMap,Map<String,String> importStatus ,String nameFromMap,Model model,Directions directions) {
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatenew = new FieldMappingLandscapeTemplate();
		try{
			fieldMappingLandscapeTemplatenew.setDirection(fieldMappingTemplate.getDirection());
			fieldMappingLandscapeTemplatenew.setName(nameFromMap);
			fieldMappingLandscapeTemplatenew.setId(fieldMappingLandscapeTemplatemerge.getId());
			fieldMappingLandscapeTemplatenew.setVersion(fieldMappingLandscapeTemplatemerge.getVersion());
			fieldMappingLandscapeTemplatenew.setParent(parent);
			fieldMappingLandscapeTemplatenew.getRules().clear();
			fieldMappingLandscapeTemplatenew.getRules().addAll(newrules);
			fieldMappingLandscapeTemplatenew.getValueMaps().clear();
			fieldMappingLandscapeTemplatenew.setValueMaps(newValueMap);
			fieldMappingLandscapeTemplatenew.setKind(fieldMappingTemplate.getKind());
			fieldMappingLandscapeTemplatenew.merge();
			importStatus.put(fieldMappingTemplate.getName(), UPDATED_SUCCESSFULLY);

		}
		catch(Exception exception){
			log.debug("Error updating field mapping template: " + exception.getMessage(), exception);
			if (isTemplateExists(fieldMappingTemplate.getName().toString(), directions)) {
				importStatus.put(nameFromMap, UPDATED_FAILED + FIELD_MAPPING_TEMPLATE_NAME_ALREADY_EXISTS);
			}
			else{
				importStatus.put(nameFromMap, UPDATED_FAILED);
			}
		}
	}

	/**
	 * @param session
	 * @return
	 */
	private List<FieldMappingLandscapeTemplate> getFMTFromSession(HttpSession session) {
		FieldMappingLandscapeTemplateList fieldMappingLandscapeTemplateSessionList =(FieldMappingLandscapeTemplateList)session.getAttribute(FROMSESSION);
		List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplateSession=fieldMappingLandscapeTemplateSessionList.getFieldMappingTemplate();
		return fieldMappingLandscapeTemplateSession;
	}

	/**
	 * @param request
	 * @param items
	 * @return
	 */
	public static Map<String,String> mapIdandName(HttpServletRequest request, String[] items) {
		Map<String,String> nameMap=new HashMap<String,String>();
		String value=null;
		for(String fmtname:items){
			if(request.getParameter("name-"+fmtname).equals("")||request.getParameter("name-"+fmtname).equals(null)){
				value=NULL_VALUE;
			}else
			{
				value=request.getParameter("name-"+fmtname);
			}
			nameMap.put(fmtname,value);
		}
		return nameMap;
	}



	private boolean isTemplateExists(String templateName, Directions directions) {
		Landscape landscape = ControllerHelper.findLandscape();
		boolean templateexists = false;
		if(FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndNameAndDirection(landscape, templateName, directions).getResultList().size()!=0){
			templateexists = true;
		}
		else{
			templateexists = false;
		}
		return templateexists;
	}

	public static void populatePageSizetoModel(Directions directions, Model model,
			HttpSession session) {
		Integer size = (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE: (Integer) session.getAttribute(ControllerConstants.SIZE_IN_SESSION);
		float nrOfPages = (float)FieldMappingLandscapeTemplate.countFieldMappingLandscapeTemplatesByDirection(directions) / size.intValue();
		Integer page = (Integer) session.getAttribute(ControllerConstants.PAGE_IN_SESSION);
		// if page in session is null.get the default value of page
		if (page == null) {
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (page <= 0) {
			// in case if current page value is less than or equal to zero get
			// default value of page (on deleting the last record of the first
			// page)
			page = Integer.valueOf(ControllerConstants.DEFAULT_PAGE);
		} else if (Math.ceil(nrOfPages) != 0.0 && page >= Math.ceil(nrOfPages)) {
			// in case if current page value is greater than no of page (on
			// deleting last record from the current page.traverse to the
			// previous page)
			page = (int) Math.ceil(nrOfPages);
		}
		model.addAttribute("page", page);
		model.addAttribute("size", size);
	}
	
	
}
