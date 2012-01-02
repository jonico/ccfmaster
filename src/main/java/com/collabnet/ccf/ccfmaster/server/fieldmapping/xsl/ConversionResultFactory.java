package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.springframework.stereotype.Service;

import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.CustomXsl;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MapForce;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

@Service
public class ConversionResultFactory {
	public ConversionResult get(Mapping<?> mapping) {
		switch (mapping.getKind()) {
		case CUSTOM_XSLT:
			CustomXsl customXsl = new ConversionResult.CustomXsl(mapping);
			return new ConversionResult(customXsl);
		case MAPFORCE:
			MapForce mapforce = new ConversionResult.MapForce(mapping);
			return new ConversionResult(mapforce);
		case MAPPING_RULES:
			MappingRules mappingRules = new ConversionResult.MappingRules(mapping);
			return new ConversionResult(mappingRules);
		default:
			throw new IllegalArgumentException("unsupported kind: " + mapping.getKind());
		}
	}
}
