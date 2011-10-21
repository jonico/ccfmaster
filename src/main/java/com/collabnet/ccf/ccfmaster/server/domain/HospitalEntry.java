package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
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
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.Assert;

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@RooEntity(finders = { "findHospitalEntrysByRepositoryMappingDirection"})
public class HospitalEntry {

    @NotNull
    private String description;

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(RepositoryMappingDirection.XmlAdapter.class)
    private RepositoryMappingDirection repositoryMappingDirection;

    @Size(max = 128)
    private String timestamp;

    @Size(max = 256)
    private String exceptionClassName;

    @Size(max = 65536)
    private String exceptionMessage;

    @Size(max = 128)
    private String causeExceptionClassName;

    @Size(max = 65536)
    private String causeExceptionMessage;

    @Size(max = 65536)
    private String stackTrace;

    @Size(max = 128)
    private String adaptorName;

    @Size(max = 128)
    @Index(name = "hospitalOriginatingComponentIndex")
    private String originatingComponent;

    @Size(max = 128)
    private String dataType;

    @Size(max = 65536)
    private String data;

    private Boolean fixed;

    private Boolean reprocessed;

    @Size(max = 128)
    @Index(name = "hospitalSourceArtifactIdIndex")
    private String sourceArtifactId;

    @Size(max = 128)
    @Index(name = "hospitalTargetArtifactIdIndex")
    private String targetArtifactId;

    @Size(max = 128)
    @Index(name = "hospitalErrorCodeIndex")
    private String errorCode;

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

    @Size(max = 128)
    private String artifactType;

    @Size(max = 10485760)
    private String genericArtifact;

    public static long countHospitalEntrysByExternalApp(ExternalApp ea) {
        return entityManager().createQuery("select count(o) from HospitalEntry o where o.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp", Long.class).setParameter("externalApp", ea).getSingleResult();
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByExternalApp(ExternalApp externalApp) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByRepositoryMapping(RepositoryMapping rm) {
        if (rm == null) throw new IllegalArgumentException("The rm argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping = :rm ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("rm", rm);
        return q;
    }

    public static long countHospitalEntrysByRepositoryMapping(RepositoryMapping rm) {
        if (rm == null) throw new IllegalArgumentException("The rm argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping = :rm", Long.class);
        q.setParameter("rm", rm);
        return q.getSingleResult();
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByExternalAppAndDirection(ExternalApp externalApp, Directions direction) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp AND hospitalentry.repositoryMappingDirection.direction = :direction ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByRepositoryMappingAndDirection(RepositoryMapping rm, Directions direction) {
        if (rm == null) throw new IllegalArgumentException("The rm argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping = :rm AND hospitalentry.repositoryMappingDirection.direction = :direction ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("rm", rm);
        q.setParameter("direction", direction);
        return q;
    }

    public static long countHospitalEntrysByRepositoryMappingAndDirection(RepositoryMapping rm, Directions direction) {
        if (rm == null) throw new IllegalArgumentException("The rm argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping = :rm AND hospitalentry.repositoryMappingDirection.direction = :direction", Long.class);
        q.setParameter("rm", rm);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countHospitalEntrysByExternalAppAndDirection(ExternalApp externalApp, Directions direction) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp = :externalApp AND hospitalentry.repositoryMappingDirection.direction = :direction", Long.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp.landscape = :landscape ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("landscape", landscape);
        return q;
    }

    public static long countHospitalEntrysByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp.landscape = :landscape", Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByLandscapeAndDirection(Landscape landscape, Directions direction) {
        Assert.notNull(landscape, "The landscape argument is required");
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.direction = :direction AND hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp.landscape = :landscape ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByDirection(Directions direction) {
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT HospitalEntry FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.direction = :direction ORDER BY hospitalentry.id", HospitalEntry.class);
        q.setParameter("direction", direction);
        return q;
    }

    public static long countHospitalEntrysByDirection(Directions direction) {
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.direction = :direction", Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countHospitalEntrysByLandscapeAndDirection(Landscape landscape, Directions direction) {
        Assert.notNull(landscape, "The landscape argument is required");
        Assert.notNull(direction, "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.direction = :direction AND hospitalentry.repositoryMappingDirection.repositoryMapping.externalApp.landscape = :landscape", Long.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static long countHospitalEntrysByRepositoryMappingDirection(RepositoryMappingDirection repositoryMappingDirection) {
        if (repositoryMappingDirection == null) throw new IllegalArgumentException("The repositoryMappingDirection argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection = :repositoryMappingDirection", Long.class);
        q.setParameter("repositoryMappingDirection", repositoryMappingDirection);
        return q.getSingleResult();
    }

    public static TypedQuery<HospitalEntry> findHospitalEntrysByRepositoryMappingDirection(RepositoryMappingDirection repositoryMappingDirection) {
        if (repositoryMappingDirection == null) throw new IllegalArgumentException("The repositoryMappingDirection argument is required");
        EntityManager em = HospitalEntry.entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT o FROM HospitalEntry AS o WHERE o.repositoryMappingDirection = :repositoryMappingDirection ORDER BY o.id", HospitalEntry.class);
        q.setParameter("repositoryMappingDirection", repositoryMappingDirection);
        return q;
    }

  public static TypedQuery<HospitalEntry> findHospitalEntrysByDirectionAndSourceArtifactIdOrTargetArtifactId(Directions direction, String sourceArtifactId, String targetArtifactId) {
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = HospitalEntry.entityManager();
        TypedQuery<HospitalEntry> q = em.createQuery("SELECT o FROM HospitalEntry AS o WHERE o.repositoryMappingDirection.direction = :direction AND (o.sourceArtifactId = :sourceArtifactId OR o.targetArtifactId = :targetArtifactId) ORDER BY o.id", HospitalEntry.class);
        q.setParameter("direction", direction);
        q.setParameter("sourceArtifactId", sourceArtifactId);
        q.setParameter("targetArtifactId", targetArtifactId);
        return q;
    }
  
  public static long countHospitalEntrysByByDirectionAndSourceArtifactIdOrTargetArtifactId(Directions direction, String sourceArtifactId, String targetArtifactId) {
	  if (direction == null) throw new IllegalArgumentException("The direction argument is required");
      EntityManager em = entityManager();
      TypedQuery<Long> q = em.createQuery("SELECT COUNT(hospitalentry) FROM HospitalEntry AS hospitalentry WHERE hospitalentry.repositoryMappingDirection.direction = :direction AND (hospitalentry.sourceArtifactId = :sourceArtifactId OR hospitalentry.targetArtifactId = :targetArtifactId)", Long.class);
      q.setParameter("direction", direction);
      q.setParameter("sourceArtifactId", sourceArtifactId);
      q.setParameter("targetArtifactId", targetArtifactId);
      return q.getSingleResult();
  }
  
  
}
