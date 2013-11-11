package com.collabnet.ccf.ccfmaster.controller.web.project;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.controller.web.LandscapeFieldMappingTemplatesController;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.model.FileUpload;
import com.collabnet.ccf.core.utils.FieldMappingUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

@Controller
@RequestMapping(ProjectFieldMappingController.PROJECT_FIELD_MAPPING_TEMPLATES_PATH)
public class ProjectFieldMappingController extends AbstractProjectController {

    public static final String  PROJECT_FIELD_MAPPING_TEMPLATES_NAME                    = "project/fieldmappingtemplates";
    public static final String  PROJECT_FIELD_MAPPING_TEMPLATES_PATH                    = "/"
                                                                                                + PROJECT_FIELD_MAPPING_TEMPLATES_NAME;

    private static final Logger log                                                     = LoggerFactory
                                                                                                .getLogger(ProjectFieldMappingController.class);

    private static final String FMT_ID_REQUEST_PARAM                                    = "fmtid";
    private static final String FMTNAME                                                 = "fmtname";
    private static final String FROMSESSION                                             = "fmeatfromsession";
    private static final String DIRECTION_REQUEST_PARAM                                 = "direction";
    private static final String IMPORTED_SUCCESSFULLY                                   = "Imported Successfully";
    private static final String IMPORTED_FAILED                                         = "Import Failed";
    private static final String UPDATED_SUCCESSFULLY                                    = "Updated Successfully";
    private static final String UPDATED_FAILED                                          = "Update Failed";
    private static final String FIELD_MAPPING_EXTERNAL_APP_TEMPLATE_NAME_ALREADY_EXISTS = "  (Field Mapping External App Template Name Already Exists)";
    private static final String TEMPLATE_NAME_ALREADY_EXISTS_OVERRIDDEN                 = "<font color='red'> * Template Name Already Exists(will be overridden on import)</font>";

    /**
     * Controller method to import bulk field mapping external app templates
     * 
     */
    @RequestMapping("/bulkimport")
    public String bulkImportFieldMappingTemplate(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions directions,
            Model model, HttpServletRequest request, HttpSession session) {
        String[] items = request.getParameterValues(FMTNAME);
        Map<String, String> idNameMap = LandscapeFieldMappingTemplatesController
                .mapIdandName(request, items);
        Map<String, String> importStatus = new HashMap<String, String>();
        String nameFromMap = null;
        try {
            List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplateSession = getFMEATFromSession(session);

            for (FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate : fieldMappingExternalAppTemplateSession) {
                List<FieldMappingRule> newrules = FieldMappingUtil
                        .createFieldMappingRule(fieldMappingExternalAppTemplate
                                .getRules());
                List<FieldMappingValueMap> newValuemaps = FieldMappingUtil
                        .createFieldMappingValueMap(fieldMappingExternalAppTemplate
                                .getValueMaps());
                for (String fmtName : items) {
                    nameFromMap = idNameMap.get(fmtName);
                    if (fieldMappingExternalAppTemplate.getName().equals(
                            fmtName)) {
                        if (isTemplateExists(model, nameFromMap, directions, ea)) {
                            FieldMappingExternalAppTemplate fieldMappingExternalAppTemplatemerge = FieldMappingExternalAppTemplate
                                    .findFieldMappingExternalAppTemplatesByParentAndNameAndDirection(
                                            ea, nameFromMap, directions)
                                    .getSingleResult();
                            mergeFieldMappingTemplate(ea,
                                    fieldMappingExternalAppTemplate,
                                    fieldMappingExternalAppTemplatemerge,
                                    newrules, newValuemaps, importStatus,
                                    nameFromMap, model, directions);
                        } else {
                            persistFieldMappingExternalAppTemplate(ea,
                                    nameFromMap,
                                    fieldMappingExternalAppTemplate, newrules,
                                    newValuemaps, importStatus);
                        }
                    }
                }
            }

        } catch (Exception exception) {
            log.debug(
                    "Error importing field mapping template: "
                            + exception.getMessage(), exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.FMTIMPORTFAILUREMESSAGE,
                    exception.getMessage());
        }
        model.addAttribute("status", importStatus);
        model.addAttribute("directions", directions.name());
        populatePageSizetoModel(directions, ea, model, session);
        populateFieldMappingExternalAppTemplatesModel(model, directions, ea);
        return PROJECT_FIELD_MAPPING_TEMPLATES_NAME + "/status";

    }

