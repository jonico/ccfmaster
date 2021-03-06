// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FieldMapping_Roo_Entity {
    
    declare @type: FieldMapping: @Entity;
    
    @PersistenceContext
    transient EntityManager FieldMapping.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long FieldMapping.id;
    
    @Version
    @Column(name = "version")
    private Integer FieldMapping.version;
    
    public Long FieldMapping.getId() {
        return this.id;
    }
    
    public void FieldMapping.setId(Long id) {
        this.id = id;
    }
    
    public Integer FieldMapping.getVersion() {
        return this.version;
    }
    
    public void FieldMapping.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void FieldMapping.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void FieldMapping.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FieldMapping attached = FieldMapping.findFieldMapping(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void FieldMapping.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void FieldMapping.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public FieldMapping FieldMapping.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FieldMapping merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager FieldMapping.entityManager() {
        EntityManager em = new FieldMapping().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long FieldMapping.countFieldMappings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FieldMapping o", Long.class).getSingleResult();
    }
    
    public static List<FieldMapping> FieldMapping.findAllFieldMappings() {
        return entityManager().createQuery("SELECT o FROM FieldMapping o", FieldMapping.class).getResultList();
    }
    
    public static FieldMapping FieldMapping.findFieldMapping(Long id) {
        if (id == null) return null;
        return entityManager().find(FieldMapping.class, id);
    }
    
    public static List<FieldMapping> FieldMapping.findFieldMappingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FieldMapping o", FieldMapping.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
