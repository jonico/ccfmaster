package com.collabnet.ccf.ccfmaster.config;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperties;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.core.utils.SerializationUtil;

@Component
public class CoreConfigLoader {
	
	final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Autowired
	private CCFRuntimePropertyHolder runtimePropertyHolder;

	public List<DirectionConfig> populateDefaultCoreConfig(Direction direction) throws JAXBException, IOException {
		return populateDefaultCoreConfig(loadCCFCoreProperties(),direction);
	}

	public List<DirectionConfig> populateDefaultCoreConfig(CCFCoreProperties properties, Direction direction) {
		List<DirectionConfig> directionConfig = new ArrayList<DirectionConfig>();
		SystemKind systemkind = direction.getLandscape().getParticipant().getSystemKind();
		if (properties != null) {
			for (CCFCoreProperty prop : properties.getCcfCoreProperties()) {
				if (systemkind.equals(prop.getSystemKind())) {
					Direction propDirection = buildDirection(prop.getDirection(),direction);
					if(propDirection != null){
						DirectionConfig config = new DirectionConfig();
						config.setName(prop.getName());
						config.setVal(prop.getValue());
						config.setDirection(propDirection);
						directionConfig.add(config);
					}
				}
			}
		}
		return directionConfig;
	}
	
	public CCFCoreProperties loadCCFCoreProperties() throws JAXBException, IOException {
		CCFCoreProperties properties = null;
		String fileLocation = String.format("%s/%s",ControllerHelper.landscapeDirName(runtimePropertyHolder.getCcfHome()),"ccfcoredefaultconfig.xml");
		Resource resource = new FileSystemResource(fileLocation);
		if (resource.exists()) {
			properties = SerializationUtil.deSerialize(resource.getFile(),CCFCoreProperties.class);
			validateCCFCoreProperty(properties);
		}
		return properties;
	}
	
	private Direction buildDirection(Directions directionEnum, Direction direction) {
		if(directionEnum == null || directionEnum.equals(direction.getDirection())){
			return direction;
		}
		return null;
	}
	
	private void validateCCFCoreProperty(CCFCoreProperties properties) throws JAXBException{
		for(CCFCoreProperty ccfProperty: properties.getCcfCoreProperties()){
			Set<ConstraintViolation<CCFCoreProperty>> errors = validator.validate(ccfProperty);
			if (!errors.isEmpty()) {
				throw new JAXBException("Required attributes for given ccfcoredefaultconfig.xml are missing");
			}
		}
	}
}
