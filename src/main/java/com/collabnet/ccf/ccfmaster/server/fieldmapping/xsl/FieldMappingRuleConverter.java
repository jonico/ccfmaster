package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public interface FieldMappingRuleConverter {
	public static final Namespace XSL_NS = Namespace.get("xsl", "http://www.w3.org/1999/XSL/Transform");
	public static final QName XSL_ELEMENT = new QName("element", XSL_NS);
	public static final QName XSL_ATTRIBUTE = new QName("attribute", XSL_NS);
	public static final QName XSL_VALUE_OF = new QName("value-of", XSL_NS);
	public static final QName XSL_IF = new QName("if", XSL_NS);
	public static final QName XSL_CHOOSE = new QName("choose", XSL_NS);
	public static final QName XSL_WHEN = new QName("when", XSL_NS);
	public static final QName XSL_OTHERWISE = new QName("otherwise", XSL_NS);
	public static final QName XSL_VARIABLE = new QName("variable", XSL_NS);
	
	public Element asElement();
	public Element asTopLevelAttribute();
}