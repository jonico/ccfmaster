package com.collabnet.ccf.ccfmaster.server.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

@XmlRootElement(name="fieldMappingTemplate")
@RooJavaBean
@RooToString
@XmlAccessorType(XmlAccessType.FIELD)
@RooEntity(finders = { "findFieldMappingLandscapeTemplatesByParentAndNameAndDirection", "findFieldMappingLandscapeTemplatesByParent", "findFieldMappingLandscapeTemplatesByParentAndDirection", "findFieldMappingLandscapeTemplatesByDirection","countFieldMappingLandscapeTemplatesByDirection"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "PARENT", "NAME", "DIRECTION" }))
public class FieldMappingLandscapeTemplate implements Template<Landscape> {

    @NotNull
    @ManyToOne(cascade = {  })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @XmlJavaTypeAdapter(Landscape.XmlAdapter.class)
    private Landscape parent;

    @NotNull
    @Pattern(regexp = "[\\w\\s]+")
    private String name;

    @NotNull
    @Enumerated
    private Directions direction;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldMappingKind kind;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingRule> rules = new ArrayList<FieldMappingRule>();

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<FieldMappingValueMap> valueMaps = new ArrayList<FieldMappingValueMap>();

    @Override
    public File getStorageDirectory(File baseDir) {
        Landscape landscape = getParent();
        final File dir = new File(baseDir, String.format("landscape%d/fieldmappings/%s/landscape/%s", landscape.getId(), getDirection(), getName()));
        return dir;
    }
    
    public static long countFieldMappingLandscapeTemplatesByDirection(Directions direction) {
    	if (direction == null) throw new IllegalArgumentException("The direction argument is required");
    	TypedQuery<Long> q = entityManager().createQuery("SELECT COUNT(o) FROM FieldMappingLandscapeTemplate o WHERE o.direction = :direction", Long.class);
        q.setParameter("direction", direction);
        return q.getSingleResult();
    }
    
}
