package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingLandscapeTemplatePersisterFactory;

@RooIntegrationTest(entity = FieldMappingLandscapeTemplate.class)
public class FieldMappingLandscapeTemplateIntegrationTest {

	@Autowired FieldMappingLandscapeTemplateDataOnDemand fmltdod;
	
	@Test(expected=ConstraintViolationException.class)
	public void throwsWhenBadName() {
		FieldMappingLandscapeTemplate fmlt = fmltdod.getNewTransientFieldMappingLandscapeTemplate(42);
		fmlt.setName("$!@invalid");
		fmlt.persist();
	}
	
	@Test
	public void savesOnPersistAndMerge() {
		FieldMappingLandscapeTemplate fmlt = fmltdod.getNewTransientFieldMappingLandscapeTemplate(42);
		MockFieldMappingLandscapeTemplatePersisterFactory mockFmltpf = new MockFieldMappingLandscapeTemplatePersisterFactory();
		fmlt.setPersisterFactory(mockFmltpf);
		fmlt.persist();
		assertTrue("save wasn't called on persist().", mockFmltpf.calledSave);

		mockFmltpf = new MockFieldMappingLandscapeTemplatePersisterFactory();
		fmlt = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplate(fmlt.getId());
		assertNotNull("couldn't find fieldMappingLandscapeTemplate after persist.", fmlt);
		fmlt.setPersisterFactory(mockFmltpf);
		fmlt = fmlt.merge();
		assertTrue("save wasn't called on merge().", mockFmltpf.calledSave);
		
	}
	
    @Test
    public void testMarkerMethod() {
    }
}
