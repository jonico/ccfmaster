package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;

public interface FieldMappingRuleConverterFactory {
	// public FieldMappingRuleConverter get(FieldMappingRule rule);
	/** ugly hack to allow rules to access their parent's valueMaps by name */
	public FieldMappingRuleConverter get(FieldMappingRule rule, List<FieldMappingValueMap> valueMaps);
}