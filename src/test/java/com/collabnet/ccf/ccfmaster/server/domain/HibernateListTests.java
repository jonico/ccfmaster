package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@ContextConfiguration
public class HibernateListTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired FieldMappingDataOnDemand fmdod;
	@Autowired FieldMappingLandscapeTemplateDataOnDemand fmltdod;
	@Autowired FieldMappingExternalAppTemplateDataOnDemand fmeatdod;
	@Autowired FieldMappingValueMapDataOnDemand fmvmdod;
	private List<FieldMappingValueMap> fmValueMaps;
	private List<FieldMappingValueMap> fmltValueMaps;
	private List<FieldMappingValueMap> fmeatValueMaps;
	private FieldMapping fm;
	private FieldMappingLandscapeTemplate fmlt;
	private FieldMappingExternalAppTemplate fmeat;
	// used by fiveValueMaps to generate "random" fmvm's
	private int idx = 42;

	@Before
	public void setUp() {
		fmValueMaps = fiveValueMaps();
		fm = fmdod.getRandomFieldMapping();
		fm.getValueMaps().addAll(fmValueMaps);
		fm.merge();

		fmltValueMaps = fiveValueMaps();
		fmlt = fmltdod.getRandomFieldMappingLandscapeTemplate();
		fmlt.getValueMaps().addAll(fmltValueMaps);
		fmlt.merge();

		fmeatValueMaps = fiveValueMaps();
		fmeat = fmeatdod.getRandomFieldMappingExternalAppTemplate();
		fmeat.getValueMaps().addAll(fmeatValueMaps);
		fmeat.merge();
	}

	private ImmutableList<FieldMappingValueMap> fiveValueMaps() {
		Builder<FieldMappingValueMap> builder = ImmutableList.builder();
		for(int i = 0; i < 5; i++, idx ++) {
			final FieldMappingValueMap fmvm = fmvmdod.getNewTransientFieldMappingValueMap(idx);
			builder.add(fmvm);
		}
		final ImmutableList<FieldMappingValueMap> res = builder.build();
		return res;
	}
	@Test
	public void removeDoesNotDeleteOtherValueMaps() {
		FieldMappingValueMap removed = fm.getValueMaps().remove(0);
		fm.merge();
		Assert.assertNull(FieldMappingValueMap.findFieldMappingValueMap(removed.getId()));
		for (FieldMappingValueMap fmvm : fmltValueMaps) {
			Assert.assertTrue(
					String.format("missing fmltValueMaps[%d]: %s", fmltValueMaps.indexOf(fmvm), fmvm),
					fmlt.getValueMaps().contains(fmvm));
		}
		for (FieldMappingValueMap fmvm : fmeatValueMaps) {
			Assert.assertTrue(
					String.format("missing fmeatValueMaps[%d]: %s", fmeatValueMaps.indexOf(fmvm), fmvm),
					fmeat.getValueMaps().contains(fmvm));
		}
//		Assert.assertEquals(fmltValueMaps, fmlt.getValueMaps());
//		Assert.assertEquals(fmeatValueMaps, fmeat.getValueMaps());
	}


	@Test
	public void canRemoveAndAddAgainToList() {
		FieldMappingValueMap removed = fm.getValueMaps().remove(0);
		fm.getValueMaps().add(removed);
		fm.merge();
		Assert.assertNotNull(FieldMappingValueMap.findFieldMappingValueMap(removed.getId()));
		for (FieldMappingValueMap fmvm : fmltValueMaps) {
			Assert.assertTrue(
					String.format("missing fmltValueMaps[%d]: %s", fmltValueMaps.indexOf(fmvm), fmvm),
					fmlt.getValueMaps().contains(fmvm));
		}
		for (FieldMappingValueMap fmvm : fmeatValueMaps) {
			Assert.assertTrue(
					String.format("missing fmeatValueMaps[%d]: %s", fmeatValueMaps.indexOf(fmvm), fmvm),
					fmeat.getValueMaps().contains(fmvm));
		}
	}
}
