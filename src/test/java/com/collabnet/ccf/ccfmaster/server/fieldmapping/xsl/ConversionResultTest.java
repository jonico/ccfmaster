package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import mockit.Mocked;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConversionResultTest {
	@Mocked ConversionResult.CustomXsl customXsl;
	@Mocked ConversionResult.MapForce mapForce;
	@Mocked ConversionResult.MappingRules mappingRules;
	
	@Test
	public void customXmlResult() {
		assertNotNull(customXsl);
		ConversionResult cr = new ConversionResult(customXsl);
		assertTrue(cr.customXsl().isSome());
		assertFalse(cr.mapForce().isSome());
		assertFalse(cr.mappingRules().isSome());
	}
	@Test
	public void mapForceResult() {
		assertNotNull(mapForce);
		ConversionResult cr = new ConversionResult(mapForce);
		assertFalse(cr.customXsl().isSome());
		assertTrue(cr.mapForce().isSome());
		assertFalse(cr.mappingRules().isSome());
	}
	@Test
	public void mappingRulesResult() {
		assertNotNull(mappingRules);
		ConversionResult cr = new ConversionResult(mappingRules);
		assertFalse(cr.customXsl().isSome());
		assertFalse(cr.mapForce().isSome());
		assertTrue(cr.mappingRules().isSome());
	}
}
