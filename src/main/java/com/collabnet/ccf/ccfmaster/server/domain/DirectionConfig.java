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
@RooEntity(finders = { "findDirectionConfigsByDirection",
        "findDirectionConfigsByDirectionAndName" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "direction",
        "name" }))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectionConfig implements ConfigItem, PersistableConfigItem<DirectionConfig> {

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, DirectionConfig> {

        @Override
        public Long marshal(DirectionConfig v) throws Exception {
            return v.getId();
        }

        @Override
        public DirectionConfig unmarshal(Long v) throws Exception {
            return findDirectionConfig(v);
        }
    }

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Direction.XmlAdapter.class)
    private Direction direction;

    @NotNull
    private String    name;

    @NotNull
    @Size(max = 10485760)
    private String    val;

    public static long countDirectionConfigsByDirection(Direction direction) {
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = DirectionConfig.entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(directionconfig) FROM DirectionConfig AS directionconfig WHERE directionconfig.direction = :direction",
                        Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

}
