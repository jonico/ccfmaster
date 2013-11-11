package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.core.ConfigSavingAspect;
import com.collabnet.ccf.ccfmaster.server.core.Persister;

/**
 * this interface specifies the methods a properly written aspect would need to
 * enable an entity to be persisted.
 * 
 * Currently every entity is special-cased in {@link ConfigSavingAspect} because
 * generics and type erasure got in the way.
 * 
 * @author ctaylor
 * 
 * @param <T>
 */
public interface PersistableConfigItem<T> extends ConfigItem {
    public Persister<T> getPersister();

    public PersistableConfigItem<T> merge();

    public void persist();

    public void remove();
}
