package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.core.utils.IconUploader;
import com.collabnet.teamforge.api.filestorage.FileStorageClient;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentDO;
import com.collabnet.teamforge.api.pluggable.PluggableComponentParameterDO;
import com.collabnet.teamforge.api.pluggable.PluggablePermissionDO;
import com.google.common.collect.ImmutableList.Builder;

public class CreateIntegratedAppStrategy extends AbstractLandscapeCreationListener {

    private static final Logger                  log               = LoggerFactory
                                                                           .getLogger(CreateIntegratedAppStrategy.class);
    private String                               baseUrl;
    private IntegratedApplicationClient          integratedAppClient;
    private String                               endPoint          = "http://localhost:8090/services/DummyService";

    private String[]                             permissionNames   = {
            "Hospital", "Identity Mappings", "Repository Mappings",
            "Reset Synchronization Status", "Pause Synchronization",
            "Mapping Rules", "Mapping Rule Templates", "CCF Core Configuration" };
    static final String                          description       = "l10n.CCFMASTER";
    static final String                          prefix            = "ccf";
    static final String                          isScmRequired     = "false";
    static final String                          requireProjPrefix = "false";
    static final String                          iconFileId        = "";
    static final PluggableComponentParameterDO[] paramDO           = {};
    static final String                          pceInputType      = "select";
    static final String                          pceResultFormat   = "list";
    static final String                          pceDescription    = "description";
    static final String                          pceTitle          = "title";
    static final Random                          rnd               = new Random();
    private FileStorageClient                    fileStorageClient;
    private boolean                              isCTF8Support;

    public CreateIntegratedAppStrategy(String baseUrl,
            String iafServiceEndpoint,
            IntegratedApplicationClient integratedAppClient,
            FileStorageClient fileStorageClient, boolean isCTF8Support) {
        this.baseUrl = baseUrl;
        this.endPoint = iafServiceEndpoint;
        this.integratedAppClient = integratedAppClient;
        this.fileStorageClient = fileStorageClient;
        this.isCTF8Support = isCTF8Support;
    }

    @Override
    public Landscape beforeCreate(Landscape landscape) {
        String plugName = landscape.getName();
        String goUrl = getBaseUrl() + "gourl/%p/%o";
        String adminUrl = getBaseUrl()
                + UIPathConstants.CREATELANDSCAPE_CCFMASTER;
        PluggablePermissionDO[] permDO = buildPermissions();
        try {
            String plugId = integratedAppClient
                    .getPlugIdByBaseUrl(getBaseUrl());
            if (plugId == null) {
                String pfx = randomPrefix();
                PluggableComponentDO integratedApplication = integratedAppClient
                        .createIntegratedApplication(plugName, description,
                                getBaseUrl(), goUrl, pfx, isScmRequired,
                                requireProjPrefix, iconFileId, endPoint,
                                paramDO, adminUrl, permDO, pceInputType,
                                pceResultFormat, pceDescription, pceTitle);
                plugId = integratedApplication.getId();
                if (isCTF8Support) {
                    IconUploader.loadIcon(plugId, fileStorageClient,
                            integratedAppClient);
                }
                // make sure the description appears properly.
                integratedAppClient.setPluggableAppMessageResource(plugId,
                        "en", description, plugName + " integration");
            }
            landscape.setPlugId(plugId);
            return landscape;
        } catch (RemoteException e) {
            final String exceptionMessage = e.getMessage();
            String msg;
            if (exceptionMessage
                    .contains("java.lang.IllegalArgumentException: Unable to parse guid")) {
                msg = "An Integrated Application with the same name/prefix/base URL already exists in TeamForge.";
            } else {
                msg = "TeamForge API error creating landscape: "
                        + exceptionMessage;
            }
            log.debug(msg, e);
            throw new CoreConfigurationException(msg, e);
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String[] getPermissionNames() {
        return permissionNames.clone();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPermissionNames(String[] permissionNames) {
        this.permissionNames = permissionNames.clone();
    }

    PluggablePermissionDO[] buildPermissions() {
        Builder<PluggablePermissionDO> builder = com.google.common.collect.ImmutableList
                .builder();
        PluggablePermissionDO defaultPermission = new PluggablePermissionDO();
        defaultPermission.setDapMappedTo("View");
        defaultPermission.setPermission("Default");
        builder.add(defaultPermission);
        for (String name : getPermissionNames()) {
            PluggablePermissionDO permission = new PluggablePermissionDO();
            permission.setPermission(name);
            builder.add(permission);
        }
        PluggablePermissionDO[] permDO = builder.build().toArray(
                new PluggablePermissionDO[0]);
        return permDO;
    }

    static String prefixFor(int num) {
        Assert.isTrue(num >= 0 && num < 0x1000000);
        // ensure the first digit in the hex representation is a letter (a,b,e or f)
        num |= 0xa00000;
        String pfx = String.format("%06x", num);
        return pfx;
    }

    static String randomPrefix() {
        // generate a six-digit (in hex) random number
        int randInt = rnd.nextInt(0x1000000);
        return prefixFor(randInt);
    }
}
