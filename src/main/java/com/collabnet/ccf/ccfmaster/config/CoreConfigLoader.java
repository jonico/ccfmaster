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

import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
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

	public void setRuntimePropertyHolder(CCFRuntimePropertyHolder runtimePropertyHolder) {
		this.runtimePropertyHolder = runtimePropertyHolder;
	}

	public List<DirectionConfig> getAsDirectionConfigList(Direction direction) throws JAXBException, IOException {
		return getAsDirectionConfigList(loadCCFCoreProperties(),direction);
	}
	
	public List<CCFCoreProperty> getDefaultCCFCorePropertyList(Direction direction) throws JAXBException, IOException {
		return getDefaultCCFCorePropertyList(loadCCFCoreProperties(),direction);
	}

	public static List<DirectionConfig> getAsDirectionConfigList(CCFCorePropertyList properties, Direction direction) {
		List<DirectionConfig> directionConfig = new ArrayList<DirectionConfig>();
		List<CCFCoreProperty> directionSpecificPropList =getDefaultCCFCorePropertyList(properties,direction); 
		for (CCFCoreProperty prop : directionSpecificPropList) {
			DirectionConfig config = new DirectionConfig();
			config.setName(prop.getName());
			config.setVal(prop.getValue());
			config.setDirection(direction);
			directionConfig.add(config);
		}
		return directionConfig;
	}
	
	public CCFCorePropertyList loadCCFCoreProperties() throws JAXBException, IOException {
		CCFCorePropertyList properties = null;
		String fileLocation = String.format("%s/%s",ControllerHelper.landscapeDirName(runtimePropertyHolder.getCcfHome()),"ccfcoredefaultconfig.xml");
		Resource resource = new FileSystemResource(fileLocation);
		if (resource.exists()) {
			properties = SerializationUtil.deSerialize(resource.getFile(),CCFCorePropertyList.class);
			validateCCFCoreProperty(properties);
		}
		return properties;
	}
	
	private static Direction buildDirection(Directions directionEnum, Direction direction) {
		if(directionEnum == null || directionEnum.equals(direction.getDirection())){
			return direction;
		}
		return null;
	}
	
	private void validateCCFCoreProperty(CCFCorePropertyList properties) throws JAXBException{
		for(CCFCoreProperty ccfProperty: properties.getCcfCoreProperties()){
			Set<ConstraintViolation<CCFCoreProperty>> errors = validator.validate(ccfProperty);
			if (!errors.isEmpty()) {
				throw new JAXBException("Required attributes for given ccfcoredefaultconfig.xml are missing");
			}
		}
	}
	
	private static List<CCFCoreProperty> getDefaultCCFCorePropertyList(CCFCorePropertyList properties, Direction direction){
		List<CCFCoreProperty> corePropertyList = new ArrayList<CCFCoreProperty>();
		SystemKind systemkind = direction.getLandscape().getParticipant().getSystemKind();
		if (properties != null) {
			for (CCFCoreProperty prop : properties.getCcfCoreProperties()) {
					Direction propDirection = buildDirection(prop.getDirection(),direction);
					if(propDirection != null && systemkind.equals(prop.getSystemKind())){
						corePropertyList.add(prop);
					}
			}
		}
		return corePropertyList;
	}
}
