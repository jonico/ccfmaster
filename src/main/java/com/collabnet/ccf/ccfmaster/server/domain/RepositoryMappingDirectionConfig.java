package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findRepositoryMappingDirectionConfigsByRepositoryMappingDirection" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "REPOSITORY_MAPPING_DIRECTION", "NAME" }))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RepositoryMappingDirectionConfig {

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, RepositoryMappingDirectionConfig> {

        @Override
        public Long marshal(RepositoryMappingDirectionConfig v)
                throws Exception {
            return v.getId();
        }

        @Override
        public RepositoryMappingDirectionConfig unmarshal(Long v)
                throws Exception {
            return findRepositoryMappingDirectionConfig(v);
        }
    }

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Direction.XmlAdapter.class)
    private RepositoryMappingDirection repositoryMappingDirection;

    @NotNull
    private String                     name;

    @NotNull
    @Size(max = 10485760)
    private String                     val;
}
