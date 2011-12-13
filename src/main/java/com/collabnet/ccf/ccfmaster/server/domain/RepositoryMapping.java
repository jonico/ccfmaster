package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@RooEntity(finders = { "findRepositoryMappingsByExternalApp" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"EXTERNAL_APP", "participantRepositoryId", "teamForgeRepositoryId"}))
public class RepositoryMapping {
	
    @NotNull
    private String description;

    @NotNull
    @ManyToOne(cascade={})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(ExternalApp.XmlAdapter.class)
    private ExternalApp externalApp;

    @NotNull
    @Size(max = 128)
    @Index(name="teamForgeRepositoryIndex")
    @Pattern(regexp = "^[\\w\\-]+$")
    private String teamForgeRepositoryId;

    @NotNull
    @Size(max = 128)
    @Index(name="participantRepositoryIdIndex")
    @Pattern(regexp = "^[^']+$")
    private String participantRepositoryId;
    
    
    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, RepositoryMapping> {

		@Override
		public RepositoryMapping unmarshal(Long v) throws Exception {
			return findRepositoryMapping(v);
		}

		@Override
		public Long marshal(RepositoryMapping v) throws Exception {
			return v.getId();
		}
    	
    }
    
    public static long countRepositoryMappingsByExternalApp(ExternalApp ea) {
        return entityManager()
	        .createQuery("select count(o) from RepositoryMapping o where o.externalApp = :externalApp", Long.class)
	        .setParameter("externalApp", ea)
	        .getSingleResult();
    }
    
    public static long countRepositoryMappingsByLandscape(Landscape landscape) {
        return entityManager()
	        .createQuery("select count(o) from RepositoryMapping o where o.externalApp.landscape = :landscape", Long.class)
	        .setParameter("landscape", landscape)
	        .getSingleResult();

    }

    public static TypedQuery<RepositoryMapping> findRepositoryMappingsByLandscape(Landscape landscape) {
    	return entityManager()
    		.createQuery("select o from RepositoryMapping o where o.externalApp.landscape = :landscape ORDER BY o.id", RepositoryMapping.class)
    		.setParameter("landscape", landscape);
    }

	public static TypedQuery<RepositoryMapping> findRepositoryMappingsByExternalApp(ExternalApp externalApp) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<RepositoryMapping> q = em.createQuery("SELECT o FROM RepositoryMapping AS o WHERE o.externalApp = :externalApp ORDER BY o.id", RepositoryMapping.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }
}
