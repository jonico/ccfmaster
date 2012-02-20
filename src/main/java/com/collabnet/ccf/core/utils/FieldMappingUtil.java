package com.collabnet.ccf.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;

public class FieldMappingUtil {

	/**
	 * create copy of new fieldmappingrule object where id and version of the given object are excluded.
	 * @param fieldMappingRule
	 * @param rules
	 * @param fieldMappingLandscapeTemplatelist
	 * @return
	 */
	public static List<FieldMappingRule> createFieldMappingRule(List<FieldMappingRule> rules) {
		List<FieldMappingRule> newrules = new ArrayList<FieldMappingRule>();
		for (FieldMappingRule fmrules : rules) {
			FieldMappingRule fieldMappingRule=new FieldMappingRule();
			fieldMappingRule.setName(fmrules.getName());
			fieldMappingRule.setDescription(fmrules.getDescription());
			fieldMappingRule.setType(fmrules.getType());
			fieldMappingRule.setXmlContent(fmrules.getXmlContent());
			fieldMappingRule.setSource(fmrules.getSource());
			fieldMappingRule.setTarget(fmrules.getTarget());
			fieldMappingRule.setSourceIsTopLevelAttribute(fmrules.isSourceIsTopLevelAttribute());
			fieldMappingRule.setTargetIsTopLevelAttribute(fmrules.isTargetIsTopLevelAttribute());
			fieldMappingRule.setValueMapName(fmrules.getValueMapName());
			fieldMappingRule.setCondition(fieldMappingRule.getCondition());
			newrules.add(fieldMappingRule);		
		}

		return newrules;
	}


	/**
	 * create copy of new fieldmappingvaluemap object where id and version of the given object are excluded.
	 * @param FieldMappingValueMap
	 * @param valueMap
	 * @param FieldMappingValueMap
	 * @return
	 */
	public static List<FieldMappingValueMap> createFieldMappingValueMap(List<FieldMappingValueMap> valueMap) {
		List<FieldMappingValueMap> newValueMaps = new ArrayList<FieldMappingValueMap>();
		for (FieldMappingValueMap fmvaluemaps : valueMap) {
			FieldMappingValueMap fieldMappingValueMap=new FieldMappingValueMap();
			fieldMappingValueMap.setDefaultValue(fmvaluemaps.getDefaultValue());
			List<FieldMappingValueMapEntry> fieldMappingValueMapEntry=createFieldMappingValueMapEntry(fmvaluemaps.getEntries());
			fieldMappingValueMap.setEntries(fieldMappingValueMapEntry);
			fieldMappingValueMap.setHasDefault(fmvaluemaps.isHasDefault());
			fieldMappingValueMap.setName(fmvaluemaps.getName());
			newValueMaps.add(fieldMappingValueMap);		
		}
		return newValueMaps;
	}

	/**
	 * create copy of new fieldmappingvaluemapentry object where id and version of the given object are excluded.
	 * @param FieldMappingValueMap
	 * @param valueMap
	 * @param FieldMappingValueMap
	 * @return
	 */
	public static List<FieldMappingValueMapEntry> createFieldMappingValueMapEntry(List<FieldMappingValueMapEntry> valueMapEntry) {
		List<FieldMappingValueMapEntry> newValueMapeEntry= new ArrayList<FieldMappingValueMapEntry>();
		for (FieldMappingValueMapEntry fmValueMapEntry : valueMapEntry) {
			 FieldMappingValueMapEntry newFieldMappingValueMapEntry =new FieldMappingValueMapEntry();
			 newFieldMappingValueMapEntry.setSource(fmValueMapEntry.getSource());
			 newFieldMappingValueMapEntry.setTarget(fmValueMapEntry.getTarget());
			 newValueMapeEntry.add(newFieldMappingValueMapEntry);
		}
		return newValueMapeEntry;
	}

}
