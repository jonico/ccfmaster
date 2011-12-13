package com.collabnet.ccf.ccfmaster.web.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlWebHelper {

	public static String xmlToString(Node node) throws TransformerException {
        Source source = new DOMSource(node);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        return stringWriter.getBuffer().toString();
	}
	 
	public static Document createDocument(String xmlString) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource inStream = new InputSource();
		inStream.setCharacterStream(new StringReader(xmlString));
		Document doc = builder.parse(inStream);
		return doc; 
	}
	

	public static <T> void createJAXBExport(HttpServletResponse response,
			String defaultFileName, T objectToMarshall, Class<T> cls) throws IOException, JAXBException {
		response.setHeader("Content-Disposition", "attachment; filename=\""	+ defaultFileName + "\"");
		response.setContentType("text/xml; charset=utf-8");
		OutputStream out = response.getOutputStream();
		JAXBContext xmlContext = JAXBContext.newInstance(cls);
		Marshaller marshaller = xmlContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(objectToMarshall, out);
		out.flush();
		out.close();
	}
	
}
