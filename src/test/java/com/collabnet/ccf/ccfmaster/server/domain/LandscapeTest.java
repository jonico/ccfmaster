package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.Test;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

@MockStaticEntityMethods
public class LandscapeTest {

    @Test
    public void testMethod() {
        int expectedCount = 13;
        Landscape.countLandscapes();
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl
                .expectReturn(expectedCount);
        org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl
                .playback();
        org.junit.Assert.assertEquals(expectedCount,
                Landscape.countLandscapes());
    }
}
