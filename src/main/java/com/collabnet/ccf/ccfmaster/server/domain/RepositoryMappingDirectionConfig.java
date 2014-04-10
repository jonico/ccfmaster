package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
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
    @XmlJavaTypeAdapter(RepositoryMappingDirection.XmlAdapter.class)
    private RepositoryMappingDirection repositoryMappingDirection;

    @NotNull
    private String                     name;

    @NotNull
    @Size(max = 10485760)
    private String                     val;

    public static long countRepositoryMappingDirectionConfigsByExternalAppAndRepositoryMappingDirection(
            ExternalApp ea,
            RepositoryMappingDirection repositoryMappingDirection) {
        return entityManager()
                .createQuery(
                        "select count(o) from RepositoryMappingDirectionConfig o where o.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp AND  o.repositoryMappingDirection = :repositoryMappingDirection",
                        Long.class)
                .setParameter("externalApp", ea)
                .setParameter("repositoryMappingDirection",
                        repositoryMappingDirection).getSingleResult();
    }

    public static TypedQuery<RepositoryMappingDirectionConfig> findRepositoryMappingDirectionConfigByExternalApp(
            ExternalApp externalApp) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirectionConfig> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirectionConfig AS o WHERE o.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp ORDER BY o.id",
                        RepositoryMappingDirectionConfig.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirectionConfig> findRepositoryMappingDirectionConfigByExternalAppAndRepositoryMapping(
            ExternalApp externalApp, RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirectionConfig> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirectionConfig AS o WHERE o.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp AND o.repositoryMappingDirection.repositoryMapping = :repositoryMapping ORDER BY o.id",
                        RepositoryMappingDirectionConfig.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirectionConfig> findRepositoryMappingDirectionConfigByExternalAppAndRepositoryMappingDirection(
            ExternalApp externalApp,
            RepositoryMappingDirection repositoryMappingDirection) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        if (repositoryMappingDirection == null)
            throw new IllegalArgumentException(
                    "The repositoryMappingDirection argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirectionConfig> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirectionConfig AS o WHERE o.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp AND o.repositoryMappingDirection = :repositoryMappingDirection ORDER BY o.id",
                        RepositoryMappingDirectionConfig.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("repositoryMappingDirection", repositoryMappingDirection);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirectionConfig> findRepositoryMappingDirectionConfigByRepositoryMapping(
            RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirectionConfig> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirectionConfig AS o WHERE o.repositoryMappingDirection.repositoryMapping = :repositoryMapping ORDER BY o.id",
                        RepositoryMappingDirectionConfig.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q;
    }
}
