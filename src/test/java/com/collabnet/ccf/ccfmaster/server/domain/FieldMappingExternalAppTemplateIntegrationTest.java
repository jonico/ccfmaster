package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingExternalAppTemplatePersisterFactory;

@RooIntegrationTest(entity = FieldMappingExternalAppTemplate.class)
public class FieldMappingExternalAppTemplateIntegrationTest {
	@Autowired
	FieldMappingExternalAppTemplateDataOnDemand fmeatdod;
	
	@Test
	public void savesOnPersistAndMerge() {
		FieldMappingExternalAppTemplate fmeat = fmeatdod.getNewTransientFieldMappingExternalAppTemplate(42);
		MockFieldMappingExternalAppTemplatePersisterFactory mockFmeatpf = new MockFieldMappingExternalAppTemplatePersisterFactory();
		fmeat.setPersisterFactory(mockFmeatpf);
		fmeat.persist();
		assertTrue("save wasn't called on persist().", mockFmeatpf.calledSave);

		mockFmeatpf = new MockFieldMappingExternalAppTemplatePersisterFactory();
		fmeat = FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplate(fmeat.getId());
		assertNotNull("couldn't find fieldMappingExternalAppTemplate after persist.", fmeat);
		fmeat.setPersisterFactory(mockFmeatpf);
		fmeat = fmeat.merge();
		assertTrue("save wasn't called on merge().", mockFmeatpf.calledSave);
		
	}

    @Test
    public void testMarkerMethod() {
    }
}
