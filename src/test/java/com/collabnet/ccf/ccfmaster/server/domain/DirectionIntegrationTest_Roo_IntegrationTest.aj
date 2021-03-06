// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DirectionIntegrationTest_Roo_IntegrationTest {
    
    declare @type: DirectionIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: DirectionIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: DirectionIntegrationTest: @Transactional;
    
    @Test
    public void DirectionIntegrationTest.testCountDirections() {
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirections();
        org.junit.Assert.assertTrue("Counter for 'Direction' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void DirectionIntegrationTest.testFindDirection() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirection(id);
        org.junit.Assert.assertNotNull("Find method for 'Direction' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'Direction' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void DirectionIntegrationTest.testFindAllDirections() {
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirections();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'Direction', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.Direction> result = com.collabnet.ccf.ccfmaster.server.domain.Direction.findAllDirections();
        org.junit.Assert.assertNotNull("Find all method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'Direction' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void DirectionIntegrationTest.testFindDirectionEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction.countDirections();
        if (count > 20) count = 20;
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.Direction> result = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirectionEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'Direction' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'Direction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void DirectionIntegrationTest.testFlush() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirection(id);
        org.junit.Assert.assertNotNull("Find method for 'Direction' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDirection(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'Direction' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DirectionIntegrationTest.testMerge() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirection(id);
        boolean modified =  dod.modifyDirection(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.collabnet.ccf.ccfmaster.server.domain.Direction merged = (com.collabnet.ccf.ccfmaster.server.domain.Direction) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'Direction' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DirectionIntegrationTest.testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getRandomDirection();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirection(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'Direction' with identifier '" + id + "'", com.collabnet.ccf.ccfmaster.server.domain.Direction.findDirection(id));
    }
    
}
