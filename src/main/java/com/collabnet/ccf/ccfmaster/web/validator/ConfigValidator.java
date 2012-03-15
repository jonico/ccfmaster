package com.collabnet.ccf.ccfmaster.web.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperties;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCoreProperty;




public class ConfigValidator implements Validator {



	@Override
	public boolean supports(Class<?> clazz) {
		return CCFCoreProperties.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CCFCoreProperties ccfCoreProperties=(CCFCoreProperties)target;
		List<CCFCoreProperty> ccfPropertyList =ccfCoreProperties.getCcfCoreProperties();
		for (int i = 0; i < ccfPropertyList.size(); i++) {  
			if (StringUtils.isEmpty(ccfPropertyList.get(i).getValue()))   
				errors.rejectValue("ccfCoreProperties[" + i + "].value", "NotEmpty.ccfcoreproperties[0].value","Blank value not accepted");  
		}  

	}

}
