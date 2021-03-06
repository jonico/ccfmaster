package com.collabnet.ccf.ccfmaster.web.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.ui.Model;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.model.TFSettingsModel;

public class TFSettingsHelper {

    /**
     * @param tfSettingsModel
     * @param model
     * @param landscape
     * @param teamforge
     */
    public void makeModel(TFSettingsModel tfSettingsModel, Model model) {
        Landscape landscape = ControllerHelper.findLandscape();
        Participant teamforge = landscape.getTeamForge();
        model.addAttribute("tfsystemid", teamforge.getSystemId());
        model.addAttribute("tfdescription", teamforge.getDescription());
        model.addAttribute("tfsettingsmodel", tfSettingsModel);
        model.addAttribute("landscape", landscape);
        model.addAttribute("participant", landscape.getParticipant());
        model.addAttribute("selectedLink", ControllerConstants.TFSETTINGS);
        model.addAttribute("tfusernameversion", tfSettingsModel
                .getTfUserNameLandscapeConfig().getVersion());
        model.addAttribute("tfusernameid", tfSettingsModel
                .getTfUserNameLandscapeConfig().getId());
        model.addAttribute("tfpasswordversion", tfSettingsModel
                .getTfPasswordLandscapeConfig().getVersion());
        model.addAttribute("tfpasswordid", tfSettingsModel
                .getTfPasswordLandscapeConfig().getId());
        model.addAttribute("teamforgeversion", tfSettingsModel.getTeamforge()
                .getVersion());
        model.addAttribute("teamforgeid", tfSettingsModel.getTeamforge()
                .getId());
        model.addAttribute("landscapeversion", tfSettingsModel.getLandscape()
                .getVersion());
        model.addAttribute("landscapeid", tfSettingsModel.getLandscape()
                .getId());
    }

    /**
     * 
     * Helper method to populate TFsettings
     * 
     */
    public void populateTFSettingsModel(TFSettingsModel tfSettingsModel) {
        Landscape landscape = ControllerHelper.findLandscape();
        Participant teamforge = landscape.getTeamForge();
        makeTFSettingsModel(tfSettingsModel, landscape, teamforge);
    }

    /**
     * Helper method to update TF settings
     * 
     */
    public void updateTFSettings(TFSettingsModel tfSettingsModel, Model model,
            HttpServletRequest request) {
        Landscape landscape = ControllerHelper.findLandscape();
        mergeTFSettings(tfSettingsModel, request, landscape);
        model.addAttribute("tfsettingsmodel", tfSettingsModel);
        model.addAttribute("selectedLink", ControllerConstants.TFSETTINGS);
        FlashMap.setSuccessMessage(ControllerConstants.TF_SAVE_SUCCESS_MESSAGE);
    }

    /**
     * @param tfSettingsModel
     * @param landscape
     * @param teamforge
     */
    private void makeTFSettingsModel(TFSettingsModel tfSettingsModel,
            Landscape landscape, Participant teamforge) {
        ParticipantConfig tfUrlParticipantConfig = ParticipantConfig
                .findParticipantConfigsByParticipantAndName(teamforge,
                        ControllerConstants.CCF_PARTICIPANT_TF_URL)
                .getSingleResult();
        LandscapeConfig tfUserNameLandscapeConfig = LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_USERNAME)
                .getSingleResult();
        LandscapeConfig tfPasswordLandscapeConfig = LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD)
                .getSingleResult();
        tfPasswordLandscapeConfig.setVal(Obfuscator
                .decodePassword(tfPasswordLandscapeConfig.getVal()));
        tfSettingsModel.setTfUrlParticipantConfig(tfUrlParticipantConfig);
        tfSettingsModel.setTeamforge(teamforge);
        tfSettingsModel.setLandscape(landscape);
        tfSettingsModel.setTfUserNameLandscapeConfig(tfUserNameLandscapeConfig);
        tfSettingsModel.setTfPasswordLandscapeConfig(tfPasswordLandscapeConfig);
    }

    /**
     * @param tfSettingsModel
     * @param request
     * @param landscape
     */
    private void mergeTFSettings(TFSettingsModel tfSettingsModel,
            HttpServletRequest request, Landscape landscape) {
        Participant teamforgeTimezone = tfSettingsModel.getTeamforge();
        teamforgeTimezone.setId(Long.valueOf(request
                .getParameter(ControllerConstants.TEAMFORGE_ID)));
        teamforgeTimezone.setVersion(Integer.parseInt(request
                .getParameter(ControllerConstants.TEAMFORGE_VERSION)));
        teamforgeTimezone.merge();

        LandscapeConfig tfUsernameLandscapeConfig = LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_USERNAME)
                .getSingleResult();
        tfUsernameLandscapeConfig.setVal(tfSettingsModel
                .getTfUserNameLandscapeConfig().getVal());
        tfUsernameLandscapeConfig.merge();

        LandscapeConfig tfPasswordLandscapeConfig = LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD)
                .getSingleResult();
        tfPasswordLandscapeConfig.setVal(Obfuscator
                .encodePassword(tfSettingsModel.getTfPasswordLandscapeConfig()
                        .getVal()));
        tfPasswordLandscapeConfig.merge();

    }

}
