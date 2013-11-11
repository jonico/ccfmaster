package com.collabnet.ccf.ccfmaster.server.core;

/**
 * produces {@link Persister}s that can persist entities of type T.
 * 
 * Concrete implementations can use the OwningEntity do decide where/how to
 * persist the T.
 * 
 * @author ctaylor
 * 
 * @param <T>
 *            the type of the entity that will be persisted by the produced
 *            {@link Persister}.
 * @param <OwningEntity>
 */
public interface PersisterFactory<T, OwningEntity> {
    public Persister<T> get(OwningEntity participant);
}
