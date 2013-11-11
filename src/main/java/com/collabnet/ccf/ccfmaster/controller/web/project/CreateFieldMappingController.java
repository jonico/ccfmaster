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

import com.collabnet.ccf.ccfmaster.controller.api.BadRequestException;
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

    public static final String  PROJECT_FIELD_MAPPING_NAME = "project/fieldmapping";
    public static final String  PROJECT_FIELD_MAPPING_PATH = "/"
                                                                   + PROJECT_FIELD_MAPPING_NAME;

    private static final Logger log                        = LoggerFactory
                                                                   .getLogger(CreateFieldMappingController.class);
    private static final String DIRECTION_REQUEST_PARAM    = "direction";
    private static final String RMD_ID_REQUEST_PARAM       = "rmdid";
    private static final String FIELD_MAPPING_ID           = "fieldmappingid";

    /**
     * Controller method to create field mapping for the RMD
     * 
     */
    @RequestMapping(value = "/createfm", method = RequestMethod.POST)
    public String createFieldMapping(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            Model model, HttpServletRequest request) {

        validateRepositoryMappingDirection(ea, rmd);
        Landscape landscape = ControllerHelper.findLandscape();
        FieldMappingTemplateModel fieldMappingTemplateModel = new FieldMappingTemplateModel();
        //Get landscape templates by landscape and direction - connector templates
        List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate = FieldMappingLandscapeTemplate
                .findFieldMappingLandscapeTemplatesByParentAndDirection(
                        landscape, rmd.getDirection()).getResultList();
        //Get external app templates by external app and direction - project templates
        List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate = FieldMappingExternalAppTemplate
                .findFieldMappingExternalAppTemplatesByParentAndDirection(
                        rmd.getRepositoryMapping().getExternalApp(),
                        rmd.getDirection()).getResultList();

        fieldMappingTemplateModel
                .setFieldMappingLandscapeTemplate(fieldMappingLandscapeTemplate);
        fieldMappingTemplateModel
                .setFieldMappingExternalAppTemplate(fieldMappingExternalAppTemplate);
        model.addAttribute("rmdid", rmd.getId());
        model.addAttribute("direction", rmd.getDirection().name());
        model.addAttribute("fieldMappingTemplateModel",
                fieldMappingTemplateModel);
        model.addAttribute("selectedLink", "repositorymappings");
        return PROJECT_FIELD_MAPPING_NAME + "/displaycreatenewfm";
    }

    /**
     * Controller method to set delete the field mapping for the RMD
     * 
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteFieldMapping(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            @RequestParam(FIELD_MAPPING_ID) String[] items, Model model,
            HttpServletRequest request) {
        if (items == null)
            items = new String[0];
        try {
            validateRepositoryMappingDirection(ea, rmd);
            for (String fieldMappingId : items) {
                if (rmd.getActiveFieldMapping().getId() == Long.valueOf(
                        fieldMappingId).longValue()) {
                    FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_NODELETE_ACTIVE_FAILURE_MESSAGE);
                } else {
                    FieldMapping fieldMappingEntry = FieldMapping
                            .findFieldMapping(new Long(fieldMappingId));
                    validateRepositoryMappingDirection(ea,
                            fieldMappingEntry.getParent());
                    fieldMappingEntry.remove();
                    FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_DELETE_SUCCESS_MESSAGE);
                }
            }

        } catch (Exception exception) {
            log.debug(
                    "Error deleting field mapping: " + exception.getMessage(),
                    exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.FIELD_MAPPING_DELETE_FAILURE_MESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        model.addAttribute(RMD_ID_REQUEST_PARAM, rmd.getId());
        return "redirect:" + PROJECT_FIELD_MAPPING_PATH;

    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(value = DIRECTION_REQUEST_PARAM, defaultValue = "FORWARD") Directions direction,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            @RequestParam(value = PAGE_REQUEST_PARAM, required = false) Integer page,
            @RequestParam(value = PAGE_SIZE_REQUEST_PARAM, required = false) Integer size,
            Model model, HttpSession session) {
        LandscapeFieldMappingController.getFieldMappingForRMD(rmd, model);
        Directions directions = ControllerConstants.FORWARD.equals(direction) ? Directions.FORWARD
                : Directions.REVERSE;
        populatePageSizetoModel(directions, ea, model, session);
        return PROJECT_FIELD_MAPPING_NAME + "/viewassociatefm";
    }

    /**
     * Controller method to save new field mapping for the existing RMD
     * 
     */
    @RequestMapping(value = "/savefm", method = RequestMethod.POST)
    public String saveNewFieldMapping(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            FieldMappingTemplateModel fieldMappingTemplateModel, Model model,
            HttpServletRequest request) {
        try {
            validateRepositoryMappingDirection(ea, rmd);
            LandscapeFieldMappingController.persistFieldMapping(
                    fieldMappingTemplateModel, rmd);

        } catch (Exception exception) {
            FlashMap.setErrorMessage(
                    ControllerConstants.FIELD_MAPPING_CREATE_FAILURE_MESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        model.addAttribute(RMD_ID_REQUEST_PARAM, rmd.getId());
        return "redirect:" + PROJECT_FIELD_MAPPING_PATH;

    }

    /**
     * Controller method to set active field mapping for the RMD
     * 
     */
    @RequestMapping(value = "/setactivefm", method = RequestMethod.POST)
    public String setasActiveFieldMapping(
            @ModelAttribute(EXTERNAL_APP_MODEL_ATTRIBUTE) ExternalApp ea,
            @RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
            @RequestParam(FIELD_MAPPING_ID) FieldMapping fieldMapping,
            Model model, HttpServletRequest request) {
        try {
            validateRepositoryMappingDirection(ea, rmd);
            rmd.setActiveFieldMapping(fieldMapping);
            rmd.merge();
            FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_SUCCESS_MESSAGE);
        } catch (Exception exception) {
            log.debug(
                    "Error setting up active field mapping: "
                            + exception.getMessage(), exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_FAILURE_MESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        model.addAttribute("rmdid", rmd.getId());
        return "redirect:" + PROJECT_FIELD_MAPPING_PATH;
    }

    void validateRepositoryMappingDirection(ExternalApp ea,
            RepositoryMappingDirection rmd) {
        if (!ea.getId().equals(
                rmd.getRepositoryMapping().getExternalApp().getId()))
            throw new BadRequestException("wrong external app.");
    }

    public static void populatePageSizetoModel(Directions direction,
            ExternalApp ea, Model model, HttpSession session) {
        Integer size = (Integer) session
                .getAttribute(ControllerConstants.SIZE_IN_SESSION) == null ? ControllerConstants.PAGINATION_SIZE
                : (Integer) session
                        .getAttribute(ControllerConstants.SIZE_IN_SESSION);
        float nrOfPages = (float) RepositoryMappingDirection
                .countRepositoryMappingDirectionsByExternalAppAndDirection(ea,
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

}
