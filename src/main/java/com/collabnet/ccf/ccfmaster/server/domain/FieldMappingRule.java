package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

@RooJavaBean
@RooEntity
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldMappingRule {
    private String               name;
    private String               description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingRuleType type;

    private String               source;
    private boolean              sourceIsTopLevelAttribute;

    private String               target;
    private boolean              targetIsTopLevelAttribute;

    @Size(max = 1024)
    private String               condition;

    private String               valueMapName;

    @Size(max = 10485760)
    // 10MB should be enough - same as *Config.val
    private String               xmlContent;

}
