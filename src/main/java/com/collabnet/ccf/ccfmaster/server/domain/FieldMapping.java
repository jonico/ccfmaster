package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooEntity(finders={"findFieldMappingsByParent"})
@XmlRootElement
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldMapping implements Mapping<RepositoryMappingDirection> {
	private static final Logger log = LoggerFactory.getLogger(FieldMapping.class);
	
	@NotNull
	@ManyToOne(cascade={})
	@OnDelete(action = OnDeleteAction.CASCADE)
	@XmlJavaTypeAdapter(RepositoryMappingDirection.XmlAdapter.class)
	private RepositoryMappingDirection parent;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private FieldMappingScope scope;

	@Pattern(regexp="[\\w\\s]+")
	private String param;

	@NotNull
	@Enumerated(EnumType.STRING)
	private FieldMappingKind kind;

	@OneToMany(cascade=javax.persistence.CascadeType.ALL, orphanRemoval=true)
	private List<FieldMappingRule> rules = new ArrayList<FieldMappingRule>();

	@OneToMany(cascade=javax.persistence.CascadeType.ALL, orphanRemoval=true)
	private List<FieldMappingValueMap> valueMaps = new ArrayList<FieldMappingValueMap>();

	@Override
	public File getStorageDirectory(File baseDir) {
		final RepositoryMappingDirection rmd = getParent();
		final Landscape landscape = rmd.getRepositoryMapping().getExternalApp().getLandscape();
		final File dir = new File(baseDir, String.format(
				"landscape%d/fieldmappings/%s/%d",
				landscape.getId(),
				rmd.getDirection(),
				rmd.getId()));
		return dir;
	}
	
    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, FieldMapping> {

		@Override
		public FieldMapping unmarshal(Long v) throws Exception {
			return findFieldMapping(v);
		}

		@Override
		public Long marshal(FieldMapping v) throws Exception {
			return v.getId();
		}
    	
    }

    public static TypedQuery<FieldMapping> findFieldMappingsByExternalApp(ExternalApp externalApp) {
        if (externalApp == null) throw new IllegalArgumentException("The externalApp argument is required");
        EntityManager em = RepositoryMapping.entityManager();
        TypedQuery<FieldMapping> q = em.createQuery("SELECT FieldMapping FROM FieldMapping AS fieldmapping WHERE fieldmapping.parent.repositoryMapping.externalApp = :externalApp", FieldMapping.class);
        q.setParameter("externalApp", externalApp);
        return q;
    }
    
    public static long countFieldMappingsByExternalApp(ExternalApp ea) {
        return entityManager().createQuery("select count(o) from FieldMapping o where o.parent.repositoryMapping.externalApp = :externalApp", Long.class).setParameter("externalApp", ea).getSingleResult();
    }

    public static long countFieldMappingsByParent(RepositoryMappingDirection rmd) {
        return entityManager().createQuery("select count(o) from FieldMapping o where o.parent = :parent", Long.class).setParameter("parent", rmd).getSingleResult();
    }

}
