package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingPersisterFactory;

@RooIntegrationTest(entity = FieldMapping.class)
public class FieldMappingIntegrationTest {

    @Autowired
    FieldMappingDataOnDemand fmdod;

    @Test
    public void doesNotPersistWhenScopeIsNotRMD() {
        MockFieldMappingPersisterFactory mockFmpf = new MockFieldMappingPersisterFactory();
        FieldMapping fm = fmdod.getNewTransientFieldMapping(42);
        fm.setPersisterFactory(mockFmpf);
        fm.setScope(FieldMappingScope.CCF_CORE);
        assertFalse(fm.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
        fm.persist();
        assertFalse("save was called on persist.", mockFmpf.calledSave);

        mockFmpf = new MockFieldMappingPersisterFactory();
        fm = FieldMapping.findFieldMapping(fm.getId());
        assertNotNull("couldn't find fieldMapping after persist.", fm);
        fm.setPersisterFactory(mockFmpf);
        fm = fm.merge();
        assertFalse("save was called on merge().", mockFmpf.calledSave);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void paramMustBeAlphaNumerical() {
        FieldMapping fm = fmdod.getNewTransientFieldMapping(23);
        fm.setName("illegalParam.xsl");
        fm.persist();
    }

    @Test
    public void persistsWhenScopeIsRMD() {
        MockFieldMappingPersisterFactory mockFmpf = new MockFieldMappingPersisterFactory();
        FieldMapping fm = fmdod.getNewTransientFieldMapping(42);
        fm.setPersisterFactory(mockFmpf);
        assertTrue(fm.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
        fm.persist();
        assertTrue("save wasn't called on persist().", mockFmpf.calledSave);

        mockFmpf = new MockFieldMappingPersisterFactory();
        fm = FieldMapping.findFieldMapping(fm.getId());
        assertNotNull("couldn't find fieldMapping after persist.", fm);
        fm.setPersisterFactory(mockFmpf);
        fm = fm.merge();
        assertTrue("save wasn't called on merge().", mockFmpf.calledSave);
    }

    @Test
    public void testMarkerMethod() {
    }
}
