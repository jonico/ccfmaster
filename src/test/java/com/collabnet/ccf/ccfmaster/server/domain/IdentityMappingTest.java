package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

@MockStaticEntityMethods
public class IdentityMappingTest {

    @Test
    public void testMethod() {
        int expectedCount = 13;
        IdentityMapping.countIdentityMappings();
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl.expectReturn(expectedCount);
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl.playback();
        org.junit.Assert.assertEquals(expectedCount, IdentityMapping.countIdentityMappings());
    }
}
