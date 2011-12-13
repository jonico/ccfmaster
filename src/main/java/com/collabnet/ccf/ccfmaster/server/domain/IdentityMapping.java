package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "REPOSITORY_MAPPING", "sourceArtifactId", "artifactType" }), @UniqueConstraint(columnNames = { "REPOSITORY_MAPPING", "targetArtifactId", "artifactType" }) })
@RooEntity(finders = { "findIdentityMappingsByRepositoryMapping" })
public class IdentityMapping {

    @NotNull
    private String description;

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(RepositoryMapping.XmlAdapter.class)
    private RepositoryMapping repositoryMapping;

    @NotNull
    @Size(max = 128)
    @Pattern(regexp = "^[^']+$")
    @Index(name = "sourceArtifactIdIndex")
    private String sourceArtifactId;

    @NotNull
    @Size(max = 128)
    @Pattern(regexp = "^[^']+$")
    @Index(name = "targetArtifactIdIndex")
    private String targetArtifactId;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date sourceLastModificationTime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date targetLastModificationTime;

    @Size(max = 128)
    private String sourceArtifactVersion;

    @Size(max = 128)
    private String targetArtifactVersion;

    @NotNull
    @Size(max = 128)
    @Pattern(regexp = "^[\\w]+$")
    @Index(name = "artifactType")
    private String artifactType;

    @Size(max = 128)
    String depChildSourceArtifactId;

    @Size(max = 128)
    String depChildSourceRepositoryId;

    @Size(max = 128)
    String depChildSourceRepositoryKind;

    @Size(max = 128)
    String depChildTargetArtifactId;

    @Size(max = 128)
    String depChildTargetRepositoryId;

    @Size(max = 128)
    String depChildTargetRepositoryKind;

    @Size(max = 128)
    String depParentSourceArtifactId;

    @Size(max = 128)
    String depParentSourceRepositoryId;

    @Size(max = 128)
    String depParentSourceRepositoryKind;

    @Size(max = 128)
    String depParentTargetArtifactId;

    @Size(max = 128)
    String depParentTargetRepositoryId;

    @Size(max = 128)
    String depParentTargetRepositoryKind;

    public static long countIdentityMappingsByExternalApp(ExternalApp ea) {
        return entityManager().createQuery("select count(o) from IdentityMapping o where o.repositoryMapping.externalApp = :externalApp", Long.class).setParameter("externalApp", ea).getSingleResult();
    }

    public static TypedQuery<IdentityMapping> findIdentityMappingsByExternalApp(ExternalApp externalApp) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        EntityManager em = entityManager();
        TypedQuery<IdentityMapping> q = em.createQuery("SELECT IdentityMapping FROM IdentityMapping AS identitymapping WHERE identitymapping.repositoryMapping.externalApp = :externalApp ORDER BY identitymapping.id", IdentityMapping.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }

    public static TypedQuery<IdentityMapping> findIdentityMappingsByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<IdentityMapping> q = em.createQuery("SELECT IdentityMapping FROM IdentityMapping AS identitymapping WHERE identitymapping.repositoryMapping.externalApp.landscape = :landscape ORDER BY identitymapping.id", IdentityMapping.class);
        q.setParameter("landscape", landscape);
        return q;
    }

    public static long countIdentityMappingsByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(identitymapping) FROM IdentityMapping AS identitymapping WHERE identitymapping.repositoryMapping.externalApp.landscape = :landscape", Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
    }

    public static long countIdentityMappingsByRepositoryMapping(RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null) throw new IllegalArgumentException("The repositoryMapping argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(identitymapping) FROM IdentityMapping AS identitymapping WHERE identitymapping.repositoryMapping = :repositoryMapping", Long.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q.getSingleResult();
    }

	public static TypedQuery<IdentityMapping> findIdentityMappingsBySourceArtifactIdOrTargetArtifactId(String sourceArtifactId, String targetArtifactId) {
        EntityManager em = IdentityMapping.entityManager();
        TypedQuery<IdentityMapping> q = em.createQuery("SELECT o FROM IdentityMapping AS o WHERE o.sourceArtifactId = :sourceArtifactId OR o.targetArtifactId = :targetArtifactId ORDER BY o.id", IdentityMapping.class);
        q.setParameter("sourceArtifactId", sourceArtifactId);
        q.setParameter("targetArtifactId", targetArtifactId);
        return q;
    }
	
	public static long countIdentityMappingsByExternalAppAndSourceArtifactIdOrTargetArtifactId(ExternalApp ea,String sourceArtifactId, String targetArtifactId) {
		EntityManager em = IdentityMapping.entityManager();
		TypedQuery<Long> q = em.createQuery("select count(o) from IdentityMapping o where o.repositoryMapping.externalApp = :externalApp AND (o.sourceArtifactId = :sourceArtifactId OR o.targetArtifactId = :targetArtifactId)", Long.class);
		q.setParameter("externalApp", ea);
		q.setParameter("sourceArtifactId", sourceArtifactId);
        q.setParameter("targetArtifactId", targetArtifactId);
        return q.getSingleResult();
   }

	public static TypedQuery<IdentityMapping> findIdentityMappingsByRepositoryMapping(RepositoryMapping repositoryMapping) {
        if (repositoryMapping == null) throw new IllegalArgumentException("The repositoryMapping argument is required");
        EntityManager em = IdentityMapping.entityManager();
        TypedQuery<IdentityMapping> q = em.createQuery("SELECT o FROM IdentityMapping AS o WHERE o.repositoryMapping = :repositoryMapping ORDER BY o.id", IdentityMapping.class);
        q.setParameter("repositoryMapping", repositoryMapping);
        return q;
    }
}
