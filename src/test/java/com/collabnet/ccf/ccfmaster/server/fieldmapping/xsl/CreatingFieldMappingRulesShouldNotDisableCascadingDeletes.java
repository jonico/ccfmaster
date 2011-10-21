package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class CreatingFieldMappingRulesShouldNotDisableCascadingDeletes extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired private FieldMappingDataOnDemand fmdod;
	private FieldMapping fm;
	
	@Before
	public void setup() {
		fm = fmdod.getNewTransientFieldMapping(42);
		fm.persist();
		fm.getRules().clear();
		fm.setKind(FieldMappingKind.CUSTOM_XSLT);
		FieldMappingDataOnDemand.processMappingKind(fm);
//		for (FieldMappingRule rule : fm.getRules()) {
//			rule.persist();
//		}
		fm = fm.merge();
		assertEquals(1, fm.getRules().size());
	}
	
	@Test
	public void deleteLandscape() {
		Landscape landscape = fm.getParent().getRepositoryMapping().getExternalApp().getLandscape();
		long id = landscape.getId();
		landscape.remove();
		landscape.flush();
		assertNull(Landscape.findLandscape(id));
	}

	@Test(expected=JpaSystemException.class)
	public void deleteActiveFieldMappingShouldFail() {
		assertNotNull(fm);
		final RepositoryMappingDirection parent = fm.getParent();
		parent.setActiveFieldMapping(fm);
		parent.merge();
		long id = fm.getId();
		fm.remove();
		fm.flush();
		assertNull(FieldMapping.findFieldMapping(id));
	}
	
	@Test
	public void deleteInactiveFieldMapping() {
		assertNotNull(fm);
		final RepositoryMappingDirection parent = fm.getParent();
		assertTrue("mapping is active", parent.getActiveFieldMapping()==null || parent.getActiveFieldMapping().getId().equals(fm.getId()));
		long id = fm.getId();
		fm.remove();
		fm.flush();
		assertNull(FieldMapping.findFieldMapping(id));
	}
	

	@Test
	public void deleteRepositoryMappingDirection() {
		RepositoryMappingDirection rmd = fm.getParent();
		long id = rmd.getId();
		rmd.remove();
		rmd.flush();
		assertNull(RepositoryMappingDirection.findRepositoryMappingDirection(id));
	}

	@Test(expected=CoreConfigurationException.class)
	public void removeFieldMappingRuleFromList() {
		FieldMappingRule fmr = fm.getRules().get(0);
		long id = fmr.getId();
		fm.getRules().remove(0);
		fm.merge();
		assertNull(FieldMappingRule.findFieldMappingRule(id));
	}

	
	@Test(expected=ObjectRetrievalFailureException.class)
	public void deleteFieldMappingRuleShouldFail() {
		FieldMappingRule fmr = fm.getRules().get(0);
		long id = fmr.getId();
		fmr.remove();
		fmr.flush();
		assertNull(FieldMappingRule.findFieldMappingRule(id));
	}
}
