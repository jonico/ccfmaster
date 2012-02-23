package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static com.collabnet.ccf.ccfmaster.util.Maybe.maybe;
import static com.collabnet.ccf.ccfmaster.util.Maybe.none;
import static com.collabnet.ccf.ccfmaster.util.Maybe.some;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingRuleConverter;
import com.collabnet.ccf.ccfmaster.util.Maybe;
import com.collabnet.ccf.ccfmaster.util.Transformer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;

public class ConversionResult {
	private static final Logger log = LoggerFactory
			.getLogger(ConversionResult.class);
	@Valid
	private CustomXsl customXsl = null;
	@Valid
	private MapForce mapForce = null;
	@Valid
	private MappingRules mappingRules = null;
	static final Transformer<FieldMappingRule, Element> xmlContent2Element = new Transformer<FieldMappingRule, Element>() {
		@Override
		public Element transform(FieldMappingRule t) {
			try {
				return DocumentHelper.parseText(t.getXmlContent())
						.getRootElement();
			} catch (DocumentException e) {
				throw new CoreConfigurationException("unable to parse XML", e);
			}
		}
	};

	public ConversionResult(CustomXsl customXsl) {
		this.customXsl = customXsl;
	}

	public ConversionResult(MapForce mapForce) {
		this.mapForce = mapForce;
	}

	public ConversionResult(MappingRules mappingRules) {
		this.mappingRules = mappingRules;
	}

	public Maybe<CustomXsl> customXsl() {
		return maybe(customXsl);
	}

	public Maybe<MapForce> mapForce() {
		return maybe(mapForce);
	}

	public Maybe<MappingRules> mappingRules() {
		return maybe(mappingRules);
	}

	static Maybe<FieldMappingRule> getRuleByType(List<FieldMappingRule> rules,
			FieldMappingRuleType type) {
		for (FieldMappingRule rule : rules) {
			if (rule.getType() == type)
				return some(rule);
		}
		return none();
	}

	static CoreConfigurationException noRuleOfType(FieldMappingRuleType type) {
		final String msg = String.format("No rule of type %s found.", type);
		return new CoreConfigurationException(msg);
	}

	public static class CustomXsl {
		private final Element xml;

		public CustomXsl(final Mapping<?> mapping) {
			Assert.isTrue(mapping.getKind() == FieldMappingKind.CUSTOM_XSLT,
					"bad mapping kind: " + mapping.getKind());
			this.xml = getRuleByType(mapping.getRules(),
					FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT).map(
					xmlContent2Element).getOrThrow(
					noRuleOfType(FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT));
		}

		@VisibleForTesting
		CustomXsl(final Element xml) {
			this.xml = xml;

		}

		@SafeXslt
		public Element getXml() {
			return xml;
		}

	}

	public static class MapForce {
		private final Element preXml;
		private final Element mainXml;
		private final Element postXml;

		public MapForce(Mapping<?> mapping) {
			Assert.isTrue(mapping.getKind() == FieldMappingKind.MAPFORCE,
					"bad mapping kind: " + mapping.getKind());
			final List<FieldMappingRule> rules = mapping.getRules();
			this.preXml = getRuleByType(rules,
					FieldMappingRuleType.MAPFORCE_PRE)
					.map(xmlContent2Element)
					.getOrThrow(noRuleOfType(FieldMappingRuleType.MAPFORCE_PRE));
			this.mainXml = getRuleByType(rules,
					FieldMappingRuleType.MAPFORCE_MAIN).map(xmlContent2Element)
					.getOrThrow(
							noRuleOfType(FieldMappingRuleType.MAPFORCE_MAIN));
			this.postXml = getRuleByType(rules,
					FieldMappingRuleType.MAPFORCE_POST).map(xmlContent2Element)
					.getOrThrow(
							noRuleOfType(FieldMappingRuleType.MAPFORCE_POST));
		}

		@SafeXslt
		public Element getPreXml() {
			return preXml;
		}

		@SafeXslt
		public Element getMainXml() {
			return mainXml;
		}

