package com.collabnet.ccf.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

public abstract class SerializationUtil {
	
	@SuppressWarnings("unchecked")
	public static <T> T deSerialize(InputStream stream,Class<T> className) throws JAXBException{
		JAXBContext xmlContext = JAXBContext.newInstance(className);
		Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
		return (T) unmarshaller.unmarshal(stream);
	}
	
	
	public static <T> T deSerialize(File file,Class<T> className) throws JAXBException, IOException{
		return deSerialize(FileUtils.openInputStream(file), className);
	}

}
