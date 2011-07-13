// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntryDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FieldMappingValueMapEntryIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FieldMappingValueMapEntryIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FieldMappingValueMapEntryIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: FieldMappingValueMapEntryIntegrationTest: @Transactional;
    
    @Autowired
    private FieldMappingValueMapEntryDataOnDemand FieldMappingValueMapEntryIntegrationTest.dod;
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testCountFieldMappingValueMapEntrys() {
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", dod.getRandomFieldMappingValueMapEntry());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.countFieldMappingValueMapEntrys();
        org.junit.Assert.assertTrue("Counter for 'FieldMappingValueMapEntry' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testFindFieldMappingValueMapEntry() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry obj = dod.getRandomFieldMappingValueMapEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntry(id);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingValueMapEntry' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'FieldMappingValueMapEntry' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testFindAllFieldMappingValueMapEntrys() {
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", dod.getRandomFieldMappingValueMapEntry());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.countFieldMappingValueMapEntrys();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'FieldMappingValueMapEntry', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry> result = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findAllFieldMappingValueMapEntrys();
        org.junit.Assert.assertNotNull("Find all method for 'FieldMappingValueMapEntry' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'FieldMappingValueMapEntry' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testFindFieldMappingValueMapEntryEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", dod.getRandomFieldMappingValueMapEntry());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.countFieldMappingValueMapEntrys();
        if (count > 20) count = 20;
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry> result = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntryEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'FieldMappingValueMapEntry' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'FieldMappingValueMapEntry' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testFlush() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry obj = dod.getRandomFieldMappingValueMapEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntry(id);
        org.junit.Assert.assertNotNull("Find method for 'FieldMappingValueMapEntry' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFieldMappingValueMapEntry(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'FieldMappingValueMapEntry' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testMerge() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry obj = dod.getRandomFieldMappingValueMapEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntry(id);
        boolean modified =  dod.modifyFieldMappingValueMapEntry(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry merged = (com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'FieldMappingValueMapEntry' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", dod.getRandomFieldMappingValueMapEntry());
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry obj = dod.getNewTransientFieldMappingValueMapEntry(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'FieldMappingValueMapEntry' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'FieldMappingValueMapEntry' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FieldMappingValueMapEntryIntegrationTest.testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry obj = dod.getRandomFieldMappingValueMapEntry();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FieldMappingValueMapEntry' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntry(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'FieldMappingValueMapEntry' with identifier '" + id + "'", com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry.findFieldMappingValueMapEntry(id));
    }
    
}
