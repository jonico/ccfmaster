package com.collabnet.ccf.ccfmaster.server.domain;


import java.io.File;
import java.util.List;

public interface Mapping<ParentType> {
	public ParentType getParent();
	public FieldMappingKind getKind();
	public List<FieldMappingRule> getRules();
	public List<FieldMappingValueMap> getValueMaps();
	public File getStorageDirectory(File baseDir);
}
