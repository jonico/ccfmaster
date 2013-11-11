package com.collabnet.ccf.ccfmaster.web.helper;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.ccf.ccfmaster.controller.web.ControllerConstants;
import com.collabnet.teamforge.api.Connection;

public class TeamForgeConnectionHelper {

    private static final String METADATA          = " : Metadata";
    private static final String FOLDER_CLOSED_GIF = "folder_closed.gif";

    /**
     * Helper method to get repository data for the given repository id
     * 
     */
    public static String getRepositoryData(String sourceRepositoryId,
            Model model, HttpServletRequest request) {
        RequestContext ctx = new RequestContext(request);
        Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        TFUserDetails tfUser = (TFUserDetails) user;
        String repositoryDetail = null;
        try {
            Connection teamforgeConnection = tfUser.getConnection();
            if (RepositoryConnections.isTrackerRepository(sourceRepositoryId)) {
                repositoryDetail = teamforgeConnection.getTrackerClient()
                        .getTrackerData(sourceRepositoryId).getIcon()
                        + "-"
                        + teamforgeConnection.getTrackerClient()
                                .getTrackerData(sourceRepositoryId).getTitle()
                        + "-" + sourceRepositoryId;
            } else if (RepositoryConnections
                    .isPlanningFolderRepository(sourceRepositoryId)) {
                String sourceProjectId = RepositoryConnections
                        .extractProjectFromRepositoryId(sourceRepositoryId);
                repositoryDetail = FOLDER_CLOSED_GIF
                        + "-"
                        + teamforgeConnection.getTeamForgeClient()
                                .getProjectData(sourceProjectId).getTitle()
                        + "-" + sourceProjectId;
            } else if (RepositoryConnections
                    .isTrackerMetaDataRepository(sourceRepositoryId)) {
                String sourceProjectId = RepositoryConnections
                        .extractTrackerFromMetaDataRepositoryId(sourceRepositoryId);
                repositoryDetail = teamforgeConnection.getTrackerClient()
                        .getTrackerData(sourceProjectId).getIcon()
                        + "-"
                        + teamforgeConnection.getTrackerClient()
                                .getTrackerData(sourceProjectId).getTitle()
                        + METADATA + "-" + sourceProjectId;
            }
        } catch (RemoteException remoteException) {
            model.addAttribute("connectionerror",
                    ctx.getMessage(ControllerConstants.TEAMFORGE)
                            + remoteException.getMessage());
        }
        return repositoryDetail;
    }

    public static Connection teamForgeConnection() throws RemoteException {
        Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        TFUserDetails tfUser = (TFUserDetails) user;
        Connection teamforgeConnection = tfUser.getConnection();
        return teamforgeConnection;
    }

}
