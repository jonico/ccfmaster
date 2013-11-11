package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.List;

public interface Mapping<ParentType> {
    public FieldMappingKind getKind();

    public Directions getMappingDirection();

    public ParentType getParent();

    public List<FieldMappingRule> getRules();

    public File getStorageDirectory(File baseDir);

    public List<FieldMappingValueMap> getValueMaps();
}
