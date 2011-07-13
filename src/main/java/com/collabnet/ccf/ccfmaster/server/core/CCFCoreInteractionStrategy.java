package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public abstract class CCFCoreInteractionStrategy<T> {
	public abstract void create(T context);

	public abstract void delete(T context);

	public abstract void update(T context);

	public void setCcfHome(String ccfHome) {
		this.ccfHome = ccfHome;
	}

	public String getCcfHome() {
		return ccfHome;
	}

	protected void writeProperties(T context, Properties properties) {
		for (String key : propertyMap.keySet()) {
			String expressionValue = propertyMap.get(key);
			Expression exp = parser.parseExpression(expressionValue);
			StandardEvaluationContext evalContext = new StandardEvaluationContext();
			evalContext.setRootObject(context);
			String value = exp.getValue(evalContext).toString();
			properties.put(key, value);
		}
	}

	private Map<String, String> propertyMap = new HashMap<String, String>();
	private SpelExpressionParser parser = new SpelExpressionParser();

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	protected void updateProperties(T context, File propertyFile) {
		FileOutputStream fos;
		try {
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream(propertyFile);
			try {
				properties.load(fis);
				writeProperties(context, properties);
			} finally {
				fis.close();
			}

			fos = new FileOutputStream(propertyFile);
			try {
				properties.store(fos, "Updated properties");
			} finally {
				fos.close();
			}
		} catch (IOException e) {
			throw new CoreConfigurationException("Could not update properties for "
					+ context.toString() + ": " + e.getMessage(), e);
		}
	}

	protected void createProperties(T context, File propertyFile) {
		try {
			FileOutputStream fos = new FileOutputStream(propertyFile);
			Properties landscapeProperties = new Properties();
	
			writeProperties(context, landscapeProperties);
			try {
				landscapeProperties.store(fos,
						"Initially created properties");
			} finally {
				fos.close();
			}
		} catch (IOException e) {
			throw new CoreConfigurationException("Could not write properties for "
					+ context.toString() + ": " + e.getMessage(), e);
		}
	}

	private String ccfHome;

}
