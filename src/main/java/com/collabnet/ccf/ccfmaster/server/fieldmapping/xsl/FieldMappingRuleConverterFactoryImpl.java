package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;
import com.collabnet.ccf.ccfmaster.util.Maybe;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

@Service
public class FieldMappingRuleConverterFactoryImpl implements FieldMappingRuleConverterFactory {
    static abstract class AbstractFieldMappingRuleConverter implements FieldMappingRuleConverter {
        protected final FieldMappingRule rule;

        protected AbstractFieldMappingRuleConverter(final FieldMappingRule rule) {
            this.rule = rule;
        }

        public static Element xslElement(Element parent, String elementName) {
            return parent.addElement(XSL_ELEMENT).addAttribute("name",
                    elementName);
        }

        public static Element xslElement(String elementName) {
            return DocumentHelper.createElement(XSL_ELEMENT).addAttribute(
                    "name", elementName);
        }

        public static Element xslIf(Element parent, String condition) {
            return parent.addElement(XSL_IF).addAttribute("test", condition);
        }

        public static Element xslIf(String condition) {
            return DocumentHelper.createElement(XSL_IF).addAttribute("test",
                    condition);
        }

        public static Element xslValueOf(Element parent, String select) {
            return parent.addElement(XSL_VALUE_OF).addAttribute("select",
                    select);
        }

        public static Element xslValueOf(String select) {
            return DocumentHelper.createElement(XSL_VALUE_OF).addAttribute(
                    "select", select);
        }

        public static Element xslVariable(String name, String value) {
            return DocumentHelper.createElement(XSL_VARIABLE)
                    .addAttribute("name", name).addText(value);
        }

        public static Element xslWhen(String test, String value) {
            return DocumentHelper.createElement(XSL_WHEN)
                    .addAttribute("test", test).addText(value);
        }
    }

    static class ConditionalConstantConverter extends DirectConstantConverter {
        public ConditionalConstantConverter(FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            Element ret = xslIf(rule.getCondition());
            ret.add(super.asElement());
            return ret;
        }

    }

    static class ConditionalFieldConverter extends DirectFieldConverter {
        public ConditionalFieldConverter(FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            Element ret = xslIf(rule.getCondition());
            ret.add(super.asElement());
            return ret;
        }

    }

    static class ConditionalValueMapConverter extends DirectValueMapConverter {
        public ConditionalValueMapConverter(FieldMappingRule rule,
                List<FieldMappingValueMap> valueMaps) {
            super(rule, valueMaps);
        }

        @Override
        public Element asElement() {
            Element ret = xslIf(rule.getCondition());
            ret.add(super.asElement());
            return ret;
        }
    }

    static class CustomXSLTDocumentConverter extends AbstractFieldMappingRuleConverter {

        protected CustomXSLTDocumentConverter(FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            Element mainXmlElem = null;
            try {
                Document mainXmlDom = DocumentHelper.parseText(rule
                        .getXmlContent());
                mainXmlElem = mainXmlDom.getRootElement();
            } catch (DocumentException e) {
                throw new CoreConfigurationException("unable to parse XML", e);
            }
            return mainXmlElem;
        }

    }

    static class CustomXsltSnippetConverter extends AbstractFieldMappingRuleConverter {

