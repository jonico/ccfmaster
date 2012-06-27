package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.springframework.util.Assert;

import com.collabnet.ccf.ccfmaster.server.core.CoreConfigurationException;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.CustomXsl;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MapForce;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult.MappingRules;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public abstract class AbstractPersister<T extends Mapping<?>> {
	public static final String FILENAME_RULES = "rules.xsl";
	public static final String FILENAME_PREXML_RULES = "preprocessingrules.xsl";
	public static final String FILENAME_POSTXML_RULES = "postprocessingrules.xsl";
	public static final String FILENAME_MAPFORCE_PRE = "GenericArtifactFormatToMapForce.xsl";
	public static final String FILENAME_MAPFORCE_MAIN = "MapForceMain.xsl";
	public static final String FILENAME_MAPFORCE_POST = "MapForceToGenericArtifactFormat.xsl";
	public static final String FILENAME_MAPFORCE_MFD = "MapForceMFD.xsl";
	public static final String FILENAME_CUSTOM_XSL = "custom.xsl";

	protected final ConversionResult result;
	protected final File baseDir;
	final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

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
			writeXml(mappingRules.getXml(), new File(directory, FILENAME_RULES));
			writeXml(mappingRules.getPreXml(), new File(directory, FILENAME_PREXML_RULES));
			writeXml(mappingRules.getPostXml(), new File(directory, FILENAME_POSTXML_RULES));
			return;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * validates and saves <code>cfg</code>.
	 * @param cfg
	 * @throws CoreConfigurationException if the mapping is invalid or if an IO error occurs while saving.
	 */
	public final void doSave(T cfg) {
		validate(result);
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

	@VisibleForTesting
	static void validate(ConversionResult result) throws CoreConfigurationException {
		Set<ConstraintViolation<ConversionResult>> errors = validator.validate(result);
		if (!errors.isEmpty()) {
			throw new CoreConfigurationException(String.format("invalid XSLT for field mapping: %s", buildErrorMessage(errors)));
		}
	}

	static void writeXml(Element xml, File file) throws IOException {
		if (xml == null)
			return;
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
			writer.write(xml);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	private static String buildErrorMessage(Set<ConstraintViolation<ConversionResult>> errors) {
		final Function<ConstraintViolation<?>,String> constraintViolationMessage = new Function<ConstraintViolation<?>,String>() {

			@Override
			public String apply(ConstraintViolation<?> input) {
				return input.getMessage();
			}
		};
		Iterable<String> messages = Iterables.transform(errors, constraintViolationMessage);
		String errMsg = Joiner.on("; ").join(messages);
		return errMsg;
	}

	
}