		@SafeXslt
		public Element getPostXml() {
			return postXml;
		}
	}

	public static class Source {
		String source = "";
		Boolean isTopLevelAttribute = true;
		private volatile int hashCode = 0;
		

		public Source(String source, boolean isTopLevelAttribute) {
			this.source = source;
			this.isTopLevelAttribute = isTopLevelAttribute;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if ((obj == null) || (obj.getClass() != this.getClass()))
				return false;
			Source test = (Source) obj;
			return source.equals(test.source) && isTopLevelAttribute.equals(test.isTopLevelAttribute);
		}
		
		public int hashCode() {
			 final int multiplier = 23;
			 if (hashCode == 0) {
				 int code = 133;
				 code = multiplier * code + (source == null ? 0 : source.hashCode());
				 code = multiplier * code + (isTopLevelAttribute == null ? 0 : isTopLevelAttribute.hashCode());
				 hashCode = code;
			 }
			 return hashCode;
		}
	}

	public static class MappingRules {
		static final String XPATH_ELEMENT = "/xsl:stylesheet";
		static final String XPATH_TOP_LEVEL_ATTRIBUTE = XPATH_ELEMENT;
		static final String XPATH_CONSTANT_ELEMENT = "//artifact";
		static final String XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE = "//artifact/topLevelAttributes";
		static final String MAPPING_RULE_TEMPLATE_XSL = "/mapping-rule-template.xsl";
		//FIXME: Autowired and instantiated?
		@Autowired
		FieldMappingRuleConverterFactory converterFactory = new FieldMappingRuleConverterFactoryImpl();
	
		private ArrayListMultimap<Source,FieldMappingRule> fields = ArrayListMultimap.create();
		private ArrayListMultimap<Source,FieldMappingRule> topLevelAttributes = ArrayListMultimap.create();
		
		private ArrayList<FieldMappingRule> constantFields = new ArrayList<FieldMappingRule>();
		private ArrayList<FieldMappingRule> constantTopLevelAttributes = new ArrayList<FieldMappingRule>();
		private Element preXml;
		private Element postXml;
		private Mapping<?> mapping;

		public MappingRules(final Mapping<?> mapping) {
			this.mapping = mapping;
			Assert.isTrue(mapping.getKind() == FieldMappingKind.MAPPING_RULES,
					"bad mapping kind: " + mapping.getKind());
			mappingRuleXsltHandler();
		}

		private void mappingRuleXsltHandler() {
			for (FieldMappingRule rule : mapping.getRules()) {
				FieldMappingRuleConverter converter;
				switch (rule.getType()) {
				case SOURCE_REPOSITORY_LAYOUT:
					converter = converterFactory.get(rule,
							mapping.getValueMaps(),
							getXsltDir((mapping.getMappingDirection())));
					this.preXml = converter.asElement();
					break;
				case TARGET_REPOSITORY_LAYOUT:
					converter = converterFactory.get(rule,
							mapping.getValueMaps(),
							getXsltDir((mapping.getMappingDirection())));
					this.postXml = converter.asElement();
					break;
				default:
					buildMainXsltAttribute(rule);
					break;
				}
			}
		}

		private void addRuleToMap(ArrayListMultimap<Source,FieldMappingRule> map, FieldMappingRule rule, Source source) {
			if (map.containsKey(source)) {
				map.get(source).add(rule);
			} else {
				map.put(source, rule);
			}
		}

		private void buildMainXsltAttribute(FieldMappingRule rule) {
			List<FieldMappingRuleType> constantTypes = Arrays.asList(
					FieldMappingRuleType.DIRECT_CONSTANT,
					FieldMappingRuleType.CUSTOM_XSLT_SNIPPET);

			Source source = new Source(rule.getSource(),
					rule.isSourceIsTopLevelAttribute());
			
			if (rule.isTargetIsTopLevelAttribute()) {
				if (constantTypes.contains(rule.getType())) 
					constantTopLevelAttributes.add(rule);
				else
					addRuleToMap(topLevelAttributes, rule, source);
			} else {
				if (constantTypes.contains(rule.getType())) 
					constantFields.add(rule);
				else
					addRuleToMap(fields, rule, source);
			}
		}

