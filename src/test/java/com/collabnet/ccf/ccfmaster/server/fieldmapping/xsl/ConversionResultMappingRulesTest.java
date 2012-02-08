package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules.XPATH_CONSTANT_ELEMENT;
import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules.XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE;
import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules.XPATH_ELEMENT;
import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules.XPATH_TOP_LEVEL_ATTRIBUTE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

@ContextConfiguration
public class ConversionResultMappingRulesTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	private static final String RULENAME = "RULENAME";

	private static final String CONDITION = "CONDITION";

	private static final String TARGET = "TARGET";

	private static final String SOURCE = "SOURCE";

	private final ConversionResultFactory resultFactory = new ConversionResultFactory();
	@Autowired FieldMappingDataOnDemand dod;
	
	@Test
	public void testEmptyMappingRules() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final MappingRules mappingRules = res.mappingRules().get();
		final Element xsl = mappingRules.getXml();
		assertTrue("weird, I found a rule.", xsl.selectNodes("//xsl:element[@name='TARGET']").isEmpty());
	}
	
	@Test
	public void testDirectFieldMappingRule() {
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.DIRECT_FIELD, false);
		fm.getRules().add(rule);

		ConversionResult res = resultFactory.get(fm);
		assertTrue(res.mappingRules().isSome());
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
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
		final Element xsl = res.mappingRules().get().getXml();
		final String xpath = String.format(
				"%s/xsl:if[@test='%s']/xsl:element[@name='%s']", 
				XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE, CONDITION, TARGET);
		assertFalse(
				"couldn't find rule in result",
				xsl.selectNodes(xpath).isEmpty());
	}
	
	@Test
	public void testSourceRepositoryMappingRule() {
		String xmlContent = genericArtifactXml();
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.SOURCE_REPOSITORY_LAYOUT, true);
		rule.setXmlContent(xmlContent);
		fm.getRules().add(rule);
		ConversionResult res = resultFactory.get(fm);
		final Element prexml = res.mappingRules().get().getPreXml();
		final String xpath = "//actualHours/xslo:value-of";
		assertTrue(prexml !=null);
		assertFalse(prexml.selectNodes(xpath).isEmpty());
	}
	
	@Test
	public void testTargetRepositoryMappingRule(){
		String xmlContent = genericArtifactXml();
		final FieldMapping fm = getMappingRulesFieldMapping();
		FieldMappingRule rule = getRule(FieldMappingRuleType.TARGET_REPOSITORY_LAYOUT, true);
		rule.setXmlContent(xmlContent);
		fm.getRules().add(rule);
		ConversionResult res = resultFactory.get(fm);
		final Element postxml = res.mappingRules().get().getPostXml();
		final String xpath = "//xslo:attribute[@name='fieldName']";
		assertTrue(postxml !=null);
		assertFalse(postxml.selectNodes(xpath).isEmpty());
	}
	
	public static String genericArtifactXml(){
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<artifact xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xmlns=\"http://ccf.open.collab.net/GenericArtifactV1.0\"");
		sb.append(" xsi:schemaLocation=\"http://ccf.open.collab.net/GenericArtifactV1.0 http://ccf.open.collab.net/files/documents/177/1972/genericartifactschema.xsd\">");
		sb.append("<field fieldAction=\"replace\" fieldName=\"actualHours\" fieldType=\"mandatoryField\" fieldValueHasChanged=\"true\" minOccurs=\"0\" maxOccurs=\"1\" ");
		sb.append("nullValueSupported=\"false\" alternativeFieldName=\"actualHours\" fieldValueType=\"Integer\" fieldValueIsNull=\"false\">actualHours(mandatoryField/ INTEGER)");
		sb.append("</field></artifact>");
		return sb.toString();
	}
}
