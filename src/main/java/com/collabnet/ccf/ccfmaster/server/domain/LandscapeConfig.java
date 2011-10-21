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
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findLandscapeConfigsByLandscape", "findLandscapeConfigsByLandscapeAndName" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"landscape", "name"}))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LandscapeConfig implements PersistableConfigItem<LandscapeConfig> {

    @ManyToOne(cascade={})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    private Landscape landscape;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 10485760)
    private String val;

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, LandscapeConfig> {

        @Override
        public LandscapeConfig unmarshal(Long v) throws Exception {
            return findLandscapeConfig(v);
        }

        @Override
        public Long marshal(LandscapeConfig v) throws Exception {
            return v.getId();
        }
    }

	public static long countLandscapeConfigsByLandscape(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = LandscapeConfig.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(landscapeconfig) FROM LandscapeConfig AS landscapeconfig WHERE landscapeconfig.landscape = :landscape", Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
	}

}