		public static String getXsltDir(Directions direction) {
			String dir = "";
			switch (direction) {
			case FORWARD:
				dir = String.format("%sxslt%sTF2QC", File.separator,
						File.separator);
				break;
			case REVERSE:
				dir = String.format("%sxslt%sQC2TF", File.separator,
						File.separator);
				break;
			}
			return dir;
		}

		// TODO: For now below line is commented out to make FieldMapping
		// instance to create using REST post and put request
		// Needs to uncomment the below line once FieldMappingRuleConvertor and
		// its related core implementation is done
		// @SafeXslt
		private Element createTemplateNode(Source source, Boolean isTargetIsTopLevelAttribute) {
			Element template = DocumentHelper
					.createElement(FieldMappingRuleConverter.XSL_TEMPLATE);
			if (source.isTopLevelAttribute){
				template.addAttribute("match", "topLevelAttributes/@" + source.source);
			} else {
				template.addAttribute("match", source.source);
			}
			if (isTargetIsTopLevelAttribute) {
				template.addAttribute("mode", "topLevelAttribute");
			} else{
				template.addAttribute("mode", "element");
			}
			Element variable = DocumentHelper
					.createElement(FieldMappingRuleConverter.XSL_VARIABLE);
			variable.addAttribute("name", "input");
			Element valueof = DocumentHelper
					.createElement(FieldMappingRuleConverter.XSL_VALUE_OF);
			valueof.addAttribute("select", ".");
			variable.add(valueof);
			template.add(variable);
			return template;
		}
		
	
		
		private Element addRuleToTemplateNode(Element template, FieldMappingRule rule, Source source){
			final Element e = getElementForRule(rule);
			Node n = (Node) e.clone();
			template.add(n.detach());
			return template;
		}

		private Element getElementForRule(FieldMappingRule rule) {
			return converterFactory.get(rule, mapping.getValueMaps()).asElement();
		}
		
		public Element getXml() {
			try {
				final SAXReader saxReader = new SAXReader();
				final InputStream in = getClass().getResourceAsStream(
						MAPPING_RULE_TEMPLATE_XSL);
				final Document template = saxReader.read(in);
				final Element result = template.getRootElement();

				final Element tla = (Element) result
						.selectSingleNode(XPATH_TOP_LEVEL_ATTRIBUTE);
				
				for (Source source : topLevelAttributes.keySet()) {
					Element templateNode = createTemplateNode(source, true);
					for (FieldMappingRule r : topLevelAttributes.get(source)) {
						templateNode = addRuleToTemplateNode(templateNode, r, source);
					}
					tla.add(templateNode.detach());
				}

				final Element fld = (Element) result
						.selectSingleNode(XPATH_ELEMENT);
				
				for (Source source : fields.keySet()) {
					Element templateNode = createTemplateNode(source, false);
					for (FieldMappingRule r : fields.get(source)) {
						templateNode = addRuleToTemplateNode(templateNode, r, source);
					}
					fld.add(templateNode.detach());
				}
				

				final Element constantTla = (Element) result
						.selectSingleNode(XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE);
				for (FieldMappingRule r : constantTopLevelAttributes) {
					final Element e = getElementForRule(r);
					Node n = (Node) e.clone();
					constantTla.add(n.detach());
				}
				

				final Element constantFld = (Element) result
						.selectSingleNode(XPATH_CONSTANT_ELEMENT);
				
				for (FieldMappingRule r : constantFields) {
					final Element e = getElementForRule(r);
					Node n = (Node) e.clone();
					constantFld.add(n.detach());
				}
				
				
				if (log.isDebugEnabled())
					log.debug(result.asXML());
				
				
				
				return result;
			} catch (DocumentException e) {
				throw new CoreConfigurationException(e);
			}
		}

		// @SafeXslt
		public Element getPreXml() {
			return preXml;
		}

		// @SafeXslt
		public Element getPostXml() {
			return postXml;
		}

	}
}
