package com.collabnet.ccf.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import org.apache.commons.io.FileUtils;

import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.Connection.Builder;
import com.collabnet.teamforge.api.filestorage.FileStorageClient;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentDO;

public class IconUploader {

    public void updateIAFIcon() throws RemoteException {
        Landscape landscape = ControllerHelper.findLandscape();
        if (landscape != null) {
            Builder connectionBuilder = Connection.builder(getTFUrl(landscape));
            Connection tfConnection = connectionBuilder
                    .userNamePassword(getTFUserName(landscape),
                            getTFPassword(landscape)).connectionCached(false)
                    .build();
            IntegratedApplicationClient integratedAppClient = tfConnection
                    .getIntegratedAppClient();
            PluggableComponentDO plugDO = integratedAppClient
                    .getIntegratedApplicationByName(landscape.getName());
            String iconKey = plugDO.getIconKey();
            if (iconKey == null && tfConnection.supports65()) {
                loadIcon(plugDO.getId(), tfConnection.getFileStorageClient(),
                        integratedAppClient);
            }
        }
    }

    private String getTFPassword(Landscape landscape) {
        String password = LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_PASSWORD)
                .getSingleResult().getVal();
        return Obfuscator.decodePassword(password);
    }

    private String getTFUrl(Landscape landscape) {
        return ParticipantConfig
                .findParticipantConfigsByParticipantAndName(
                        landscape.getTeamForge(),
                        ControllerConstants.CCF_PARTICIPANT_TF_URL)
                .getSingleResult().getVal();
    }

    private String getTFUserName(Landscape landscape) {
        return LandscapeConfig
                .findLandscapeConfigsByLandscapeAndName(landscape,
                        ControllerConstants.CCF_LANDSCAPE_TF_USERNAME)
                .getSingleResult().getVal();
    }

    public static void loadIcon(String plugId, FileStorageClient fileStorage,
            IntegratedApplicationClient integratedAppClient) {
        File fileIcon = new File("ccf.png");
        try {
            if (plugId != null) {
                File tempIconFile = new File(FileUtils.getTempDirectory()
                        + File.separator + fileIcon);
                FileUtils.copyInputStreamToFile(
                        IconUploader.class.getResourceAsStream("ccf.png"),
                        tempIconFile);
                String iconFileKey = fileStorage.uploadFile(new DataHandler(
                        tempIconFile.toURL()));
                integratedAppClient.setIntegratedApplicationIcon(plugId,
                        iconFileKey, "ccf.png", "image/png");
            }
        } catch (MalformedURLException e) {
            throw new CoreConfigurationException(e);
        } catch (IOException e) {
            throw new CoreConfigurationException(e);
        }
    }

}
