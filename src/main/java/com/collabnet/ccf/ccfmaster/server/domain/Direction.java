package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.Assert;

@RooJavaBean
@RooToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "LANDSCAPE", "DIRECTION" }))
@RooEntity(finders = { "findDirectionsByLandscapeEquals", "findDirectionsByLandscapeEqualsAndDirectionEquals", "findDirectionsByDirection" })
public class Direction {

    @NotNull
    private String description;

    @NotNull
    @Enumerated
    @Index(name = "directionDirectionIndex")
    private Directions direction;

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    private Landscape landscape;

    @NotNull
    private Boolean shouldStartAutomatically = false;
    
    public File determineBaseDirectory(File ccfHome) {
		final Landscape landscape = getLandscape();
		final SystemKind teamForgeSystemKind = landscape.getTeamForge().getSystemKind();
		final SystemKind participantSystemKind = landscape.getParticipant().getSystemKind();
		final Participant participant = landscape.getParticipant();
		Assert.isTrue(teamForgeSystemKind == SystemKind.TF, "landscape.teamForge must be of Kind TF, was: " + teamForgeSystemKind);
	
		// handle inconsistent directory names
		String scenario = null;
		switch (participantSystemKind) {
		case QC:
			scenario = "QCTF";
			break;
		case SWP:
			scenario = "TFSWP";
			break;
		case GENERIC:
			scenario = String.format("%sTF", participant.getPrefix()); 
			break;
		default:
			throw new IllegalArgumentException("CCF doesn't support mapping TF <-> " + participantSystemKind);
		}
		String baseName = baseName();
		File res = new File(ccfHome, String.format("landscape%d/samples/%s/%s", landscape.getId(), scenario, baseName));
		Assert.isTrue(res.exists(), "Scenario base directory doesn't exist: " + res);
		return res;
    }
    
	private String baseName() {
		final Landscape landscape = getLandscape();
		final SystemKind teamForgeSystemKind = landscape.getTeamForge().getSystemKind();
		final SystemKind participantSystemKind = landscape.getParticipant().getSystemKind();
		Assert.isTrue(teamForgeSystemKind == SystemKind.TF, "landscape.teamForge must be of Kind TF, was: " + teamForgeSystemKind);
		if(participantSystemKind.equals(SystemKind.GENERIC)){
			return String.format(
					getDirection() == Directions.FORWARD ? "%1$s2%2$s" : "%2$s2%1$s",
					teamForgeSystemKind,
					landscape.getParticipant().getPrefix());
		}
		return String.format(
				getDirection() == Directions.FORWARD ? "%1$s2%2$s" : "%2$s2%1$s",
				teamForgeSystemKind,
				participantSystemKind);
	}

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, Direction> {

        @Override
        public Direction unmarshal(Long v) throws Exception {
            return findDirection(v);
        }

        @Override
        public Long marshal(Direction v) throws Exception {
            return v.getId();
        }
    }

    public static TypedQuery<Direction> findDirectionsByLandscapeEquals(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<Direction> q = em.createQuery("SELECT Direction FROM Direction AS direction WHERE direction.landscape = :landscape", Direction.class);
        q.setParameter("landscape", landscape);
        return q;
    }

    public static long countDirectionsByLandscapeEquals(Landscape landscape) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(direction) FROM Direction AS direction WHERE direction.landscape = :landscape", Long.class);
        q.setParameter("landscape", landscape);
        return q.getSingleResult();
    }

    public static TypedQuery<Direction> findDirectionsByLandscapeEqualsAndDirectionEquals(Landscape landscape, Directions direction) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Direction> q = em.createQuery("SELECT Direction FROM Direction AS direction WHERE direction.landscape = :landscape  AND direction.direction = :direction", Direction.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q;
    }

    public static long countDirectionsByLandscapeAndDirection(Landscape landscape, Directions direction) {
        if (landscape == null) throw new IllegalArgumentException("The landscape argument is required");
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(direction) FROM Direction AS direction WHERE direction.landscape = :landscape  AND direction.direction = :direction", Long.class);
        q.setParameter("landscape", landscape);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static TypedQuery<Direction> findDirectionsByDirection(Directions direction) {
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Direction> q = em.createQuery("SELECT Direction FROM Direction AS direction WHERE direction.direction = :direction", Direction.class);
        q.setParameter("direction", direction);
        return q;
    }

    public static long countDirectionsByDirection(Directions direction) {
        if (direction == null) throw new IllegalArgumentException("The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(direction) FROM Direction AS direction WHERE direction.direction = :direction", Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }
}
