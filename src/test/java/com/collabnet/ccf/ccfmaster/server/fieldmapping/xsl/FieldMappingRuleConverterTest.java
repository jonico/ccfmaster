package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;

import static com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingRuleConverter.*;
import static org.junit.Assert.*;

public class FieldMappingRuleConverterTest {
    private static final String             VALUEMAPNAME      = "VALUEMAPNAME";

    private static Logger                   log               = LoggerFactory
                                                                      .getLogger(FieldMappingRuleConverterTest.class);

    private final String                    source            = "SOURCE";
    private final String                    target            = "TARGET_FIELD";
    private final String                    content           = "CONTENT";
    private final String                    condition         = "CONDITION";
    private final String                    defaultValue      = "DEFAULT";

    private final String                    valueMapSource    = "VM_SOURCE";
    private final String                    valueMapTarget    = "VM_TARGET";

    private final String                    validXSLTDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                                                      + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" exclude-result-prefixes=\"xs xsi xsl\">"
                                                                      + "</xsl:stylesheet>";

    FieldMappingRuleConverterFactoryImpl    converterFactory  = new FieldMappingRuleConverterFactoryImpl();

    private FieldMappingRule                rule;
    private FieldMappingValueMap            valueMap;
    private List<FieldMappingValueMapEntry> entries;

    @Before
    public void setUp() {
        rule = new FieldMappingRule();
        rule.setTarget(target);
        rule.setSource(source);
        rule.setXmlContent(content);
        valueMap = new FieldMappingValueMap();
        valueMap.setName(VALUEMAPNAME);
        FieldMappingValueMapEntry entry = new FieldMappingValueMapEntry();
        entry.setSource(valueMapSource);
        entry.setTarget(valueMapTarget);
        entries = new ArrayList<FieldMappingValueMapEntry>();
    }

    @Test
    public void testConditionalConstantConverter() {
        rule.setType(FieldMappingRuleType.CONDITIONAL_CONSTANT);
        rule.setCondition(condition);
        FieldMappingRuleConverter converter = converterFactory.get(rule);

        final Element result = extractIfResult(converter.asElement());
        assertEquals(XSL_ELEMENT, result.getQName());
        assertEquals(target, result.attribute("name").getValue());
        assertEquals(content, result.getText());
    }

    @Test
    public void testConditionalFieldConverter() {
        rule.setType(FieldMappingRuleType.CONDITIONAL_FIELD);
        rule.setCondition(condition);
        FieldMappingRuleConverter converter = converterFactory.get(rule);

        final Element result = extractIfResult(converter.asElement());
        assertEquals(XSL_ELEMENT, result.getQName());
        assertEquals(target, result.attribute("name").getValue());
        assertEquals("$input", result.element(XSL_VALUE_OF).attribute("select")
                .getValue());

        final Element tlaResult = extractIfResult(converter.asElement());
        log.debug(tlaResult.asXML());
        assertEquals(XSL_ELEMENT, tlaResult.getQName());
        assertEquals(target, tlaResult.attribute("name").getValue());
        // FIXME: correct return value? see comment in FMRCFI.CFC 
        // assertEquals("string(.)", tlaResult.element(XSL_VALUE_OF).attribute("select").getValue());
        assertEquals("$input",
                tlaResult.element(XSL_VALUE_OF).attribute("select").getValue());
    }

    @Test
    public void testCustomXsltDocumentConvertor() {
        rule.setType(FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT);
        rule.setXmlContent(validXSLTDocument);
        FieldMappingRuleConverter converter = converterFactory.get(rule);
        converter.asElement();
    }

    @Test
    public void testCustomXsltSnippetConvertor() {
        String customSnippet = "<xsl:element></xsl:element>";
        rule.setType(FieldMappingRuleType.CUSTOM_XSLT_SNIPPET);
        rule.setXmlContent(customSnippet);
        FieldMappingRuleConverter converter = converterFactory.get(rule);
        final Element result = converter.asElement();
        assertTrue(result.getNamespacePrefix().equals("xsl"));
    }

    @Test
    public void testDirectConstantConverter() {
        rule.setType(FieldMappingRuleType.DIRECT_CONSTANT);
        FieldMappingRuleConverter converter = converterFactory.get(rule);

        final Element result = converter.asElement();
        log.debug(result.asXML());
        assertEquals(XSL_ELEMENT, result.getQName());
        assertEquals(target, result.attribute("name").getValue());
        assertEquals(content, result.getText());

    }

    @Test
    public void testDirectFieldConverter() {
        rule.setType(FieldMappingRuleType.DIRECT_FIELD);
        FieldMappingRuleConverter converter = converterFactory.get(rule);

        final Element result = converter.asElement();
        log.debug(result.asXML());
        assertEquals(XSL_ELEMENT, result.getQName());
        assertEquals(target, result.attribute("name").getValue());
        assertEquals("$input", result.element(XSL_VALUE_OF).attribute("select")
                .getValue());

    }

