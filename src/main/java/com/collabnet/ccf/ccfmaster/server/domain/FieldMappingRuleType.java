package com.collabnet.ccf.ccfmaster.server.domain;

public enum FieldMappingRuleType {
    /** map a field/TLA to a field/TLA */
    DIRECT_FIELD,
    /** maps a constant value to a field/TLA */
    DIRECT_CONSTANT,
    /** maps a field/TLA to a field/TLA with a value map in the middle */
    DIRECT_VALUE_MAP,

    /** map a field/TLA to a field/TLA if a condition applies */
    CONDITIONAL_FIELD,
    /** maps a constant value to a field/TLA if a condition applies */
    CONDITIONAL_CONSTANT,
    /**
     * maps a field/TLA to a field/TLA with a value map in the middle if a
     * condition applies
     */
    CONDITIONAL_VALUE_MAP,

    /**
     * a user-supplied XSL snippet that takes a field/TLA as input and does
     * something with it
     */
    CUSTOM_XSLT_SNIPPET,
    /**
     * the whole mapping is a user-supplied XSLT document. We don't know what it
     * does
     */
    CUSTOM_XSLT_DOCUMENT,

    /** pre-processing XSLT document that converts GAF to mapforce schema */
    MAPFORCE_PRE,
    /** post-processing XSLT document that converts mapforce schema to GAF */
    MAPFORCE_POST,
    /**
     * XSLT document that maps one mapforce schema artifact to another mapforce
     * schema artifact
     */
    MAPFORCE_MAIN,
    /** a mapforce document that corresponds to the MAPFORCE_MAIN XSLT document */
    MAPFORCE_MFD,

    /**
     * source repository schema. Used in MapForce and mapping rule based
     * mappings.
     */
    SOURCE_REPOSITORY_LAYOUT,
    /**
     * target repository schema. Used in MapForce and mapping rule based
     * mappings.
     */
    TARGET_REPOSITORY_LAYOUT
}