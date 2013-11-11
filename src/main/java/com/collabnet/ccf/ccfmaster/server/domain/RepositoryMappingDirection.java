package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.Assert;

@RooJavaBean
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "DIRECTION",
        "REPOSITORY_MAPPING" }))
@RooEntity(finders = {
        "findRepositoryMappingDirectionsByRepositoryMappingAndDirection",
        "findRepositoryMappingDirectionsByDirection",
        "findRepositoryMappingDirectionsByRepositoryMapping" })
public class RepositoryMappingDirection {

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, RepositoryMappingDirection> {

        @Override
        public Long marshal(RepositoryMappingDirection v) throws Exception {
            return v.getId();
        }

        @Override
        public RepositoryMappingDirection unmarshal(Long v) throws Exception {
            return findRepositoryMappingDirection(v);
        }
    }

    @NotNull
    @Enumerated
    @Index(name = "repositoyMappingDirectionDirectionIndex")
    private Directions                       direction;

    @NotNull
    @ManyToOne(cascade = {})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(RepositoryMapping.XmlAdapter.class)
    private RepositoryMapping                repositoryMapping;

    @NotNull
    @Enumerated
    @Index(name = "repositoyMappingDirectionStatusIndex")
    private RepositoryMappingDirectionStatus status = RepositoryMappingDirectionStatus.PAUSED;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date                             lastSourceArtifactModificationDate;

    @Size(max = 128)
    private String                           lastSourceArtifactVersion;

    @Size(max = 128)
    private String                           lastSourceArtifactId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConflictResolutionPolicy         conflictResolutionPolicy;

    @OneToOne(cascade = {})
    @XmlJavaTypeAdapter(FieldMapping.XmlAdapter.class)
    private FieldMapping                     activeFieldMapping;

    public String getSourceRepositoryId() {
        return direction == Directions.FORWARD ? getRepositoryMapping()
                .getTeamForgeRepositoryId() : getRepositoryMapping()
                .getParticipantRepositoryId();
    }

    public Participant getSourceSystem() {
        Landscape landscape = getRepositoryMapping().getExternalApp()
                .getLandscape();
        if (getDirection() == Directions.FORWARD) {
            return landscape.getTeamForge();
        } else {
            return landscape.getParticipant();
        }
    }

    public String getSourceSystemEncoding() {
        return getSourceSystem().getEncoding();
    }

    public String getSourceSystemId() {
        return getSourceSystem().getSystemId();
    }

    public SystemKind getSourceSystemKind() {
        return getSourceSystem().getSystemKind();
    }

    public String getSourceSystemTimezone() {
        return null;
    }

    public String getTargetRepositoryId() {
        return direction == Directions.REVERSE ? getRepositoryMapping()
                .getTeamForgeRepositoryId() : getRepositoryMapping()
                .getParticipantRepositoryId();
    }

    public Participant getTargetSystem() {
        Landscape landscape = getRepositoryMapping().getExternalApp()
                .getLandscape();
        if (getDirection() == Directions.REVERSE) {
            return landscape.getTeamForge();
        } else {
            return landscape.getParticipant();
        }
    }

    public String getTargetSystemEncoding() {
        return getTargetSystem().getEncoding();
    }

    public String getTargetSystemId() {
        return getTargetSystem().getSystemId();
    }

    public SystemKind getTargetSystemKind() {
        return getTargetSystem().getSystemKind();
    }

    public String getTargetSystemTimezone() {
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Direction: ").append(getDirection()).append(", ");
        sb.append("RepositoryMapping: ").append(getRepositoryMapping())
                .append(", ");
        return sb.toString();
    }

    public static long countRepositoryMappingDirectionsByDirection(
            Directions direction) {
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = RepositoryMappingDirection.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.direction = :direction",
                        Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByExternalApp(
            ExternalApp ea) {
        return entityManager()
                .createQuery(
                        "select count(o) from RepositoryMappingDirection o where o.repositoryMapping.externalApp = :externalApp",
                        Long.class).setParameter("externalApp", ea)
                .getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByExternalAppAndDirection(
            ExternalApp externalApp, Directions direction) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping.externalApp = :externalApp AND repositorymappingdirection.direction = :direction",
                        Long.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByLandscape(
            Landscape landscape) {
        if (landscape == null)
            throw new IllegalArgumentException(
                    "The landscape argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping.externalApp.landscape = :landscape",
                        Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByLandscapeAndDirection(
            Landscape landscape, Directions direction) {
        Assert.notNull(landscape, "The landscape argument is required");
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.direction = :direction AND repositorymappingdirection.repositoryMapping.externalApp.landscape = :landscape",
                        Long.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByRepositoryMapping(
            RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping = :repositoryMapping",
                        Long.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q.getSingleResult();
    }

    public static long countRepositoryMappingDirectionsByRepositoryMappingAndDirection(
            RepositoryMapping repositoryMapping, Directions direction) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(repositorymappingdirection) FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping = :repositoryMapping AND repositorymappingdirection.direction = :direction",
                        Long.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByDirection(
            Directions direction) {
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = RepositoryMappingDirection.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirection AS o WHERE o.direction = :direction ORDER BY o.id",
                        RepositoryMappingDirection.class);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByExternalApp(
            ExternalApp externalApp) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT RepositoryMappingDirection FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping.externalApp = :externalApp ORDER BY repositorymappingdirection.id",
                        RepositoryMappingDirection.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByExternalAppAndDirection(
            ExternalApp externalApp, Directions direction) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT RepositoryMappingDirection FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping.externalApp = :externalApp AND repositorymappingdirection.direction = :direction ORDER BY repositorymappingdirection.id",
                        RepositoryMappingDirection.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByLandscape(
            Landscape landscape) {
        if (landscape == null)
            throw new IllegalArgumentException(
                    "The landscape argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT RepositoryMappingDirection FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.repositoryMapping.externalApp.landscape = :landscape ORDER BY repositorymappingdirection.id",
                        RepositoryMappingDirection.class);
        q.setParameter("landscape", landscape);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByLandscapeAndDirection(
            Landscape landscape, Directions direction) {
        Assert.notNull(landscape, "The landscape argument is required");
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT RepositoryMappingDirection FROM RepositoryMappingDirection AS repositorymappingdirection WHERE repositorymappingdirection.direction = :direction AND repositorymappingdirection.repositoryMapping.externalApp.landscape = :landscape ORDER BY repositorymappingdirection.id",
                        RepositoryMappingDirection.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByRepositoryMapping(
            RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        EntityManager em = RepositoryMappingDirection.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirection AS o WHERE o.repositoryMapping = :repositoryMapping ORDER BY o.id",
                        RepositoryMappingDirection.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q;
    }

    public static TypedQuery<RepositoryMappingDirection> findRepositoryMappingDirectionsByRepositoryMappingAndDirection(
            RepositoryMapping repositoryMapping, Directions direction) {
        if (repositoryMapping == null)
            throw new IllegalArgumentException(
                    "The repositoryMapping argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = RepositoryMappingDirection.entityManager();
        TypedQuery<RepositoryMappingDirection> q = em
                .createQuery(
                        "SELECT o FROM RepositoryMappingDirection AS o WHERE o.repositoryMapping = :repositoryMapping AND o.direction = :direction ORDER BY o.id",
                        RepositoryMappingDirection.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        q.setParameter("direction", direction);
        return q;
    }
}