    /**
     * Controller method to delete field mapping external app templates
     * 
     */
    @RequestMapping(value = "/delete")
    public String deleteFieldMappingExternalAppTemplate(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(FMT_ID_REQUEST_PARAM) FieldMappingExternalAppTemplate[] entries,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions directions,
            Model model, HttpSession session) {
        try {
            Iterable<FieldMappingExternalAppTemplate> validEntries = Iterables
                    .filter(Arrays.asList(entries),
                            isValidFieldMappingExternalAppTemplate(directions,
                                    ea));
            for (FieldMappingExternalAppTemplate entry : validEntries) {
                entry.remove();
            }
            FlashMap.setSuccessMessage(ControllerConstants.FMTDELETESUCCESSMESSAGE);
        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.FMTDELETEFAILUREMESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        populatePageSizetoModel(directions, ea, model, session);
        return "redirect:" + PROJECT_FIELD_MAPPING_TEMPLATES_PATH
                + "?direction=" + directions;
    }

    /**
     * Controller method to export field mapping external app templates
     * 
     */
    @ResponseBody
    @RequestMapping(value = "/export")
    public FieldMappingExternalAppTemplateList exportFieldMappingTemplate(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(FMT_ID_REQUEST_PARAM) FieldMappingExternalAppTemplate[] entries,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            HttpServletResponse response) {
        String defaultFileName = null;
        if (entries.length == 1) {
            defaultFileName = entries[0].getName() + ".xml";
        } else {
            defaultFileName = "field_mapping_templates" + ".xml";
        }
        Iterable<FieldMappingExternalAppTemplate> validEntries = Iterables
                .filter(Arrays.asList(entries),
                        isValidFieldMappingExternalAppTemplate(direction, ea));
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + defaultFileName + "\"");
        return new FieldMappingExternalAppTemplateList(
                ImmutableList.copyOf(validEntries));
    }

