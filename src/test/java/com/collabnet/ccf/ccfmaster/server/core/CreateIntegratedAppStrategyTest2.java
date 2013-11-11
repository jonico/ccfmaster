package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.rmi.RemoteException;

import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ce.soap50.webservices.pluggable.PluggableComponentSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentDO;
import com.collabnet.teamforge.api.pluggable.PluggableComponentParameterDO;
import com.collabnet.teamforge.api.pluggable.PluggablePermissionDO;

public class CreateIntegratedAppStrategyTest2 {

    /**
     * note: this test needs to be in its own class. When the identical code was
     * in {@link CreateIntegratedAppStrategyTest}, it failed with a NPE in the
     * soap API code called from the {@link IntegratedApplicationClient}
     * constructor :-/
     * 
     * @author ctaylor
     * 
     */
    private class TestIntegratedApplicationClient extends IntegratedApplicationClient {
        private final PluggableComponentDO integratedApp;

        private TestIntegratedApplicationClient(
                PluggableComponentDO integratedApp) {
            super(connection);
            this.integratedApp = integratedApp;
        }

        @Override
        public PluggableComponentDO createIntegratedApplication(
                String plugName, String description, String baseUrl,
                String goUrl, String prefix, String isScmRequired,
                String requireProjPrefix, String iconFileId, String endPoint,
                PluggableComponentParameterDO[] paramDO, String adminUrl,
                PluggablePermissionDO[] permDO, String pceInputType,
                String pceResultFormat, String pceDescription, String pceTitle)
                throws RemoteException {
            createCalled = true;
            return integratedApp;

        }

        @Override
        public PluggableComponentDO getIntegratedApplicationByName(String name) {
            getAppByNameCalled = true;
            return integratedApp;
        }
    }

    boolean                                                   createCalled       = false;
    boolean                                                   getAppByNameCalled = false;
    @Mocked
    Connection                                                connection;
    @Mocked
    com.collabnet.ce.soap50.webservices.ClientSoapStubFactory stubFactorySoap50;

    @Mocked
    com.collabnet.ce.soap60.webservices.ClientSoapStubFactory stubFactorySoap60;

    @Test
    public void createAndGetAppByNameNotCalledWhenIAExists() {
        final Landscape landscape = new Landscape();
        final String plugId = "plug9999";
        final PluggableComponentDO integratedApp = new PluggableComponentDO(
                (PluggableComponentSoapDO) null) {
            @Override
            public String getId() {
                return plugId;
            }
        };
        final IntegratedApplicationClient client = new TestIntegratedApplicationClient(
                integratedApp) {
            @Override
            public String getPlugIdByBaseUrl(String baseUrl) {
                return plugId;
            }
        };
        new CreateIntegratedAppStrategy("foo", "iafEndpoint", client)
                .beforeCreate(landscape);
        assertFalse("createIntegratedApplication was called", createCalled);
        assertFalse("getIntegratedApplicationByName was called",
                getAppByNameCalled);
        assertEquals("plugId not set correctly by beforeCreate", plugId,
                landscape.getPlugId());

    }

    @Before
    public void init() {
        createCalled = false;
        getAppByNameCalled = false;
    }
}