        protected CustomXsltSnippetConverter(FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            SAXReader reader = new SAXReader();
            Reader stringReader = new StringReader(
                    "<xsl:stylesheet version=\"1.0\" "
                            + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" "
                            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                            + "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
                            + "xmlns:vmf=\"http://www.altova.com/MapForce/UDF/vmf\" "
                            + "exclude-result-prefixes=\"vmf xs xsi xsl\">"
                            + rule.getXmlContent() + "</xsl:stylesheet>");
            try {
                Document document = reader.read(stringReader);
                Element result = (Element) document
                        .selectSingleNode("stylesheet/*[1]");
                return result;
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static class DirectConstantConverter extends AbstractFieldMappingRuleConverter {
        public DirectConstantConverter(final FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            return xslElement(rule.getTarget()).addText(rule.getXmlContent());
        }
    }

    static class DirectFieldConverter extends AbstractFieldMappingRuleConverter {
        public DirectFieldConverter(FieldMappingRule rule) {
            super(rule);
        }

        @Override
        public Element asElement() {
            Element ret = xslElement(rule.getTarget());
            ret.add(xslValueOf("$input"));
            return ret;
        }

    }

    static class DirectValueMapConverter extends AbstractFieldMappingRuleConverter {

        private final List<FieldMappingValueMap> valueMaps;

        public DirectValueMapConverter(final FieldMappingRule rule,
                List<FieldMappingValueMap> valueMaps) {
            super(rule);
            Assert.notNull(valueMaps);
            this.valueMaps = valueMaps;
        }

        @Override
        public Element asElement() {
            Element ret = xslElement(rule.getTarget());
            for (Element e : valueMap2Xsl()) {
                ret.add(e);
            }
            return ret;
        }

        Maybe<FieldMappingValueMap> getValueMapByName(String valueMapName) {
            for (FieldMappingValueMap vm : valueMaps) {
                if (valueMapName.equals(vm.getName()))
                    return Maybe.some(vm);
            }
            return Maybe.none();
        }

        List<Element> valueMap2Xsl() {
            FieldMappingValueMap vm = getValueMapByName(rule.getValueMapName())
                    .get();
            Builder<Element> builder = ImmutableList.builder();
            int i = 0;
            for (FieldMappingValueMapEntry entry : vm.getEntries()) {
                builder.add(xslVariable("V" + i, entry.getSource()));
                i++;
            }
            Element choose = DocumentHelper.createElement(XSL_CHOOSE);
            i = 0;
            for (FieldMappingValueMapEntry entry : vm.getEntries()) {
                choose.add(xslWhen("$input = $V" + i, entry.getTarget()));
                i++;
            }
            if (vm.isHasDefault()) {
                Element otherwise = choose.addElement(XSL_OTHERWISE);
                if (vm.getDefaultValue() != null) {
                    otherwise.addText(vm.getDefaultValue());
                } else {
                    otherwise.add(xslValueOf("$input"));
                }
            }
            builder.add(choose);
            return builder.build();
        }
    }

    static class SourceRepositoryMappingLayoutConverter extends AbstractFieldMappingRuleConverter {

        private String directory;

        protected SourceRepositoryMappingLayoutConverter(FieldMappingRule rule,
                String dir) {
            super(rule);
            this.directory = dir;
        }

        @Override
        public Element asElement() {
            Element preXmlElem = null;
            try {
                Document genericArtifact = DocumentHelper.parseText(rule
                        .getXmlContent());
                Document preXmlDom = new CCFXsltTransformer(directory)
                        .getGenericArtifactToRepositoryXSLTFile(genericArtifact);
                preXmlElem = preXmlDom.getRootElement();
            } catch (DocumentException e) {
                throw new CoreConfigurationException("unable to parse XML", e);
            } catch (TransformerException e) {
                throw new CoreConfigurationException(
                        "unable to transform given XML", e);
            }
            return preXmlElem;
        }

    }

    static class TargetRepositoryMappingLayoutConverter extends AbstractFieldMappingRuleConverter {

        private String directory;

        protected TargetRepositoryMappingLayoutConverter(FieldMappingRule rule,
                String dir) {
            super(rule);
            this.directory = dir;
        }

        @Override
        public Element asElement() {
            Element postXmlElem = null;
            try {
                Document genericArtifact = DocumentHelper.parseText(rule
                        .getXmlContent());
                Document postXmlDom = new CCFXsltTransformer(directory)
                        .getRepositoryToGenericArtifactXSLTFile(genericArtifact);
                postXmlElem = postXmlDom.getRootElement();
            } catch (DocumentException e) {
                throw new CoreConfigurationException("unable to parse XML", e);
            } catch (TransformerException e) {
                throw new CoreConfigurationException(
                        "unable to  transform given XML", e);
            }
            return postXmlElem;
        }

    }

    public AbstractFieldMappingRuleConverter get(FieldMappingRule rule) {
        return get(rule, new ArrayList<FieldMappingValueMap>());
    }

    public AbstractFieldMappingRuleConverter get(FieldMappingRule rule,
            List<FieldMappingValueMap> valueMaps) {
        return get(rule, valueMaps, StringUtils.EMPTY);
    }

    public AbstractFieldMappingRuleConverter get(FieldMappingRule rule,
            List<FieldMappingValueMap> valueMaps, String dir) {
        AbstractFieldMappingRuleConverter converter = null;
        switch (rule.getType()) {
            case DIRECT_FIELD:
                converter = new DirectFieldConverter(rule);
                break;
            case CONDITIONAL_FIELD:
                converter = new ConditionalFieldConverter(rule);
                break;
            case DIRECT_CONSTANT:
                converter = new DirectConstantConverter(rule);
                break;
            case CONDITIONAL_CONSTANT:
                converter = new ConditionalConstantConverter(rule);
                break;
            case DIRECT_VALUE_MAP:
                converter = new DirectValueMapConverter(rule, valueMaps);
                break;
            case CONDITIONAL_VALUE_MAP:
                converter = new ConditionalValueMapConverter(rule, valueMaps);
                break;
            case CUSTOM_XSLT_SNIPPET:
                converter = new CustomXsltSnippetConverter(rule);
                break;
            case SOURCE_REPOSITORY_LAYOUT:
                converter = new SourceRepositoryMappingLayoutConverter(rule,
                        dir);
                break;
            case TARGET_REPOSITORY_LAYOUT:
                converter = new TargetRepositoryMappingLayoutConverter(rule,
                        dir);
                break;
            case CUSTOM_XSLT_DOCUMENT:
                converter = new CustomXSLTDocumentConverter(rule);
                break;
            default:
                throw new IllegalArgumentException(rule.getType()
                        + " not supported.");
        }
        return converter;
    }

}
