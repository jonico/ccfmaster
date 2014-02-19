package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = RepositoryMappingDirection.class)
public class RepositoryMappingDirectionIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionDataOnDemand dod;
    @Autowired
    FieldMappingDataOnDemand                       fmDod;

    @Test(expected = IllegalArgumentException.class)
    public void invalidParentThrowsException() {
        RepositoryMappingDirection rmd1 = dod
                .getSpecificRepositoryMappingDirection(0);
        RepositoryMappingDirection rmd2 = dod
                .getSpecificRepositoryMappingDirection(1);
        assertFalse(rmd1.getId().equals(rmd2.getId()));
        FieldMapping fm = fmDod.getRandomFieldMapping();
        if (!fm.getParent().getId().equals(rmd1.getId())) {
            rmd1.setActiveFieldMapping(fm);
            rmd1.merge();
        } else {
            rmd2.setActiveFieldMapping(fm);
            rmd2.merge();
        }
    }

    @Test
    public void removeWhileFieldMappingStillActive() {
        FieldMapping fm = fmDod.getRandomFieldMapping();
        RepositoryMappingDirection rmd = fm.getParent();
        rmd.setActiveFieldMapping(fm);
        rmd.merge();
        //rmd.remove();
        fm.remove();
        rmd = RepositoryMappingDirection.findRepositoryMappingDirection(rmd
                .getId());
        System.out.println(rmd.getActiveFieldMapping().toString());
    }

    @Test
    public void testMarkerMethod() {
    }

    @Test
    public void testPersist() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly",
                        dod.getRandomRepositoryMappingDirection());
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getNewTransientRepositoryMappingDirection(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'RepositoryMappingDirection' failed to provide a new transient entity",
                        obj);
        org.junit.Assert.assertNull(
                "Expected 'RepositoryMappingDirection' identifier to be null",
                obj.getId());

        // we do not persist due to unique key constraints
        //obj.persist();
        //obj.flush();
        //org.junit.Assert.assertNotNull("Expected 'RepositoryMappingDirection' identifier to no longer be null", obj.getId());
    }

}