    /**
     * Controller method to import field mapping external app templates
     * 
     */
    @RequestMapping("/import")
    public String importFieldMappingTemplate(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @ModelAttribute("fileUpload") FileUpload fileUpload,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions directions,
            Model model, HttpServletRequest request, HttpSession session) {
        CommonsMultipartFile commonsmultipartFile = fileUpload.getFile();
        byte[] xmlContent = commonsmultipartFile.getFileItem().get();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent);
        FieldMappingExternalAppTemplateList fieldMappingExternalAppTemplateList = null;
        //un-marshal JAXB object
        try {
            JAXBContext xmlContext = JAXBContext
                    .newInstance(FieldMappingExternalAppTemplateList.class);
            Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
            fieldMappingExternalAppTemplateList = (FieldMappingExternalAppTemplateList) unmarshaller
                    .unmarshal(inputStream);
        } catch (JAXBException exception) {
            log.debug("Error unmarshalling field mapping template: "
                    + exception.getMessage(), exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.FMTIMPORTFAILUREMESSAGE,
                    exception.getMessage());
            model.asMap().clear();
            populatePageSizetoModel(directions, ea, model, session);
            return "redirect:" + PROJECT_FIELD_MAPPING_TEMPLATES_PATH
                    + "/upload?direction=" + directions.name();
        }
        try {
            List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplateHolder = fieldMappingExternalAppTemplateList
                    .getFieldMappingTemplate();
            List<String> alreadyExists = new ArrayList<String>();
            for (FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate : fieldMappingExternalAppTemplateHolder) {
                if (isTemplateExists(model,
                        fieldMappingExternalAppTemplate.getName(), directions,
                        ea)) {
                    fieldMappingExternalAppTemplate
                            .setName(fieldMappingExternalAppTemplate.getName());
                    alreadyExists.add(TEMPLATE_NAME_ALREADY_EXISTS_OVERRIDDEN);
                } else {
                    fieldMappingExternalAppTemplate
                            .setName(fieldMappingExternalAppTemplate.getName());
                    alreadyExists.add("");
                }
            }
            model.addAttribute("alreadyExists", alreadyExists);
            model.addAttribute("fieldMappingLandscapeTemplatelist",
                    fieldMappingExternalAppTemplateHolder);
            model.addAttribute("direction", directions.name());
            populateFieldMappingExternalAppTemplatesModel(model, directions, ea);
            session.setAttribute(FROMSESSION,
                    fieldMappingExternalAppTemplateList);
            populatePageSizetoModel(directions, ea, model, session);
            return PROJECT_FIELD_MAPPING_TEMPLATES_NAME + "/bulkimport";

        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.FMTIMPORTFAILUREMESSAGE,
                    exception.getMessage());
            model.asMap().clear();
            populatePageSizetoModel(directions, ea, model, session);
            return "redirect:" + PROJECT_FIELD_MAPPING_TEMPLATES_PATH
                    + "/upload?direction=" + directions.name();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        final List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplates = paginate(
                FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParentAndDirection(
                        ea, direction),
                FieldMappingExternalAppTemplate
                        .countFieldMappingExternalAppTemplatesByParentAndDirection(
                                ea, direction), page, size, model)
                .getResultList();
        session.setAttribute(ControllerConstants.SIZE_IN_SESSION, size);
        session.setAttribute(ControllerConstants.PAGE_IN_SESSION, page);
        model.addAttribute("fieldMappingLandscapeTemplate",
                fieldMappingExternalAppTemplates);
        return PROJECT_FIELD_MAPPING_TEMPLATES_NAME + "/" + direction;
    }

    /**
     * Helper method to populate Field Mapping Templates Model
     * 
     */
    public void populateFieldMappingExternalAppTemplatesModel(Model model,
            Directions directions, ExternalApp eapp) {
        Landscape landscape = ControllerHelper.findLandscape();
        List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate = FieldMappingExternalAppTemplate
                .findFieldMappingExternalAppTemplatesByParentAndDirection(eapp,
                        directions).getResultList();
        model.addAttribute("fieldMappingLandscapeTemplate",
                fieldMappingExternalAppTemplate);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", "fieldmappingtemplates");
    }

    /**
     * Controller method to upload field mapping xml rule files
     * 
     */
    @RequestMapping("/upload")
    public String uploadFieldMappingTemplate(
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions directions,
            Model model, HttpSession session) {
        Landscape landscape = ControllerHelper.findLandscape();
        FileUpload fileUpload = new FileUpload();
        model.addAttribute("fileUpload", fileUpload);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("direction", directions.name());
        model.addAttribute("selectedLink", "fieldmappingtemplates");
        return PROJECT_FIELD_MAPPING_TEMPLATES_NAME + "/upload";
    }

    /**
     * @param session
     * @return
     */
    private List<FieldMappingExternalAppTemplate> getFMEATFromSession(
            HttpSession session) {
        FieldMappingExternalAppTemplateList fieldMappingExternalAppTemplateSessionList = (FieldMappingExternalAppTemplateList) session
                .getAttribute(FROMSESSION);
        List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplateSession = fieldMappingExternalAppTemplateSessionList
                .getFieldMappingTemplate();
        return fieldMappingExternalAppTemplateSession;
    }

    private boolean isTemplateExists(Model model, String templateName,
            Directions directions, ExternalApp eapp) {
        boolean templateexists = false;
        if (FieldMappingExternalAppTemplate
                .findFieldMappingExternalAppTemplatesByParentAndNameAndDirection(
                        eapp, templateName, directions).getResultList().size() != 0) {
            templateexists = true;
        } else {
            templateexists = false;
        }
        return templateexists;
    }

    /**
     * @param eapp
     * @param fieldMappingExternalAppTemplate
     * @param fieldMappingExternalAppTemplatemerge
     * @param newrules
     * @param importStatus
     * @param idNameMap
     * @param model
     * @param directions
     */
    private void mergeFieldMappingTemplate(
            ExternalApp eapp,
            FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate,
            FieldMappingExternalAppTemplate fieldMappingExternalAppTemplatemerge,
            List<FieldMappingRule> newrules,
            List<FieldMappingValueMap> newValueMap,
            Map<String, String> importStatus, String nameFromMap, Model model,
            Directions directions) {
        FieldMappingExternalAppTemplate fieldMappingExternalAppTemplatenew = new FieldMappingExternalAppTemplate();
        try {
            fieldMappingExternalAppTemplatenew
                    .setDirection(fieldMappingExternalAppTemplate
                            .getDirection());
            fieldMappingExternalAppTemplatenew
                    .setName(fieldMappingExternalAppTemplate.getName());
            fieldMappingExternalAppTemplatenew
                    .setId(fieldMappingExternalAppTemplatemerge.getId());
            fieldMappingExternalAppTemplatenew
                    .setVersion(fieldMappingExternalAppTemplatemerge
                            .getVersion());
            fieldMappingExternalAppTemplatenew.setParent(eapp);
            fieldMappingExternalAppTemplatenew.getRules().clear();
            fieldMappingExternalAppTemplatenew.getRules().addAll(newrules);
            fieldMappingExternalAppTemplatenew
                    .setKind(fieldMappingExternalAppTemplate.getKind());
            fieldMappingExternalAppTemplatenew.getValueMaps().clear();
            fieldMappingExternalAppTemplatenew.getValueMaps().addAll(
                    newValueMap);
            fieldMappingExternalAppTemplatenew.merge();
            importStatus.put(fieldMappingExternalAppTemplate.getName(),
                    UPDATED_SUCCESSFULLY);

        } catch (Exception exception) {
            log.debug("Error updating field mapping external app template: "
                    + exception.getMessage(), exception);
            if (isTemplateExists(model, fieldMappingExternalAppTemplate
                    .getName().toString(), directions, eapp)) {
                importStatus
                        .put(nameFromMap,
                                UPDATED_FAILED
                                        + FIELD_MAPPING_EXTERNAL_APP_TEMPLATE_NAME_ALREADY_EXISTS);
            } else {
                importStatus.put(nameFromMap, UPDATED_FAILED);
            }
        }
    }

    /**
     * @param eapp
     * @param idNameMap
     * @param fieldMappingExternalAppTemplate
     * @param newrules
     * @param fmtName
     * @param importStatus
     * @param model
     * @param directions
     */
    private void persistFieldMappingExternalAppTemplate(ExternalApp eapp,
            String nameFromMap,
            FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate,
            List<FieldMappingRule> newrules,
            List<FieldMappingValueMap> newValueMap,
            Map<String, String> importStatus) {
        FieldMappingExternalAppTemplate fieldMappingExternalAppTemplatenew = new FieldMappingExternalAppTemplate();
        try {
            fieldMappingExternalAppTemplatenew
                    .setDirection(fieldMappingExternalAppTemplate
                            .getDirection());
            fieldMappingExternalAppTemplatenew.setName(nameFromMap);
            fieldMappingExternalAppTemplatenew.setParent(eapp);
            fieldMappingExternalAppTemplatenew.getRules().clear();
            fieldMappingExternalAppTemplatenew.getRules().addAll(newrules);
            fieldMappingExternalAppTemplatenew
                    .setKind(fieldMappingExternalAppTemplate.getKind());
            fieldMappingExternalAppTemplatenew.getValueMaps().clear();
            fieldMappingExternalAppTemplatenew.getValueMaps().addAll(
                    newValueMap);
            fieldMappingExternalAppTemplatenew.persist();
            importStatus.put(nameFromMap, IMPORTED_SUCCESSFULLY);

        } catch (Exception exception) {
            log.debug("Error saving field mapping external app template: "
                    + exception.getMessage(), exception);
            importStatus.put(nameFromMap, IMPORTED_FAILED);
        }
    }

    public static void populatePageSizetoModel(Directions direction,
            ExternalApp ea, Model model, HttpSession session) {
        Integer size = (Integer) session
                .getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE
                : (Integer) session
                        .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        float nrOfPages = (float) FieldMappingExternalAppTemplate
                .countFieldMappingExternalAppTemplatesByParentAndDirection(ea,
                        direction)
                / size.intValue();
        Integer page = (Integer) session
                .getAttribute(ControllerConstants.PAGE_IN_SESSION);
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

    static Predicate<FieldMappingExternalAppTemplate> isValidFieldMappingExternalAppTemplate(
            final Directions direction, final ExternalApp ea) {
        return new Predicate<FieldMappingExternalAppTemplate>() {

            @Override
            public boolean apply(FieldMappingExternalAppTemplate input) {
                if (input == null)
                    return false;
                final ExternalApp externalApp = input.getParent();
                return input.getDirection() == direction
                        && ea.getId().equals(externalApp.getId());
            }

        };
    }

}
