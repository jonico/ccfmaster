package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import javax.xml.transform.TransformerException;

import org.dom4j.Document;

public interface XsltTransformer {

	public Document getRepositorySpecificLayout(Document ga)throws TransformerException;

	public Document getGenericArtifactToRepositoryXSLTFile(Document ga)throws TransformerException;

	public Document getRepositoryToGenericArtifactXSLTFile(Document ga)	throws TransformerException;
}
