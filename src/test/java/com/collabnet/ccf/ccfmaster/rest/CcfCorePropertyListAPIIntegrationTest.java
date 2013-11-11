package com.collabnet.ccf.ccfmaster.rest;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.config.CoreConfigLoader;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;

public class CcfCorePropertyListAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionDataOnDemand dirdod;

    @Autowired
    private CoreConfigLoader      coreConfigLoader;

    @Test(expected = HttpClientErrorException.class)
    public void testCreate() {
        try {
            Direction dir = dirdod.getRandomDirection();
            Assert.assertNotNull(
                    "Data on demand for 'Direction' failed to initialize correctly",
                    dir);
            Long id = dir.getId();
            Assert.assertNotNull(
                    "Data on demand for 'Direction' failed to provide an identifier",
                    id);
            CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                    + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
            restTemplate.postForObject(ccfAPIUrl + "/ccfcoreproperties", obj,
                    CCFCorePropertyList.class);
        } catch (HttpClientErrorException e) {
            // create operation is considered to be a bad request
            Assert.assertEquals(401, e.getStatusCode().value());
            throw e;
        }
    }

    @Test
    public void testFind() throws JAXBException, IOException {
        Direction dir = dirdod.getRandomDirection();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to initialize correctly",
                dir);
        Long id = dir.getId();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to provide an identifier",
                id);
        CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
        Assert.assertNotNull(
                "Find method for 'CCFCorePropertyList' illegally returned null for id '"
                        + id + "'", obj);
        Assert.assertNotNull(coreConfigLoader
                .getDefaultCCFCorePropertyList(dir));
        Assert.assertEquals(obj.getCcfCoreProperties().size(), coreConfigLoader
                .getDefaultCCFCorePropertyList(dir).size());

    }

    @Test(expected = HttpClientErrorException.class)
    public void testRemove() {
        Direction dir = dirdod.getRandomDirection();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to initialize correctly",
                dir);
        Long id = dir.getId();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to provide an identifier",
                id);
        try {
            restTemplate.delete(ccfAPIUrl + "/ccfcoreproperties/" + id);
        } catch (HttpClientErrorException e) {
            // delete operation is considered to be a bad request
            Assert.assertEquals(401, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testShowWithInvalidId() throws JAXBException, IOException {
        try {
            CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                    + "/ccfcoreproperties/101", CCFCorePropertyList.class);
            Assert.assertNull(obj);
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(404, e.getStatusCode().value());
            throw e;
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void testUpdate() {
        Direction dir = dirdod.getRandomDirection();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to initialize correctly",
                dir);
        Long id = dir.getId();
        Assert.assertNotNull(
                "Data on demand for 'Direction' failed to provide an identifier",
                id);
        try {
            CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                    + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
            Assert.assertNotNull(
                    "Find method for 'CCFCorePropertyList' illegally returned null for id '"
                            + id + "'", obj);
            restTemplate.put(ccfAPIUrl + "/ccfcoreproperties/" + id, obj);
        } catch (HttpClientErrorException e) {
            // update operation is considered to be a bad request
            Assert.assertEquals(401, e.getStatusCode().value());
            throw e;
        }
    }
}
