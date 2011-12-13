package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.IOException;
import java.net.URL;

import javax.xml.transform.TransformerConfigurationException;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Ignore;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.CustomXsl;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import static org.junit.Assert.*;

public class AbstractPersisterTest {

	@Ignore("this test is slow and only shows bad Xalan behaviour which we can't do anything about.")
	@Test(expected=StackOverflowError.class)
	public void xalanCrashesInsteadOfThrowingException() throws DocumentException, TransformerConfigurationException {
		final String xslt = "<valid-xml-but-not-xslt xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
		"	<xsl:variable select=\"document(\" />" +
		"</valid-xml-but-not-xslt>";
		final ConversionResult conversionResult = str2ConversionResult(xslt);
		AbstractPersister.validate(conversionResult);
	}

	@Test
	public void xsltCanCallWhiteListedMethods() throws DocumentException, IOException, TransformerConfigurationException {
		for (String resourceName : ImmutableList.of("MapForceToGenericArtifactFormat.xsl", "custom.xsl"))
			validateResource(resourceName);
	}
	
	@Ignore("methods are only rejected during XSLT runtime")
	@Test
	public void callingNonWhiteListedMethodsFails() throws IOException, DocumentException {
		for (String resourceName : ImmutableList.of("non-whitelisted-method.xsl")) {
			try {
				validateResource(resourceName);
				fail("no exception while validating " + resourceName);
			} catch(CoreConfigurationException expected) {}
		}
		
	}

	@Test(expected=CoreConfigurationException.class)
	public void nonXsltXmlThrows() throws DocumentException, IOException, TransformerConfigurationException {
		final String resourceName = "bad_xslt.txt";
		validateResource(resourceName);
	}

	private ConversionResult str2ConversionResult(final String xslt) throws DocumentException {
		final Element rootElement = DocumentHelper.parseText(xslt).getRootElement();
		final CustomXsl customXsl = new CustomXsl(rootElement);
		final ConversionResult conversionResult = new ConversionResult(customXsl);
		return conversionResult;
	}

	private void validateResource(final String fileName) throws IOException, CoreConfigurationException, DocumentException {
		URL xsltLocation = Resources.getResource(AbstractPersisterTest.class, fileName);
		final String xslt = Resources.toString(xsltLocation, Charsets.UTF_8);
		AbstractPersister.validate(str2ConversionResult(xslt));
	}


}
