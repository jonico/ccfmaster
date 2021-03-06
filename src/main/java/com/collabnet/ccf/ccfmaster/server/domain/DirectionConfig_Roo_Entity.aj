// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
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

privileged aspect DirectionConfig_Roo_Entity {
    
    declare @type: DirectionConfig: @Entity;
    
    @PersistenceContext
    transient EntityManager DirectionConfig.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long DirectionConfig.id;
    
    @Version
    @Column(name = "version")
    private Integer DirectionConfig.version;
    
    public Long DirectionConfig.getId() {
        return this.id;
    }
    
    public void DirectionConfig.setId(Long id) {
        this.id = id;
    }
    
    public Integer DirectionConfig.getVersion() {
        return this.version;
    }
    
    public void DirectionConfig.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void DirectionConfig.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void DirectionConfig.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            DirectionConfig attached = DirectionConfig.findDirectionConfig(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void DirectionConfig.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void DirectionConfig.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public DirectionConfig DirectionConfig.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DirectionConfig merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager DirectionConfig.entityManager() {
        EntityManager em = new DirectionConfig().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long DirectionConfig.countDirectionConfigs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DirectionConfig o", Long.class).getSingleResult();
    }
    
    public static List<DirectionConfig> DirectionConfig.findAllDirectionConfigs() {
        return entityManager().createQuery("SELECT o FROM DirectionConfig o", DirectionConfig.class).getResultList();
    }
    
    public static DirectionConfig DirectionConfig.findDirectionConfig(Long id) {
        if (id == null) return null;
        return entityManager().find(DirectionConfig.class, id);
    }
    
    public static List<DirectionConfig> DirectionConfig.findDirectionConfigEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DirectionConfig o", DirectionConfig.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
