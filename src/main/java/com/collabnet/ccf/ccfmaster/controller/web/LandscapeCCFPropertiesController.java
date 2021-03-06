package com.collabnet.ccf.ccfmaster.controller.web;

import static com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper.isQCMaxAttachmentExist;
import static com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper.isSWPMaxAttachmentExist;
import static com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper.isTFMaxAttachmentExist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.config.CoreConfigLoader;
import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyType;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.validator.ConfigValidator;

@RequestMapping("/admin/**")
@Controller
public class LandscapeCCFPropertiesController extends AbstractLandscapeController {

    @Autowired
    public CCFRuntimePropertyHolder runtimeProperties;

    @Autowired
    private CoreConfigLoader        coreConfigLoader;

    private static final Logger     log             = LoggerFactory
                                                            .getLogger(LandscapeCCFPropertiesController.class);

    private static final String     PARAM_DIRECTION = "param_direction";

    private static final String     RESTART         = "restart";

    /**
     * Controller method to display log template from participant to TF
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF, method = RequestMethod.GET)
    public String displayCCFPropertiesLogTemplateParttoTF(Model model,
            HttpServletRequest request) {
        populateDirectionConfigModel(model, Directions.REVERSE);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
    }

    /**
     * Controller method to display log template from TF to participant
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART, method = RequestMethod.GET)
    public String displayCCFPropertiesLogTemplateTFtoPart(Model model,
            HttpServletRequest request) {
        populateDirectionConfigModel(model, Directions.FORWARD);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART;

    }

    /**
     * Controller method to display synchronize behavior from participant to TF
     * 
     * @throws IOException
     * @throws JAXBException
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF, method = RequestMethod.GET)
    public String displayCCFPropertiesSyncParttoTF(Model model,
            HttpServletRequest request) throws JAXBException, IOException {
        Landscape landscape = ControllerHelper.findLandscape();
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        Directions.REVERSE).getSingleResult();
        CCFCorePropertyList ccfCoreProperties = populateCCFCoreProperties(direction);
        populateModel(landscape, model);
        model.addAttribute("directionconfiglist", ccfCoreProperties);
        model.addAttribute("defaultdirectionconfiglist",
                buildDefaultCoreProperty(direction)); //to display default core config values in jsp
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
    }

    /**
     * Controller method to display synchronize behavior from TF to participant
     * 
     * @throws IOException
     * @throws JAXBException
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART, method = RequestMethod.GET)
    public String displayCCFPropertiesSyncTFtoPart(Model model,
            HttpServletRequest request) throws JAXBException, IOException {
        Landscape landscape = ControllerHelper.findLandscape();
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        Directions.FORWARD).getSingleResult();
        CCFCorePropertyList ccfCoreProperties = populateCCFCoreProperties(direction);
        populateModel(landscape, model);
        model.addAttribute("directionconfiglist", ccfCoreProperties);
        model.addAttribute("defaultdirectionconfiglist",
                buildDefaultCoreProperty(direction)); //to display default core config values in jsp
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowNestedPaths(true);
    }

    /**
     * Helper method to populate direction
     * 
     */
    public void populateModel(Landscape landscape, Model model) {
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", ControllerConstants.CCF_PROPERTIES);

    }

