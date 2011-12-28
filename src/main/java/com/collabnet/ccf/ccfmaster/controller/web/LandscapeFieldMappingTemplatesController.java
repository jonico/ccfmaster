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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.XmlWebHelper;
import com.collabnet.ccf.ccfmaster.web.model.FileUpload;

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
		Landscape landscape=ControllerHelper.findLandscape(model);

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
		Landscape landscape = ControllerHelper.findLandscape(model);
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
		populatePageandSizeInModel(model, session);
		if (paramdirection.equals(ControllerConstants.FORWARD)) {
			return  "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESTFTOPART;
		} else {
			return  "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_DISPLAYFIELDMAPPINGTEMPLATESPARTTOTF;
		}
	}


	private void populatePageandSizeInModel(Model model, HttpSession session) {
		model.addAttribute("page", (session.getAttribute(ControllerConstants.PAGE_IN_SESSION) == null) ? ControllerConstants.DEFAULT_PAGE : session.getAttribute(ControllerConstants.PAGE_IN_SESSION));
		model.addAttribute("size", (session.getAttribute(ControllerConstants.SIZE_IN_SESSION) == null) ? ControllerConstants.DEFAULT_PAGE_SIZE : session.getAttribute(ControllerConstants.SIZE_IN_SESSION));
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
		Landscape landscape = ControllerHelper.findLandscape(model);
		FileUpload fileUpload = new FileUpload();
		model.addAttribute("fileUpload", fileUpload);
		model.addAttribute("participant", landscape.getParticipant());
		model.addAttribute("landscape", landscape);
		model.addAttribute("direction", paramdirection);
		model.addAttribute("selectedLink", "fieldmappingtemplates");
		populatePageandSizeInModel(model, session);
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
		//un-marshal JAXB object
		try {
			JAXBContext xmlContext = JAXBContext.newInstance(FieldMappingLandscapeTemplateList.class);
			Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
			fieldMappingLandscapeTemplateList = (FieldMappingLandscapeTemplateList) unmarshaller.unmarshal(inputStream);
		} catch (JAXBException exception) {
			log.debug("Error importing field mapping template: " + exception.getMessage(), exception);
			FlashMap.setErrorMessage(ControllerConstants.FMTIMPORTFAILUREMESSAGE,exception.getMessage());
			model.asMap().clear();
			populatePageandSizeInModel(model, session);
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES+"?direction="+paramdirection;
		}
		try {
			List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplatelist=fieldMappingLandscapeTemplateList.getFieldMappingTemplate();
			List<String> alreadyExists=new ArrayList<String>();
			for(FieldMappingLandscapeTemplate fieldMappingTemplate : fieldMappingLandscapeTemplatelist){
				if (isTemplateExists(model,fieldMappingTemplate.getName(), directions)) {
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
			populatePageandSizeInModel(model, session);
			session.setAttribute(FROMSESSION, fieldMappingLandscapeTemplateList);
			return  UIPathConstants.LANDSCAPESETTINGS_LISTFIELDMAPPINGTEMPLATES;

		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FMTIMPORTFAILUREMESSAGE,exception.getMessage());
			model.asMap().clear();
			populatePageandSizeInModel(model, session);
			return "redirect:/" + UIPathConstants.LANDSCAPESETTINGS_UPLOADFIELDMAPPINGTEMPLATES+"?direction="+paramdirection;
		}
	}



	/**
	 * @param fieldMappingRule
	 * @param rules
	 * @param fieldMappingLandscapeTemplatelist
	 * @return
	 */
	public static List<FieldMappingRule> createFieldMappingRule(List<FieldMappingRule> rules) {
		List<FieldMappingRule> newrules = new ArrayList<FieldMappingRule>();
		for (FieldMappingRule fmrules : rules) {
			FieldMappingRule fieldMappingRule=new FieldMappingRule();
			fieldMappingRule.setName(fmrules.getName());
			fieldMappingRule.setDescription(fmrules.getDescription());
			fieldMappingRule.setType(fmrules.getType());
			fieldMappingRule.setXmlContent(fmrules.getXmlContent());
			newrules.add(fieldMappingRule);		
		}

		return newrules;
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
		try {
			List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplateSession = getFMTFromSession(session);
			List<FieldMappingRule> rules=new ArrayList<FieldMappingRule>();
			Landscape parent = ControllerHelper.findLandscape(model);
			for(FieldMappingLandscapeTemplate fieldMappingTemplate:fieldMappingLandscapeTemplateSession){
				rules = fieldMappingTemplate.getRules();
				List<FieldMappingRule> newrules = createFieldMappingRule(rules);
				for (String fmtName:items) {
					if(fieldMappingTemplate.getName().equals(fmtName)){
						if (isTemplateExists(model,idNameMap.get(fmtName).toString(), directions)) { 
							FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatemerge=FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndNameAndDirection(parent, idNameMap.get(fmtName).toString(), directions).getSingleResult();
							mergeFieldMappingTemplate(parent,fieldMappingTemplate,fieldMappingLandscapeTemplatemerge,newrules,importStatus,idNameMap,model,directions);
						}
						else{
							persistFieldMappingTemplate(parent,idNameMap,fieldMappingTemplate,newrules,fmtName,importStatus,model,directions);
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
		populatePageandSizeInModel(model, session);
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
	private void persistFieldMappingTemplate(Landscape parent,Map<String,String> idNameMap, 
			FieldMappingLandscapeTemplate fieldMappingTemplate,
			List<FieldMappingRule> newrules, String fmtName,Map<String,String> importStatus,Model model,Directions directions) {
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatenew = new FieldMappingLandscapeTemplate();
		try{
			fieldMappingLandscapeTemplatenew.setDirection(fieldMappingTemplate.getDirection());
			fieldMappingLandscapeTemplatenew.setName(idNameMap.get(fmtName).toString());
			fieldMappingLandscapeTemplatenew.setParent(parent);
			fieldMappingLandscapeTemplatenew.getRules().clear();
			fieldMappingLandscapeTemplatenew.getRules().addAll(newrules);
			fieldMappingLandscapeTemplatenew.setKind(fieldMappingTemplate.getKind());
			fieldMappingLandscapeTemplatenew.persist();
			importStatus.put(idNameMap.get(fmtName).toString(), IMPORTED_SUCCESSFULLY);

		}
		catch (Exception exception){
			log.debug("Error saving field mapping template: " + exception.getMessage(), exception);
			importStatus.put(idNameMap.get(fmtName).toString(), IMPORTED_FAILED);
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
			FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatemerge,List<FieldMappingRule> newrules,Map<String,String> importStatus ,Map<String,String> idNameMap,Model model,Directions directions) {
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplatenew = new FieldMappingLandscapeTemplate();
		try{
			fieldMappingLandscapeTemplatenew.setDirection(fieldMappingTemplate.getDirection());
			fieldMappingLandscapeTemplatenew.setName(fieldMappingTemplate.getName());
			fieldMappingLandscapeTemplatenew.setId(fieldMappingLandscapeTemplatemerge.getId());
			fieldMappingLandscapeTemplatenew.setVersion(fieldMappingLandscapeTemplatemerge.getVersion());
			fieldMappingLandscapeTemplatenew.setParent(parent);
			fieldMappingLandscapeTemplatenew.getRules().clear();
			fieldMappingLandscapeTemplatenew.getRules().addAll(newrules);
			fieldMappingLandscapeTemplatenew.setKind(fieldMappingTemplate.getKind());
			fieldMappingLandscapeTemplatenew.merge();
			importStatus.put(fieldMappingTemplate.getName(), UPDATED_SUCCESSFULLY);

		}
		catch(Exception exception){
			log.debug("Error updating field mapping template: " + exception.getMessage(), exception);
			if (isTemplateExists(model,fieldMappingTemplate.getName().toString(), directions)) {
				importStatus.put(idNameMap.get(fieldMappingTemplate.getName()), UPDATED_FAILED + FIELD_MAPPING_TEMPLATE_NAME_ALREADY_EXISTS);
			}
			else{
				importStatus.put(idNameMap.get(fieldMappingTemplate.getName()), UPDATED_FAILED);
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



	private boolean isTemplateExists(Model model,String templateName, Directions directions) {
		Landscape landscape = ControllerHelper.findLandscape(model);
		boolean templateexists = false;
		if(FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndNameAndDirection(landscape, templateName, directions).getResultList().size()!=0){
			templateexists = true;
		}
		else{
			templateexists = false;
		}
		return templateexists;
	}

}
