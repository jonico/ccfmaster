package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.dom4j.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules.*;
import static org.junit.Assert.*;

@ContextConfiguration
public class ConversionResultMappingRulesTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	private static final String RULENAME = "RULENAME";

	private static final String CONDITION = "CONDITION";

	private static final String TARGET = "TARGET";

	private static final String SOURCE = "SOURCE";

	private final Logger log = LoggerFactory.getLogger(ConversionResultMappingRulesTest.class);
	
	private final ConversionResultFactory resultFactory = new ConversionResultFactory();
	@Autowired FieldMappingDataOnDemand dod;
	
	@Test
	public void testEmptyMappingRules() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final MappingRules mappingRules = res.mappingRules().get();
		final Element xsl = mappingRules.asXml();
		assertTrue("weird, I found a rule.", xsl.selectNodes("//xsl:element[@name='TARGET']").isEmpty());
	}
	
	@Test
	public void testDirectFieldMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.DIRECT_FIELD, false);
		fm.getRules().add(rule);

		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:element[@name='%s']",
				XPATH_ELEMENT, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testDirectConstantMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.DIRECT_CONSTANT, false);
		fm.getRules().add(rule);

		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:element[@name='%s']",
				XPATH_CONSTANT_ELEMENT, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testConditionalFieldMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.CONDITIONAL_FIELD, false);
		fm.getRules().add(rule);

		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:if[@test='%s']/xsl:element[@name='%s']", 
				XPATH_ELEMENT, CONDITION, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testConditionalConstantMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.CONDITIONAL_CONSTANT, false);
		fm.getRules().add(rule);

		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:if[@test='%s']/xsl:element[@name='%s']", 
				XPATH_CONSTANT_ELEMENT, CONDITION, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	public FieldMappingRule getRule(final FieldMappingRuleType type,
			final boolean targetIsTlaAttribute) {
		FieldMappingRule rule = new FieldMappingRule();
		rule.setName(RULENAME);
		rule.setType(type);
		rule.setSource(SOURCE);
		rule.setTarget(TARGET);
		rule.setCondition(CONDITION);
		rule.setTargetIsTopLevelAttribute(targetIsTlaAttribute);
		return rule;
	}

	private FieldMapping getMappingRulesFieldMapping() {
		FieldMapping fm;
		do {
			fm = dod.getNewTransientFieldMapping(42);
		} while (fm.getKind() != FieldMappingKind.MAPPING_RULES);
		return fm;
	}

	@Test
	public void testDirectFieldTlaMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.DIRECT_FIELD, true);
		fm.getRules().add(rule);
	
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:element[@name='%s']",
				XPATH_TOP_LEVEL_ATTRIBUTE, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testDirectConstantTlaMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.DIRECT_CONSTANT, true);
		fm.getRules().add(rule);
	
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:element[@name='%s']",
				XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testConditionalFieldTlaMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.CONDITIONAL_FIELD, true);
		fm.getRules().add(rule);
	
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:if[@test='%s']/xsl:element[@name='%s']", 
				XPATH_TOP_LEVEL_ATTRIBUTE, CONDITION, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}

	@Test
	public void testConditionalConstantTlaMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.CONDITIONAL_CONSTANT, true);
		fm.getRules().add(rule);
	
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().asXml();
		final String xpath = String.format(
				"%s/xsl:if[@test='%s']/xsl:element[@name='%s']", 
				XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE, CONDITION, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}
}
