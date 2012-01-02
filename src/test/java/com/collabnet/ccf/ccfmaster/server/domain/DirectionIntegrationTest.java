package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

//@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@RooIntegrationTest(entity = Direction.class)
public class DirectionIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private DirectionDataOnDemand dod;

	@Test
    public void testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to initialize correctly", dod.getRandomDirection());
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod.getNewTransientDirection(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'Direction' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'Direction' identifier to be null", obj.getId());
        
        // We do not persist due to unique key constraint
        //obj.persist();
        //obj.flush();
        //org.junit.Assert.assertNotNull("Expected 'Direction' identifier to no longer be null", obj.getId());
    }
}
