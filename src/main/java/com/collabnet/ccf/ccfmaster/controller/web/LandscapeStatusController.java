package com.collabnet.ccf.ccfmaster.controller.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.core.utils.CCFMasterBackUp;

@RequestMapping("/admin/**")
@Controller
public class LandscapeStatusController extends AbstractLandscapeController {

    private static final Logger log             = LoggerFactory
                                                        .getLogger(LandscapeStatusController.class);
    private static final String PARAM_DIRECTION = "param_direction";

    @Autowired
    private CCFMasterBackUp     ccfMasterBackup;

    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_DISPLAY_CCF_BACKUP)
    public String displayCCFBackup(Model model, HttpServletRequest request) {
        Landscape landscape = ControllerHelper.findLandscape();
        model.addAttribute("selectedLink", ControllerConstants.STATUS);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAY_CCF_BACKUP;
    }

    /**
     * Controller method to display participant to TF ccfcorestatus
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS)
    public String displayStatusParticipanttoTF(Model model,
            HttpServletRequest request) {
        populateCCFCoreStatusModel(model, Directions.REVERSE);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
    }

    /**
     * Controller method to display TF to participant ccfcorestatus
     * 
     */
    @RequestMapping(value = "/"
            + UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS)
    public String displayStatusTFtoParticipant(Model model,
            HttpServletRequest request) {
        populateCCFCoreStatusModel(model, Directions.FORWARD);
        return UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;

    }

    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_CCF_BACKUP)
    public String doCCFBackup(Model model, HttpServletRequest request) {
        try {
            ccfMasterBackup.doFullBackUp();
            FlashMap.setSuccessMessage(
                    ControllerConstants.CCF_BACKUP_SUCCESS_MESSAGE,
                    ccfMasterBackup.getBackupFilePath());
        } catch (CoreConfigurationException e) {
            log.debug(
                    "Error occured while in Backup operation: "
                            + e.getMessage(), e);
            FlashMap.setErrorMessage(
                    ControllerConstants.CCF_BACKUP_ERROR_MESSAGE,
                    e.getMessage());
        }
        Landscape landscape = ControllerHelper.findLandscape();
        model.asMap().clear();
        model.addAttribute("selectedLink", ControllerConstants.STATUS);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        return String.format("redirect:%s",
                UIPathConstants.LANDSCAPESETTINGS_DISPLAY_CCF_BACKUP);
    }

    /**
     * Help method to merge/save the ccfcore status
     * 
     */
    @Transactional
    public void mergeCCFCoreStatus(Model model,
            CcfCoreStatus ccfCoreStatusModel, HttpServletRequest request,
            Directions directions, ExecutedCommand eCommand)
            throws CoreConfigurationException {
        Landscape landscape = ControllerHelper.findLandscape();
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        ccfCoreStatusModel.setExecutedCommand(eCommand);
        ccfCoreStatusModel.setDirection(direction);
        ccfCoreStatusModel.setCurrentStatus(ccfCoreStatusModel
                .getCurrentStatus());
        ccfCoreStatusModel.setId(Long.valueOf(request
                .getParameter(ControllerConstants.CCF_CORE_STAUS_ID)));
        ccfCoreStatusModel.setVersion(Integer.parseInt(request
                .getParameter(ControllerConstants.CCF_CORE_STATUS_VERSION)));
        ccfCoreStatusModel.merge();
        populateCCFCoreStatusModel(model, directions);
    }

    /**
     * Helper method to populate direction config
     * 
     */
    public void populateCCFCoreStatusModel(Model model, Directions directions) {
        Landscape landscape = ControllerHelper.findLandscape();
        Direction direction = Direction
                .findDirectionsByLandscapeEqualsAndDirectionEquals(landscape,
                        directions).getSingleResult();
        CcfCoreStatus ccfCoreStatusModel = CcfCoreStatus
                .findCcfCoreStatusesByDirection(direction).getSingleResult();
        model.addAttribute("ccfCoreStatusModel", ccfCoreStatusModel);
        model.addAttribute("selectedLink", ControllerConstants.STATUS);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("landscape", landscape);
        model.addAttribute("ccfCoreStatusId", ccfCoreStatusModel.getId());
        model.addAttribute("ccfCoreStatusVersion",
                ccfCoreStatusModel.getVersion());
    }

    /**
     * Controller method to start ccfcore status
     * 
     */
    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STARTCCFCORESTATUS, method = RequestMethod.POST)
    public String startCCFCoreStatus(
            @RequestParam(PARAM_DIRECTION) String paramdirection,
            @ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel,
            Model model, HttpServletRequest request) {
        try {
            Directions directions = ControllerConstants.FORWARD
                    .equals(paramdirection) ? Directions.FORWARD
                    : Directions.REVERSE;
            mergeCCFCoreStatus(model, ccfCoreStatusModel, request, directions,
                    ExecutedCommand.START);
        } catch (CoreConfigurationException coreConfigurationException) {
            log.debug(
                    "Error starting core: "
                            + coreConfigurationException.getMessage(),
                    coreConfigurationException);
            FlashMap.setErrorMessage(
                    ControllerConstants.CCF_CORE_STATUS_MESSAGE,
                    coreConfigurationException.getMessage());
        }
        model.asMap().clear();
        if (paramdirection.equals(ControllerConstants.FORWARD)) {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;
        } else {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
        }
    }

    /**
     * Controller method to stop ccfcore status
     * 
     */
    @RequestMapping(value = UIPathConstants.LANDSCAPESETTINGS_STOPCCFCORESTATUS, method = RequestMethod.POST)
    public String stopCCFCoreStatus(
            @RequestParam(PARAM_DIRECTION) String paramdirection,
            @ModelAttribute("ccfCoreStatusModel") CcfCoreStatus ccfCoreStatusModel,
            Model model, HttpServletRequest request) {
        try {
            Directions directions = ControllerConstants.FORWARD
                    .equals(paramdirection) ? Directions.FORWARD
                    : Directions.REVERSE;
            mergeCCFCoreStatus(model, ccfCoreStatusModel, request, directions,
                    ExecutedCommand.STOP);
        } catch (CoreConfigurationException coreConfigurationException) {
            log.debug(
                    "Error stopping core: "
                            + coreConfigurationException.getMessage(),
                    coreConfigurationException);
            FlashMap.setErrorMessage(
                    ControllerConstants.CCF_CORE_STATUS_MESSAGE,
                    coreConfigurationException.getMessage());
        }
        model.asMap().clear();
        if (paramdirection.equals(ControllerConstants.FORWARD)) {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYTFTOPARTICIPANTSTATUS;
        } else {
            return "redirect:/"
                    + UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTTOTFSTATUS;
        }
    }

    /**
     * @param model
     */
    public static void restartCCFCoreStatus(Model model) {
        try {
            List<CcfCoreStatus> coreStatus = CcfCoreStatus
                    .findAllCcfCoreStatuses();
            for (CcfCoreStatus ccfCoreStatus : coreStatus) {
                if (ccfCoreStatus.getCurrentStatus().equals(CoreState.STARTED)) {
                    ccfCoreStatus.setExecutedCommand(ExecutedCommand.RESTART);
                    ccfCoreStatus.merge();
                }
            }
        } catch (CoreConfigurationException coreConfigurationException) {
            log.debug(
                    "Error restarting core: "
                            + coreConfigurationException.getMessage(),
                    coreConfigurationException);
            FlashMap.setErrorMessage(
                    ControllerConstants.CCF_CORE_RESTART_STATUS_MESSAGE,
                    coreConfigurationException.getMessage());
        }
    }

}