    @Test
    public void testDirectValueMapConverter() {

        rule.setType(FieldMappingRuleType.DIRECT_VALUE_MAP);
        rule.setValueMapName(VALUEMAPNAME);
        valueMap.setHasDefault(false);
        valueMap.setEntries(entries);

        FieldMappingRuleConverter converter = converterFactory.get(rule,
                Arrays.asList(valueMap));

        Element result = converter.asElement();
        log.debug(result.asXML());
        assertEquals(XSL_ELEMENT, result.getQName());
        assertEquals(target, result.attribute("name").getValue());
        validateValueMapBasics(entries, result);

        valueMap.setHasDefault(false);
        valueMap.setDefaultValue(defaultValue);
        result = converter.asElement();
        hasEqualsFalseDefaultNonNull(converter.asElement());

        valueMap.setHasDefault(true);
        valueMap.setDefaultValue(defaultValue);
        hasEqualsTrueDefaultNonNull(converter.asElement());

        valueMap.setHasDefault(true);
        valueMap.setDefaultValue(null);
        hasDefaultTrueDefaultNull(converter.asElement());
    }

    @Test
    public void testSourceRepositoryLayoutConverter() {
        rule.setType(FieldMappingRuleType.SOURCE_REPOSITORY_LAYOUT);
        rule.setXmlContent(ConversionResultMappingRulesTest
                .genericArtifactXml());
        FieldMappingRuleConverter converter = converterFactory.get(rule,
                new ArrayList<FieldMappingValueMap>(),
                String.format("%sxslt%sTF2QC", File.separator, File.separator));

        final Element result = converter.asElement();
        final String xpath = "//actualHours/xslo:value-of";
        assertFalse(result.selectNodes(xpath).isEmpty());
    }

    @Test
    public void testTargetRepositoryLayoutConverter() {
        rule.setType(FieldMappingRuleType.TARGET_REPOSITORY_LAYOUT);
        rule.setXmlContent(ConversionResultMappingRulesTest
                .genericArtifactXml());
        FieldMappingRuleConverter converter = converterFactory.get(rule,
                new ArrayList<FieldMappingValueMap>(),
                String.format("%sxslt%sTF2QC", File.separator, File.separator));

        final Element result = converter.asElement();
        final String xpath = "//xslo:attribute[@name='fieldName']";
        assertFalse(result.selectNodes(xpath).isEmpty());
    }

    private Element extractIfResult(Element ifResult) {
        log.debug(ifResult.asXML());
        assertEquals(XSL_IF, ifResult.getQName());
        assertEquals(condition, ifResult.attribute("test").getValue());
        return ifResult.element(XSL_ELEMENT);
    }

    private void hasDefaultTrueDefaultNull(Element result) {
        log.debug(result.asXML());
        validateValueMapBasics(entries, result);
        Element otherwise = result.element(XSL_CHOOSE).element(XSL_OTHERWISE);
        assertEquals("$input",
                otherwise.element(XSL_VALUE_OF).attribute("select").getValue());
    }

    private void hasEqualsFalseDefaultNonNull(Element result) {
        log.debug(result.asXML());
        validateValueMapBasics(entries, result);
        assertNull(result.element(XSL_OTHERWISE));
    }

    private void hasEqualsTrueDefaultNonNull(Element result) {
        log.debug(result.asXML());
        validateValueMapBasics(entries, result);
        Element otherwise = result.element(XSL_CHOOSE).element(XSL_OTHERWISE);
        assertEquals(defaultValue, otherwise.getText());
    }

    @SuppressWarnings("unchecked")
    // dom4j doesn't use generics :(
    private void validateValueMapBasics(
            final List<FieldMappingValueMapEntry> entries, Element result) {
        List<Element> variables = result.elements(XSL_VARIABLE);
        assertEquals(entries.size(), variables.size());
        for (int i = 0; i < variables.size(); i++) {
            Element variable = variables.get(i);
            assertEquals("V" + i, variable.attribute("name").getValue());
            assertEquals(entries.get(i).getSource(), variable.getText());
        }

        Element choose = result.element(XSL_CHOOSE);
        assertNotNull("no choose clause", choose);

        List<Element> whens = choose.elements(XSL_WHEN);
        assertEquals(entries.size(), whens.size());
        for (int i = 0; i < whens.size(); i++) {
            Element when = whens.get(i);
            assertEquals("$input = $V" + i, when.attribute("test").getValue());
            assertEquals(entries.get(i).getTarget(), when.getText());
        }
    }
}
