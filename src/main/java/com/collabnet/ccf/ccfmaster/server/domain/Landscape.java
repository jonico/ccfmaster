package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

@XmlRootElement
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(name = "UNIQUE_PARTICIPANTS", columnNames = { "TEAM_FORGE", "PARTICIPANT" }))
@RooEntity(finders = { "findLandscapesByPlugIdEquals" })
public class Landscape {

    /**
	 * by convention, TF is the Participant with ID==1. Use this as default.
	 * 
	public Landscape() {
		super();
		try {
			setTeamForge(Participant.findParticipant(1L));
		} catch (PersistenceException e) {
			log.info("exception setting teamForge to default value. If this occurs during initialization, it's OK.", e);
		}
	}
	 */
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Participant.XmlAdapter.class)
    private Participant teamForge;

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Participant.XmlAdapter.class)
    private Participant participant;

    @NotNull
    @Pattern(regexp = "^plug\\d+$")
    @Column(unique = true)
    private String plugId;

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, Landscape> {

        @Override
        public Landscape unmarshal(Long v) throws Exception {
            return findLandscape(v);
        }

        @Override
        public Long marshal(Landscape v) throws Exception {
            return v.getId();
        }
    }

	public static TypedQuery<Landscape> findLandscapesByTeamForgeOrParticipant(Participant participant) {
        if (participant == null) throw new IllegalArgumentException("The participant argument is required");
        EntityManager em = Landscape.entityManager();
        TypedQuery<Landscape> q = em.createQuery("SELECT o FROM Landscape AS o WHERE o.teamForge = :teamForge OR o.participant = :participant", Landscape.class);
        q.setParameter("teamForge", participant);
        q.setParameter("participant", participant);
        return q;
    }
}
