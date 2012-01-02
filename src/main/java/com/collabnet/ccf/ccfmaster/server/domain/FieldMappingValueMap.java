package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldMappingValueMap {
	private String name;
	
	@OneToMany(cascade=javax.persistence.CascadeType.ALL, orphanRemoval=true)
	private List<FieldMappingValueMapEntry> entries;
	/**
	 * default value, only applied if hasDefault is true. If set to null, the input to the value map will be used as
	 * default.
	 */
	private String defaultValue;
	@NotNull
	private boolean hasDefault;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		FieldMappingValueMap other = (FieldMappingValueMap) o;
		return name.equals(other.name) &&
			defaultValue.equals(other.defaultValue) &&
			hasDefault == other.hasDefault;
	}
	
}