    /**
     * Controller method to restore default direction config settings
     * 
     * @throws IOException
     * @throws JAXBException
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_RESTORE_DEFAULT_SETTINGS, method = RequestMethod.POST)
    public String restoreDefaultSettings(Model model,
            HttpServletRequest request,
            @RequestParam(PARAM_DIRECTION) String paramdirection)
            throws JAXBException, IOException {
        RequestContext ctx = new RequestContext(request);
        Landscape landscape = ControllerHelper.findLandscape();
        Directions directions = ControllerConstants.FORWARD
                .equals(paramdirection) ? Directions.FORWARD
                : Directions.REVERSE;
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        CCFCorePropertyList ccfCoreProperties = new CCFCorePropertyList();
        List<CCFCoreProperty> defaultProperty = buildDefaultCoreProperty(direction);
        ccfCoreProperties.setCcfCoreProperties(defaultProperty);
        ccfCoreProperties.setDirection(direction);
        populateModel(landscape, model);
        model.addAttribute("directionconfiglist", ccfCoreProperties);
        model.addAttribute("defaultdirectionconfiglist", defaultProperty);//to display default core config values in jsp
        model.addAttribute("connectionmessage",
                ctx.getMessage(ControllerConstants.RESTORE_SUCCESS_MESSSAGE));
        if (paramdirection.equals(ControllerConstants.FORWARD)) {
            return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
        } else {
            return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
        }
    }

    /**
     * Controller method to update log template
     * 
     */
    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESLOGTEMPLATE, method = RequestMethod.POST)
    public String saveCCFPropertiesLogTemplate(
            @RequestParam(ControllerConstants.DIRECTION_CONFIG_ID) String direction_Config_Id,
            @RequestParam(ControllerConstants.DIRECTION_CONFIG_VERSION) String direction_Config_Version,
            @RequestParam(PARAM_DIRECTION) String paramdirection,
            @ModelAttribute("directionConfig") @Valid DirectionConfig directionConfig,
            BindingResult bindingResult, Model model, HttpServletRequest request) {
        Landscape landscape = ControllerHelper.findLandscape();
        Directions directions = ControllerConstants.FORWARD
                .equals(paramdirection) ? Directions.FORWARD
                : Directions.REVERSE;
        try {
            updateCCFPropertiesLogTemplate(direction_Config_Id,
                    direction_Config_Version, directionConfig, landscape,
                    directions);
            boolean hasRestart = Boolean.parseBoolean(request
                    .getParameter(RESTART));
            if (hasRestart) {
                LandscapeStatusController.restartCCFCoreStatus(model);
                FlashMap.setSuccessMessage(ControllerConstants.LOG_TEMPLATE_RESTART_SUCCESS_MESSAGE);
            }
        } catch (Exception exception) {
            log.debug("Error saving log template: " + exception.getMessage(),
                    exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.LOG_TEMPLATE_SAVE_FAIL_MESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        if (paramdirection.equals(ControllerConstants.FORWARD)) {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATETFTOPART;
        } else {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESLOGTEMPLATEPARTTOTF;
        }

    }

    /**
     * Controller method to save synchronize behavior
     * 
     * @throws IOException
     * @throws JAXBException
     * 
     */
    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_SAVECCFPROPERTIESSYNC, method = RequestMethod.POST)
    public String saveCCFPropertiesSync(
            @RequestParam(PARAM_DIRECTION) String paramdirection,
            @ModelAttribute("directionconfiglist") CCFCorePropertyList ccfCoreProperties,
            BindingResult bindingResult, Model model, HttpServletRequest request)
            throws JAXBException, IOException {
        Landscape landscape = ControllerHelper.findLandscape();
        Directions directions = ControllerConstants.FORWARD
                .equals(paramdirection) ? Directions.FORWARD
                : Directions.REVERSE;
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        ConfigValidator configValidator = new ConfigValidator();
        configValidator.validate(ccfCoreProperties, bindingResult);
        if (bindingResult.hasErrors()) {
            populateModel(landscape, model);
            model.addAttribute("directionconfiglist", ccfCoreProperties);
            model.addAttribute("defaultdirectionconfiglist",
                    buildDefaultCoreProperty(direction)); //to display default core config values in jsp
            RequestContext ctx = new RequestContext(request);
            model.addAttribute("connectionerror", ctx
                    .getMessage(ControllerConstants.VALIDATION_ERROR_MESSSAGE));
            if (paramdirection.equals(ControllerConstants.FORWARD)) {
                return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
            } else {
                return UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
            }

        }
        try {
            updateDirectionConfigs(ccfCoreProperties, direction);
            updateDirection(ccfCoreProperties, landscape, direction);
            boolean hasRestart = Boolean.parseBoolean(request
                    .getParameter(RESTART));
            if (hasRestart) {
                LandscapeStatusController.restartCCFCoreStatus(model);
                FlashMap.setSuccessMessage(ControllerConstants.SYNC_SAVE_RESTART_SUCCESS_MESSAGE);
            } else {
                FlashMap.setSuccessMessage(ControllerConstants.SYNC_SAVE_SUCCESS_MESSAGE);
            }
        } catch (Exception exception) {
            log.debug(
                    "Error saving Connector properties: "
                            + exception.getMessage(), exception);
            FlashMap.setErrorMessage(
                    ControllerConstants.SYNC_SAVE_FAIL_MESSAGE,
                    exception.getMessage());
        }
        model.asMap().clear();
        if (paramdirection.equals(ControllerConstants.FORWARD)) {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCTFTOPART;
        } else {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYCCFPROPERTIESSYNCPARTTOTF;
        }
    }

    private List<CCFCoreProperty> buildDefaultCoreProperty(Direction direction)
            throws JAXBException, IOException {
        List<CCFCoreProperty> defaultProperty = coreConfigLoader
                .getDefaultCCFCorePropertyList(direction);
        if (defaultProperty.isEmpty()) {
            defaultProperty.add(getMaxAttachmentProperty(direction, true));
        }
        return defaultProperty;
    }

    private CCFCoreProperty createMaxAttachmentProperty(String keyName,
            SystemKind systemKind, Direction direction, boolean isValueDefault) {
        DirectionConfig attachmentConfig = DirectionConfig
                .findDirectionConfigsByDirectionAndName(direction, keyName)
                .getSingleResult();
        CCFCoreProperty property = new CCFCoreProperty();
        String value = attachmentConfig.getVal();
        if (isValueDefault) {
            value = ccfRuntimePropertyHolder.getMaxAttachmentSize();
        }
        property.setName(attachmentConfig.getName());
        property.setCategory(ControllerConstants.MAX_ATTACHMENT_CATEGORY);
        property.setType(CCFCorePropertyType.INTEGER);
        property.setSystemKind(systemKind);
        property.setValue(value);
        property.setLabelName(ControllerConstants.MAX_ATTACHMENT_LABEL_NAME);
        property.setToolTip(ControllerConstants.MAX_ATTACHMENT_TOOLTIP);
        property.setDirection(direction.getDirection());
        return property;
    }

    private CCFCoreProperty getMaxAttachmentProperty(Direction direction,
            boolean isValueDefault) {
        CCFCoreProperty property = null;
        boolean isForwardDir = direction.getDirection().equals(
                Directions.FORWARD);
        SystemKind systemkind = direction.getLandscape().getParticipant()
                .getSystemKind();
        if (isForwardDir && isTFMaxAttachmentExist(direction)) {
            property = createMaxAttachmentProperty(
                    ControllerConstants.CCF_DIRECTION_TF_MAX_ATTACHMENTSIZE,
                    systemkind, direction, isValueDefault);
        } else if (!isForwardDir && systemkind.equals(SystemKind.QC)
                && isQCMaxAttachmentExist(direction)) {
            property = createMaxAttachmentProperty(
                    ControllerConstants.CCF_DIRECTION_QC_MAX_ATTACHMENTSIZE,
                    systemkind, direction, isValueDefault);
        } else if (!isForwardDir && systemkind.equals(SystemKind.SWP)
                && isSWPMaxAttachmentExist(direction)) {
            property = createMaxAttachmentProperty(
                    ControllerConstants.CCF_DIRECTION_SWP_MAX_ATTACHMENTSIZE,
                    systemkind, direction, isValueDefault);
        } else if (!isForwardDir && systemkind.equals(SystemKind.GENERIC)) {
            if (genericParticipant != null) {
                String configName = String.format(
                        "ccf.direction.%s.max.attachmentsize",
                        genericParticipant.getPrefix().toLowerCase());
                property = createMaxAttachmentProperty(configName, systemkind,
                        direction, isValueDefault);
            }
        }
        return property;
    }

    private CCFCorePropertyList populateCCFCoreProperties(Direction direction) {
        CCFCorePropertyList ccfCoreProperties = new CCFCorePropertyList();
        try {
            List<CCFCoreProperty> ccfcoreProperty = populateUpdatedCoreConfigValues(direction);
            if (ccfcoreProperty.isEmpty()) {
                ccfcoreProperty.add(getMaxAttachmentProperty(direction, false));
            }
            ccfCoreProperties.setCcfCoreProperties(ccfcoreProperty);

        } catch (JAXBException e) {
            throw new CoreConfigurationException(
                    "Could not parse the config xml file: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new CoreConfigurationException(
                    "Could not read the config xml file: " + e.getMessage(), e);
        }
        ccfCoreProperties.setDirection(direction);
        return ccfCoreProperties;
    }

    private List<CCFCoreProperty> populateUpdatedCoreConfigValues(
            Direction direction) throws JAXBException, IOException {
        List<CCFCoreProperty> newdefaultProperties = new ArrayList<CCFCoreProperty>();
        List<CCFCoreProperty> defaultProperties = coreConfigLoader
                .getDefaultCCFCorePropertyList(direction);
        for (CCFCoreProperty config : defaultProperties) {
            List<DirectionConfig> configList = DirectionConfig
                    .findDirectionConfigsByDirectionAndName(direction,
                            config.getName()).getResultList();
            if (!configList.isEmpty()) {
                DirectionConfig currentConfig = configList.get(0);
                if (!config.getValue().equals(currentConfig.getVal())) {
                    config.setValue(currentConfig.getVal());
                }
            }
            newdefaultProperties.add(config);
        }
        return newdefaultProperties;
    }

    /**
     * @param direction_Config_Id
     * @param direction_Config_Version
     * @param directionConfig
     * @param landscape
     * @param directions
     */
    private void updateCCFPropertiesLogTemplate(String direction_Config_Id,
            String direction_Config_Version, DirectionConfig directionConfig,
            Landscape landscape, Directions directions) {
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        directionConfig.setId(Long.valueOf(direction_Config_Id));
        directionConfig.setVersion(Integer.parseInt(direction_Config_Version));
        directionConfig.setDirection(direction);
        directionConfig.merge();
        FlashMap.setSuccessMessage(ControllerConstants.LOG_TEMPLATE_SAVE_SUCCESS_MESSAGE);
    }

    private void updateDirection(CCFCorePropertyList ccfCoreProperties,
            Landscape landscape, Direction direction) {
        direction.setShouldStartAutomatically(ccfCoreProperties.getDirection()
                .getShouldStartAutomatically());
        direction.setLandscape(landscape);
        direction.merge();
    }

    private void updateDirectionConfigs(CCFCorePropertyList props,
            Direction direction) {
        List<DirectionConfig> configList = CoreConfigLoader
                .getAsDirectionConfigList(props, direction);
        for (DirectionConfig dc : configList) {
            List<DirectionConfig> resultList = DirectionConfig
                    .findDirectionConfigsByDirectionAndName(direction,
                            dc.getName()).getResultList();
            if (!resultList.isEmpty()) { // if the directionconfig property is found,update the value of the property
                DirectionConfig result = resultList.get(0);
                if (!dc.getVal().equals(result.getVal())) {
                    result.setVal(dc.getVal());
                    result.merge();
                }
                continue;
            }
            dc.persist();
        }
    }

    /**
     * Helper method to populate direction config
     * 
     */
    public static void populateDirectionConfigModel(Model model,
            Directions directions) {
        Landscape landscape = ControllerHelper.findLandscape();
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        DirectionConfig directionConfig = DirectionConfig
                .findDirectionConfigsByDirectionAndName(direction,
                        ControllerConstants.CCF_DIRECTION_LOGMESSAGETEMPLATE)
                .getSingleResult();

        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("directionConfigversion",
                directionConfig.getVersion());
        model.addAttribute("directionConfigid", directionConfig.getId());
        model.addAttribute("direction", directionConfig.getDirection());
        model.addAttribute("directionConfig", directionConfig);
        model.addAttribute("landscape", landscape);
        model.addAttribute("selectedLink", ControllerConstants.CCF_PROPERTIES);
    }

}
