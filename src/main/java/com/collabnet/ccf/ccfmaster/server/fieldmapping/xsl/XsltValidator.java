package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.XMLConstants;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.DocumentSource;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.SimpleVariableContext;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class XsltValidator implements ConstraintValidator<SafeXslt, Element> {

	private Collection<String> allowedFunctions = ImmutableSet.of();
	
	@Override
	public void initialize(SafeXslt constraintAnnotation) {
		allowedFunctions = ImmutableSet.copyOf(constraintAnnotation.allowedFunctionNames());
	}

	@Override
	public boolean isValid(Element xslt, ConstraintValidatorContext context) {
		
		boolean result = false;
		
		
		if (xslt == null) {
			return false;
		}
		
		// we operate on a copy, so we don't change the original
		xslt = (Element) xslt.clone();
		for (String functionName : allowedFunctions) {
			List<Element> nodes = findFunctionCalls(xslt, functionName);
			for (Element e: nodes) {
				e.addAttribute("select", ".");
			}
		}
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			final ValidatingErrorListener listener = new ValidatingErrorListener(allowedFunctions, context);
			factory.setErrorListener(listener);

			Document doc = DocumentFactory.getInstance().createDocument(xslt);
			Source source = new DocumentSource(doc);
			factory.newTransformer(source);
			result = !listener.getErrorOccurred();
			/*
			Transformer transformer = factory.newTransformer(source);
			
			if (!listener.getErrorOccurred()) {
				final ValidatingErrorListener listener2 = new ValidatingErrorListener(allowedFunctions, context);
				transformer.setErrorListener(listener2);
				StreamSource ss = new StreamSource(new StringReader("<foo xmlns:ccf='http://ccf.open.collab.net/GenericArtifactV1.0'><ccf:field fieldName='description'>asdf</ccf:field><ccf:field fieldName='title'>asdf</ccf:field></foo>"));
				transformer.transform(ss, new XMLResult());
				result = !listener2.getErrorOccurred();
			}
			*/
		} catch (Exception e) {
			context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
		}
		
		return result;
	}

	static List<Element> findFunctionCalls(Element xslt, final String functionName) {
		XPath xpath = buildXpath(xslt, functionName);
		@SuppressWarnings("unchecked") // jaxen doesn't do generics
		List<Element> nodes = xpath.selectNodes(xslt);
		return nodes;
	}

	private static XPath buildXpath(Element xslt, final String functionName) {
		XPath xpath = xslt.createXPath(String.format("//xsl:value-of[starts-with(@select, 'stringutil:%s(string(.))')]", functionName));
		SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
		namespaceContext.addNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");
		// irrelevant because the attribute value om the XPath above is a string, not a QName.
		// If it weren't we'd need to handle these two classes/namespaces:
		// namespaceContext.addNamespace("stringutil", "xalan://com.collabnet.ccf.core.utils.GATransformerUtil");
		// namespaceContext.addNamespace("stringutil", "xalan://com.collabnet.ccf.core.utils.StringUtils");
		xpath.setNamespaceContext(namespaceContext);
		SimpleVariableContext vc = new SimpleVariableContext();
		vc.setVariableValue("methodName", functionName);
		xpath.setVariableContext(vc);
		return xpath;
	}

	private static final class ValidatingErrorListener implements ErrorListener {
	
		private final ConstraintValidatorContext context;
		private final Iterable<String> allowedErrorMessages;
		private boolean errorOccurred = false;
	
		public ValidatingErrorListener(Iterable<String> allowedFunctions, ConstraintValidatorContext context) {
			this.context = context;
			this.allowedErrorMessages = Iterables.transform(allowedFunctions, messageForFunctionName);
		}
	
		@Override
		public void warning(TransformerException exception) throws TransformerException {
			if (!expectedError(exception))
				handle(exception);
		}
	
		@Override
		public void error(TransformerException exception) throws TransformerException {
			if (!expectedError(exception))
				handle(exception);
		}

		@Override
		public void fatalError(TransformerException exception) throws TransformerException {
			if (!expectedError(exception))
				handle(exception);
		}
		
		public boolean getErrorOccurred() {
			return errorOccurred;
		}
	
		private boolean expectedError(TransformerException exception) {
			return Iterables.contains(allowedErrorMessages, exception.getMessage());
		}
		
		private void handle(TransformerException exception) {
			context.disableDefaultConstraintViolation();
			final String msg = exception.getMessageAndLocation();
			context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
			errorOccurred = true;
		}
	
		private static Function<String, String> messageForFunctionName = new Function<String, String>() {
			@Override
			public String apply(String input) {
				return String.format("The first argument to the non-static Java function '%s' is not a valid object reference.", input);
			}
		};
	
	}

}
