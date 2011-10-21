package com.collabnet.ccf.ccfmaster.web.model;


import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUpload {

	CommonsMultipartFile file;
	private String fieldmappingName="";

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	public String getFieldmappingName() {
		return fieldmappingName;
	}

	public void setFieldmappingName(String fieldmappingName) {
		this.fieldmappingName = fieldmappingName;
	}

	
	
	
}
