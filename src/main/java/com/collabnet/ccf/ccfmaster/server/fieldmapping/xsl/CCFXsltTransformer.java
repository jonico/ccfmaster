package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;

public class CCFXsltTransformer {

	public final static String REPOSITORY_TO_GENERIC_ARTIFACT_XSLT_FILE = "Repository2GenericArtifact.xsl"; //$NON-NLS-1$
	public final static String GENERIC_ARTIFACT_TO_REPOSITORY_XSLT_FILE = "GenericArtifact2Repository.xsl"; //$NON-NLS-1$

	private Transformer genericArtifactToRepositoryTransformer = null;
	private Transformer repositoryToGenericArtifactTransformer = null;

	public CCFXsltTransformer(String dir) {
		setGenericArtifactToRepositoryTransformer(dir);
		setRepositoryToGenericArtifactTransformer(dir);
	}

	public Document getGenericArtifactToRepositoryXSLTFile(Document ga)	throws TransformerException {
		return transform(genericArtifactToRepositoryTransformer, ga);
	}

	public Document getRepositoryToGenericArtifactXSLTFile(Document ga) throws TransformerException {
		return transform(repositoryToGenericArtifactTransformer, ga);
	}	
	
	private void setGenericArtifactToRepositoryTransformer(String dir) {
		String filePath = String.format("%s%s%s",dir,File.separator,GENERIC_ARTIFACT_TO_REPOSITORY_XSLT_FILE);
		this.genericArtifactToRepositoryTransformer = processXslResource(new ClassPathResource(filePath));
	}


	private void setRepositoryToGenericArtifactTransformer(String dir) {
		String filePath = String.format("%s%s%s",dir,File.separator,REPOSITORY_TO_GENERIC_ARTIFACT_XSLT_FILE);
		this.repositoryToGenericArtifactTransformer = processXslResource(new ClassPathResource(filePath));
	}
	
	private Document transform(Transformer transformer , Document d) throws TransformerException {
		DocumentSource source = new DocumentSource(d);
		DocumentResult result = new DocumentResult();
		// TODO: Allow the user to specify stylesheet parameters?
		if (transformer != null)
			transformer.transform(source, result);
		return result.getDocument();
	}

	private Transformer processXslResource(Resource processingXslFile) {
		Transformer transform = null;
		try {
			if (processingXslFile.exists()) {
				StreamSource streamSource = new StreamSource(processingXslFile.getFile());
				TransformerFactory factory = TransformerFactory.newInstance();
				transform = factory.newTransformer(streamSource);
			}
		} catch (TransformerConfigurationException e) {
			throw new CoreConfigurationException("Transformation failed", e);
		} catch (IOException e) {
			throw new CoreConfigurationException("Transformation failed", e);
		}
		return transform;
	}

}
