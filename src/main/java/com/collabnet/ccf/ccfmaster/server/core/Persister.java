package com.collabnet.ccf.ccfmaster.server.core;

/**
 * All methods in this class should throw {@link CoreConfigurationException} if
 * something goes wrong.
 * 
 * @author ctaylor
 * 
 * @param <T>
 */
public interface Persister<T> {
    public void delete(T cfg);

    public void save(T cfg);
}
