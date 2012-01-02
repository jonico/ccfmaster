package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = CcfCoreStatus.class)
public class CcfCoreStatusIntegrationTest {

	@Autowired 
	private CcfCoreStatusDataOnDemand dod;
	@Autowired
	private DirectionDataOnDemand directionDataOnDemand;
	
    @Test
    public void testMarkerMethod() {
    }

	@Test
    public void testCountCcfCoreStatuses() {
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", dod.getRandomCcfCoreStatus());
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.countCcfCoreStatuses();
        org.junit.Assert.assertTrue("Counter for 'CcfCoreStatus' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindCcfCoreStatus() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod.getRandomCcfCoreStatus();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatus(id);
        org.junit.Assert.assertNotNull("Find method for 'CcfCoreStatus' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'CcfCoreStatus' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllCcfCoreStatuses() {
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", dod.getRandomCcfCoreStatus());
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.countCcfCoreStatuses();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'CcfCoreStatus', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus> result = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findAllCcfCoreStatuses();
        org.junit.Assert.assertNotNull("Find all method for 'CcfCoreStatus' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'CcfCoreStatus' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindCcfCoreStatusEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", dod.getRandomCcfCoreStatus());
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.countCcfCoreStatuses();
        if (count > 20) count = 20;
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus> result = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatusEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'CcfCoreStatus' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'CcfCoreStatus' returned an incorrect number of entries", count, result.size());
    }

	@Test(expected=UnsupportedOperationException.class)
    public void testFlush() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod.getRandomCcfCoreStatus();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatus(id);
        org.junit.Assert.assertNotNull("Find method for 'CcfCoreStatus' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCcfCoreStatus(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'CcfCoreStatus' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMerge() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod.getRandomCcfCoreStatus();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatus(id);
        boolean modified =  dod.modifyCcfCoreStatus(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus merged = (com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus) obj.merge();
//        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'CcfCoreStatus' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
	
	@Test
	public void nonPersistedDirectionHasNoCoreStatus() {
		Direction dir = directionDataOnDemand.getNewTransientDirection(Integer.MAX_VALUE);
		Assert.assertNull(dir.getId());
		Assert.assertNull(CcfCoreStatus.findCcfCoreStatus(dir.getId()));
		Assert.assertTrue(CcfCoreStatus.findCcfCoreStatusesByDirection(dir).getResultList().isEmpty());
	}

	@Test(expected=UnsupportedOperationException.class)
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", dod.getRandomCcfCoreStatus());
        Direction dir = directionDataOnDemand.getRandomDirection();
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to provide a new transient entity", obj);
        obj.setId(null);
        obj.setVersion(null);
        org.junit.Assert.assertNull("Expected 'CcfCoreStatus' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'CcfCoreStatus' identifier to no longer be null", obj.getId());
    }

	@Test(expected=UnsupportedOperationException.class)
    public void testRemove() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod.getRandomCcfCoreStatus();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'CcfCoreStatus' failed to provide an identifier", id);
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatus(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'CcfCoreStatus' with identifier '" + id + "'", com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.findCcfCoreStatus(id));
    }
}
