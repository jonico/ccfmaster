package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.CustomXsl;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MapForce;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;

public abstract class AbstractPersister<T extends Mapping<?>> {
	public static final String FILENAME_RULES = "rules.xsl";
	public static final String FILENAME_MAPFORCE_PRE = "GenericArtifactFormatToMapForce.xsl";
	public static final String FILENAME_MAPFORCE_MAIN = "MapForceMain.xsl";
	public static final String FILENAME_MAPFORCE_POST = "MapForceToGenericArtifactFormat.xsl";
	public static final String FILENAME_MAPFORCE_MFD = "MapForceMFD.xsl";
	public static final String FILENAME_CUSTOM_XSL = "custom.xsl";

	final ConversionResult result;
	final File baseDir;
	
	public AbstractPersister(final File baseDir, final ConversionResult conversionResult) {
		Assert.notNull(baseDir, "baseDir must not be null");
		Assert.notNull(conversionResult, "conversionResult must not be null");
		Assert.isTrue(baseDir.exists(), "baseDir must exist");
		Assert.isTrue(baseDir.isDirectory(), "baseDir must be a directory");
		this.baseDir = baseDir;
		this.result = conversionResult;
	}

	protected void saveTo(File directory) throws IOException {
		Assert.isTrue(directory.isDirectory(), "not a directory: " + directory);
		Assert.isTrue(directory.exists(), directory + "doesn't exist");
		// use Maybe as an Iterable to simulate
		// pattern matching and case classes
		for (CustomXsl customXsl : result.customXsl()) {
			File outFile = new File(directory, FILENAME_CUSTOM_XSL);
			writeXml(customXsl.getXml(), outFile);
			return;
		}
		for (MapForce mapForce : result.mapForce()) {
			writeXml(mapForce.getPreXml(),
					new File(directory, FILENAME_MAPFORCE_PRE));
			writeXml(mapForce.getMainXml(),
					new File(directory, FILENAME_MAPFORCE_MAIN));
			writeXml(mapForce.getPostXml(),
					new File(directory, FILENAME_MAPFORCE_POST));
			return;
		}
		for (MappingRules mappingRules : result.mappingRules()) {
			File outFile = new File(directory, FILENAME_RULES);
			writeXml(mappingRules.asXml(), outFile);
			return;
		}
		throw new UnsupportedOperationException();
	}

	public void doSave(T cfg) {
		try {
			File directory = cfg.getStorageDirectory(baseDir);
			if (!directory.exists() && !directory.mkdirs()) {
				throw new CoreConfigurationException("error creating directory " + directory);
			}
			saveTo(directory);
		} catch (IOException e) {
			throw new CoreConfigurationException(e);
		}
	}

	static void writeXml(Element xml, File file) throws IOException{
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileWriter(file));
			writer.write(xml);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	
}