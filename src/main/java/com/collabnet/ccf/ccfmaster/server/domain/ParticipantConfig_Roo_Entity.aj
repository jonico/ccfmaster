// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
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

privileged aspect ParticipantConfig_Roo_Entity {
    
    declare @type: ParticipantConfig: @Entity;
    
    @PersistenceContext
    transient EntityManager ParticipantConfig.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ParticipantConfig.id;
    
    @Version
    @Column(name = "version")
    private Integer ParticipantConfig.version;
    
    public Long ParticipantConfig.getId() {
        return this.id;
    }
    
    public void ParticipantConfig.setId(Long id) {
        this.id = id;
    }
    
    public Integer ParticipantConfig.getVersion() {
        return this.version;
    }
    
    public void ParticipantConfig.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void ParticipantConfig.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ParticipantConfig.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ParticipantConfig attached = ParticipantConfig.findParticipantConfig(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ParticipantConfig.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ParticipantConfig.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ParticipantConfig ParticipantConfig.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ParticipantConfig merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager ParticipantConfig.entityManager() {
        EntityManager em = new ParticipantConfig().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ParticipantConfig.countParticipantConfigs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ParticipantConfig o", Long.class).getSingleResult();
    }
    
    public static List<ParticipantConfig> ParticipantConfig.findAllParticipantConfigs() {
        return entityManager().createQuery("SELECT o FROM ParticipantConfig o", ParticipantConfig.class).getResultList();
    }
    
    public static ParticipantConfig ParticipantConfig.findParticipantConfig(Long id) {
        if (id == null) return null;
        return entityManager().find(ParticipantConfig.class, id);
    }
    
    public static List<ParticipantConfig> ParticipantConfig.findParticipantConfigEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ParticipantConfig o", ParticipantConfig.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
