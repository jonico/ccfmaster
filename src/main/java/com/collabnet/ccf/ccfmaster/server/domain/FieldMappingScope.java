package com.collabnet.ccf.ccfmaster.server.domain;

public enum FieldMappingScope {
    /** a regular field mapping */
    REPOSITORY_MAPPING_DIRECTION,
    /**
     * mappings with this scope reference a
     * {@link FieldMappingLandscapeTemplate} by name
     */
    LANDSCAPE,
    /**
     * mappings with this scope reference a
     * {@link FieldMappingExternalAppTemplate} by name
     */
    EXTERNAL_APP,
    /**
     * mappings shipped with CCF core. Used for static mappings (i.e. SWP
     * integrations)
     */
    CCF_CORE
}