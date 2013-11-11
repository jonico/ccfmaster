package com.collabnet.ccf.ccfmaster.server.core;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ce.soap50.webservices.pluggable.PluggableComponentSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentDO;
import com.collabnet.teamforge.api.pluggable.PluggableComponentParameterDO;
import com.collabnet.teamforge.api.pluggable.PluggablePermissionDO;

import mockit.*;
import static org.junit.Assert.*;

public class CreateIntegratedAppStrategyTest {

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
            CreateIntegratedAppStrategyTest.this.createCalled = true;
            CreateIntegratedAppStrategyTest.this.adminUrl = adminUrl;
            return integratedApp;
        }

        @Override
        public PluggableComponentDO getIntegratedApplicationByName(String name) {
            CreateIntegratedAppStrategyTest.this.getAppByNameCalled = true;
            return integratedApp;
        }

        @Override
        public void setPluggableAppMessageResource(String plugId,
                String locale, String key, String value) throws RemoteException {
            CreateIntegratedAppStrategyTest.this.setPluggableAppMessageResourceCalled = true;
        }
    }

    boolean    createCalled                         = false;
    boolean    getAppByNameCalled                   = false;
    boolean    setPluggableAppMessageResourceCalled = false;
    String     adminUrl;

    @Mocked
    Connection connection;

    @Test
    public void createCalledAndGetAppByNameNotCalledWhenIADoesNotExist() {
        final Landscape landscape = new Landscape();
        landscape.setName("TestLandscapeName");
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
                return null;
            }
        };
        final String baseUrl = "foo";
        new CreateIntegratedAppStrategy(baseUrl, "iafEndpoint", client)
                .beforeCreate(landscape);
        assertTrue("createIntegratedApplication not called", createCalled);
        assertFalse("getIntegratedApplicationByName was called",
                getAppByNameCalled);
        assertTrue("setPluggableAppMessageResource was not called",
                setPluggableAppMessageResourceCalled);
        assertEquals("bad adminUrl", baseUrl
                + UIPathConstants.CREATELANDSCAPE_CCFMASTER, adminUrl);
        assertEquals("plugId not set correctly by beforeCreate", plugId,
                landscape.getPlugId());

    }

    @Before
    public void init() {
        createCalled = false;
        getAppByNameCalled = false;
        setPluggableAppMessageResourceCalled = false;
    }

    /* doesn't work; result = integratedApplication always sets to null :( */
    //	@NonStrict IntegratedApplicationClient client;
    //	//@Mocked PluggableComponentDO integratedApplication;
    //
    //	@Test
    //	public void createCalled() throws RemoteException {
    //		final Landscape landscape = new Landscape();
    //		landscape.setDescription("foo");
    //		final String plugId = "plug9999";
    //		final PluggableComponentDO integratedApplication = new PluggableComponentDO((PluggableComponentSoapDO)null) {
    //			@Override
    //			public String getId() { return plugId; }
    //		};
    //		//integratedApplication.setId(plugId);
    //		new Expectations() {
    //			{
    //				client.createIntegratedApplication(anyString, anyString, anyString, anyString, anyString, anyString, anyString, anyString, anyString, (PluggableComponentParameterDO[]) any, anyString, (PluggablePermissionDO[])any, anyString, anyString, anyString, anyString);
    //				client.getIntegratedApplicationByName("foo"); times = 1; result = integratedApplication;
    //				//integratedApplication.getId(); result = plugId;
    //				landscape.setPlugId(plugId); times = 1;
    //			}
    //		};
    //		new CreateIntegratedAppStrategy("foo", client).beforeCreate(landscape);
    //	}
}
