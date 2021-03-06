// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
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

privileged aspect Direction_Roo_Entity {
    
    declare @type: Direction: @Entity;
    
    @PersistenceContext
    transient EntityManager Direction.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Direction.id;
    
    @Version
    @Column(name = "version")
    private Integer Direction.version;
    
    public Long Direction.getId() {
        return this.id;
    }
    
    public void Direction.setId(Long id) {
        this.id = id;
    }
    
    public Integer Direction.getVersion() {
        return this.version;
    }
    
    public void Direction.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void Direction.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Direction.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Direction attached = Direction.findDirection(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Direction.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Direction.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Direction Direction.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Direction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager Direction.entityManager() {
        EntityManager em = new Direction().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Direction.countDirections() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Direction o", Long.class).getSingleResult();
    }
    
    public static List<Direction> Direction.findAllDirections() {
        return entityManager().createQuery("SELECT o FROM Direction o", Direction.class).getResultList();
    }
    
    public static Direction Direction.findDirection(Long id) {
        if (id == null) return null;
        return entityManager().find(Direction.class, id);
    }
    
    public static List<Direction> Direction.findDirectionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Direction o", Direction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
