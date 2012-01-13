package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import static com.collabnet.ccf.ccfmaster.util.Maybe.maybe;
import static com.collabnet.ccf.ccfmaster.util.Maybe.none;
import static com.collabnet.ccf.ccfmaster.util.Maybe.some;

import java.io.InputStream;
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
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.util.Maybe;
import com.collabnet.ccf.ccfmaster.util.Transformer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class ConversionResult {
	private static final Logger log = LoggerFactory.getLogger(ConversionResult.class);
	@Valid private CustomXsl customXsl = null;
	@Valid private MapForce mapForce = null;
	@Valid private MappingRules mappingRules = null;
	static final Transformer<FieldMappingRule, Element> xmlContent2Element = new Transformer<FieldMappingRule, Element>() {
		@Override public Element transform(FieldMappingRule t) {
			try {
				return DocumentHelper.parseText(t.getXmlContent()).getRootElement();
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
	public Maybe<MappingRules> mappingRules(){
		return maybe(mappingRules);
	}
	
	static Maybe<FieldMappingRule> getRuleByType(List<FieldMappingRule> rules, FieldMappingRuleType type) {
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
			this.xml = getRuleByType(mapping.getRules(), FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT)
				.map(xmlContent2Element)
				.getOrThrow(noRuleOfType(FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT));
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
			Assert.isTrue(
					mapping.getKind() == FieldMappingKind.MAPFORCE,
					"bad mapping kind: " + mapping.getKind());
			final List<FieldMappingRule> rules = mapping.getRules();
			this.preXml = getRuleByType(rules, FieldMappingRuleType.MAPFORCE_PRE)
					.map(xmlContent2Element)
					.getOrThrow(noRuleOfType(FieldMappingRuleType.MAPFORCE_PRE));
			this.mainXml = getRuleByType(rules, FieldMappingRuleType.MAPFORCE_MAIN)
					.map(xmlContent2Element)
					.getOrThrow(noRuleOfType(FieldMappingRuleType.MAPFORCE_MAIN));
			this.postXml = getRuleByType(rules, FieldMappingRuleType.MAPFORCE_POST)
					.map(xmlContent2Element)
					.getOrThrow(noRuleOfType(FieldMappingRuleType.MAPFORCE_POST));
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
	public static class MappingRules {
		static final String XPATH_ELEMENT = "/xsl:stylesheet";
		static final String XPATH_TOP_LEVEL_ATTRIBUTE = XPATH_ELEMENT;
		static final String XPATH_CONSTANT_ELEMENT = "//artifact";
		static final String XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE = "//artifact/topLevelAttributes";
		static final String MAPPING_RULE_TEMPLATE_XSL = "/mapping-rule-template.xsl";
		@Autowired
		FieldMappingRuleConverterFactory converterFactory = new FieldMappingRuleConverterFactoryImpl();
		final List<Element> fields;
		final List<Element> topLevelAttributes;
		private List<Element> constantFields;
		private List<Element> constantTopLevelAttributes;

		public MappingRules(final Mapping<?> mapping) {
			Assert.isTrue(
					mapping.getKind() == FieldMappingKind.MAPPING_RULES,
					"bad mapping kind: " + mapping.getKind());
			List<FieldMappingRuleType> constantTypes = Arrays.asList(FieldMappingRuleType.DIRECT_CONSTANT, FieldMappingRuleType.CONDITIONAL_CONSTANT);
			Builder<Element> fieldXml = ImmutableList.builder();
			Builder<Element> tlaXml = ImmutableList.builder();
			Builder<Element> constantFieldXml = ImmutableList.builder();
			Builder<Element> constantTlaXml = ImmutableList.builder();
			for (FieldMappingRule rule : mapping.getRules()) {
				FieldMappingRuleConverter converter = converterFactory.get(rule, mapping.getValueMaps());
				if (rule.isTargetIsTopLevelAttribute()) {
					final Element tla = converter.asTopLevelAttribute();
					if (constantTypes.contains(rule.getType()))
						constantTlaXml.add(tla);
					else
						tlaXml.add(tla);
				} else {
					final Element elem = converter.asElement();
					if (constantTypes.contains(rule.getType()))
						constantFieldXml.add(elem);
					else
						fieldXml.add(elem);
				}
			}
			fields = fieldXml.build();
			constantFields = constantFieldXml.build();
			topLevelAttributes = tlaXml.build();
			constantTopLevelAttributes = constantTlaXml.build();
		}

		//TODO: For now below line is commented out to make FieldMapping instance to create using REST post and put request
		//Needs to uncomment the below line once FieldMappingRuleConvertor and its related core implementation is done
		//@SafeXslt
		public Element getXml() {
			try {
				final SAXReader saxReader = new SAXReader();
				final InputStream in = getClass().getResourceAsStream(MAPPING_RULE_TEMPLATE_XSL);
				final Document template = saxReader.read(in);
				final Element result = template.getRootElement();

				final Element tla = (Element) result.selectSingleNode(XPATH_TOP_LEVEL_ATTRIBUTE);
				for (Element e : topLevelAttributes) {
					Node n = (Node)e.clone();
					tla.add(n.detach());
				}

				final Element fld = (Element) result.selectSingleNode(XPATH_ELEMENT);
				for (Element e : fields) {
					Node n = (Node)e.clone();
					fld.add(n.detach());
				}
				
				final Element constantTla = (Element) result.selectSingleNode(XPATH_CONSTANT_TOP_LEVEL_ATTRIBUTE);
				for (Element e : constantTopLevelAttributes) {
					Node n = (Node)e.clone();
					constantTla.add(n.detach());
				}

				final Element constantFld = (Element) result.selectSingleNode(XPATH_CONSTANT_ELEMENT);
				for (Element e : constantFields) {
					Node n = (Node)e.clone();
					constantFld.add(n.detach());
				}
				if (log.isDebugEnabled())
					log.debug(result.asXML());
				return result;
			} catch (DocumentException e) {
				throw new CoreConfigurationException(e);
			}
		}
		
	}
}
