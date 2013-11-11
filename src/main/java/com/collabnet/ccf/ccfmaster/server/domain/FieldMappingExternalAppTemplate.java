package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@XmlRootElement(name = "fieldMappingTemplate")
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@RooEntity(finders = {
        "findFieldMappingExternalAppTemplatesByParentAndNameAndDirection",
        "findFieldMappingExternalAppTemplatesByParentAndDirection",
        "findFieldMappingExternalAppTemplatesByParent" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT", "NAME",
        "DIRECTION" }))
public class FieldMappingExternalAppTemplate implements Template<ExternalApp> {

    @ManyToOne(cascade = {})
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(ExternalApp.XmlAdapter.class)
    private ExternalApp                parent;

    @NotNull
    @Pattern(regexp = "[\\w\\s]+")
    private String                     name;

    @NotNull
    @Enumerated
    private Directions                 direction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingKind           kind;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingRule>     rules     = new ArrayList<FieldMappingRule>();

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingValueMap> valueMaps = new ArrayList<FieldMappingValueMap>();

    @Override
    public Directions getMappingDirection() {
        return getDirection();
    }

    @Override
    public File getStorageDirectory(File baseDir) {
        final ExternalApp ea = getParent();
        final Landscape landscape = ea.getLandscape();
        final File dir = new File(baseDir, String.format(
                "landscape%d/fieldmappings/%s/%s/%s", landscape.getId(),
                getDirection(), ea.getLinkId(), getName()));
        return dir;
    }

    public static long countFieldMappingExternalAppTemplatesByParent(
            ExternalApp externalApp) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(fieldmappingexternalapptemplate) FROM FieldMappingExternalAppTemplate AS fieldmappingexternalapptemplate WHERE fieldmappingexternalapptemplate.parent = :externalApp",
                        Long.class);
        q.setParameter("externalApp", externalApp);
        return q.getSingleResult();
    }

    public static long countFieldMappingExternalAppTemplatesByParentAndDirection(
            ExternalApp externalApp, Directions direction) {
        if (externalApp == null)
            throw new IllegalArgumentException(
                    "The externalApp argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = entityManager();
        TypedQuery<Long> q = em
                .createQuery(
                        "SELECT COUNT(fieldmappingexternalapptemplate) FROM FieldMappingExternalAppTemplate AS fieldmappingexternalapptemplate WHERE fieldmappingexternalapptemplate.parent = :externalApp AND fieldmappingexternalapptemplate.direction = :direction",
                        Long.class);
        q.setParameter("externalApp", externalApp);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParent(
            ExternalApp parent) {
        if (parent == null)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        return q;
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParentAndDirection(
            ExternalApp parent, Directions direction) {
        if (parent == null)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent AND o.direction = :direction ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        q.setParameter("direction", direction);
        return q;
    }

    public static TypedQuery<FieldMappingExternalAppTemplate> findFieldMappingExternalAppTemplatesByParentAndNameAndDirection(
            ExternalApp parent, String name, Directions direction) {
        if (parent == null)
            throw new IllegalArgumentException(
                    "The parent argument is required");
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        if (direction == null)
            throw new IllegalArgumentException(
                    "The direction argument is required");
        EntityManager em = FieldMappingExternalAppTemplate.entityManager();
        TypedQuery<FieldMappingExternalAppTemplate> q = em
                .createQuery(
                        "SELECT o FROM FieldMappingExternalAppTemplate AS o WHERE o.parent = :parent AND o.name = :name AND o.direction = :direction ORDER BY o.id",
                        FieldMappingExternalAppTemplate.class);
        q.setParameter("parent", parent);
        q.setParameter("name", name);
        q.setParameter("direction", direction);
        return q;
    }
}
