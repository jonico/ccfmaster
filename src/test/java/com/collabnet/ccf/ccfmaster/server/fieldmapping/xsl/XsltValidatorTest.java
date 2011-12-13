package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URL;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

public class XsltValidatorTest {
	@Test
	public void functionCallsExistInSampleResources() throws Exception {
		for (String resourceName : ImmutableList.of("MapForceToGenericArtifactFormat.xsl", "custom.xsl")) {
			URL xsltLocation = Resources.getResource(XsltValidatorTest.class, resourceName);
			final String xslt = Resources.toString(xsltLocation, Charsets.UTF_8);
			
			final Element rootElement = DocumentHelper.parseText(xslt).getRootElement();
			final String functionName = "encodeHTMLToEntityReferences";
			assertFalse("no function calls in " + resourceName, XsltValidator.findFunctionCalls(rootElement, functionName).isEmpty());
		}
	}
	
	@Test
	public void functionCallsFound() throws DocumentException {
		final ImmutableList<String> data = ImmutableList.of(
				"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' select='stringutil:foo(string(.))'/>",
				"<bar><xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' select='stringutil:foo(string(.))'/></bar>"
				/*"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:other='xalan://com.collabnet.ccf.core.utils.StringUtils' select='other:foo(string(.))'/>"*/);
		for (String xml : data) {
			final Element rootElement = DocumentHelper.parseText(xml).getRootElement();
			assertEquals(1, XsltValidator.findFunctionCalls(rootElement, "foo").size());
		}
	}

	@Test
	public void noFunctionCallsFound() throws DocumentException {
		final ImmutableList<String> data = ImmutableList.of(
				"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' select='stringutil:foo(.)'/>",
				"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' select='foo(string(.))'/>",
				"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' something='stringutil:foo(string(.))'/>",
				"<value-of select='stringutil:foo'/>"
				/*"<xsl:value-of xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:stringutil='urn:whatever' select='stringutil:foo'/>"*/);
		for (String xml : data) {
			final Element rootElement = DocumentHelper.parseText(xml).getRootElement();
			assertEquals(0, XsltValidator.findFunctionCalls(rootElement, "foo").size());
		}
	}
}
