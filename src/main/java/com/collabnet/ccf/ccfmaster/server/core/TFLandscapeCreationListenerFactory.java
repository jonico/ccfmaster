package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.authentication.TFUserDetails;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.TeamForgeClient;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;

public class TFLandscapeCreationListenerFactory implements LandscapeCreationListenerFactory {

    private String              baseUrl;
    private String              iafServiceEndpoint     = "http://localhost:8090/services/DummyService";
    private static final String CTF6_API_VERSION       = "6.2.1.0";
    private static final String CTF7_DUMMY_SERVICE_URL = "http://localhost:8080/ce-soap/services/IAFDummyService";

    @Override
    public CreateIntegratedAppStrategy get() {
        Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Assert.isInstanceOf(TFUserDetails.class, user,
                "User is not logged in via TeamForge");
        try {
            Connection tfConnection = ((TFUserDetails) user).getConnection();
            TeamForgeClient tfClient = tfConnection.getTeamForgeClient();
            IntegratedApplicationClient integratedAppClient = tfConnection
                    .getIntegratedAppClient();
            return new CreateIntegratedAppStrategy(baseUrl,
                    getIafServiceEndpoint(tfClient), integratedAppClient,
                    tfConnection.getFileStorageClient(),
                    tfConnection.supports65());
        } catch (RemoteException e) {
            throw new CoreConfigurationException(e);
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getIafServiceEndpoint() {
        return iafServiceEndpoint;
    }

    public String getIafServiceEndpoint(TeamForgeClient tfClient)
            throws RemoteException {
        String apiVersion = tfClient.getApiVersion();
        if (apiVersion.compareTo(CTF6_API_VERSION) > 0) {
            return CTF7_DUMMY_SERVICE_URL;
        } else {
            return getIafServiceEndpoint();
        }
    }

    public void setBaseUrl(String baseUrl) {
        // ensure that the Base URL ends with a slash.
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        this.baseUrl = baseUrl;
    }

    public void setIafServiceEndpoint(String iafServiceEndpoint) {
        this.iafServiceEndpoint = iafServiceEndpoint;
    }

}
