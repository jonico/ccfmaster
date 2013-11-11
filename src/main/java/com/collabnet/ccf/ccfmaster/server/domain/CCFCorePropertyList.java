package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CCFCorePropertyList {

    private transient Direction   direction;

    //LazyList is used for dynamic binding values from JSP
    @SuppressWarnings("unchecked")
    @XmlElement(name = "ccfcoreproperty")
    private List<CCFCoreProperty> ccfCoreProperties = LazyList
                                                            .decorate(
                                                                    new ArrayList<CCFCoreProperty>(),
                                                                    FactoryUtils
                                                                            .instantiateFactory(CCFCoreProperty.class));

    public List<CCFCoreProperty> getCcfCoreProperties() {
        return ccfCoreProperties;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setCcfCoreProperties(List<CCFCoreProperty> ccfCoreProperties) {
        this.ccfCoreProperties = ccfCoreProperties;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
