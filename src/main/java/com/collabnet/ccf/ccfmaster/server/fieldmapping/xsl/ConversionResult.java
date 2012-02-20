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
import com.collabnet.ccf.ccfmaster.util.Maybe;
import com.collabnet.ccf.ccfmaster.util.Transformer;
import com.google.common.annotations.VisibleForTesting;

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
		private List<Element> fields = new ArrayList<Element>();
		private List<Element> topLevelAttributes = new ArrayList<Element>();
		private List<Element> constantFields = new ArrayList<Element>();
		private List<Element> constantTopLevelAttributes = new ArrayList<Element>();
		private Element preXml;
		private Element postXml;

		public MappingRules(final Mapping<?> mapping) {
			Assert.isTrue(mapping.getKind() == FieldMappingKind.MAPPING_RULES,"bad mapping kind: " + mapping.getKind());
			mappingRuleXsltHandler(mapping);
		}

		private void mappingRuleXsltHandler(final Mapping<?> mapping) {
			for (FieldMappingRule rule : mapping.getRules()) {
				FieldMappingRuleConverter converter;
				switch(rule.getType()){
					case SOURCE_REPOSITORY_LAYOUT:
						converter = converterFactory.get(rule, mapping.getValueMaps(),getXsltDir((mapping.getMappingDirection())));
						this.preXml = converter.asElement();
						break;
					case TARGET_REPOSITORY_LAYOUT:
						converter = converterFactory.get(rule, mapping.getValueMaps(),getXsltDir((mapping.getMappingDirection())));
						this.postXml = converter.asElement();
						break;
					default:
						converter = converterFactory.get(rule, mapping.getValueMaps());
						buildMainXsltAttribute( rule, converter);
						break;
				}
			}
		}

		private void buildMainXsltAttribute(FieldMappingRule rule,FieldMappingRuleConverter converter) {
			List<FieldMappingRuleType> constantTypes = Arrays.asList(FieldMappingRuleType.DIRECT_CONSTANT, FieldMappingRuleType.CONDITIONAL_CONSTANT);
			if (rule.isTargetIsTopLevelAttribute()) {
				final Element tla = converter.asTopLevelAttribute();
				if (constantTypes.contains(rule.getType()))
					this.constantTopLevelAttributes.add(tla);
				else
					this.topLevelAttributes.add(tla);
			} else {
				final Element elem = converter.asElement();
				if (constantTypes.contains(rule.getType()))
					this.constantFields.add(elem);
				else
					this.fields.add(elem);
			}
		}
		
		public static String getXsltDir(Directions direction){
			String dir = "";
			switch(direction){
				case FORWARD:
					dir= String.format("%sxslt%sTF2QC",File.separator,File.separator);
					break;
				case REVERSE:
					dir= String.format("%sxslt%sQC2TF",File.separator,File.separator);
					break;
			}
			return dir;
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
		
//		@SafeXslt
		public Element getPreXml() {
			return preXml;
		}
		
//		@SafeXslt
		public Element getPostXml() {
			return postXml;
		}
		
	}
}
