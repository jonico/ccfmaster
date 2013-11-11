package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.core.PropertiesConfigItemPersister;

/**
 * a configItem is a key-value pair. Domain classes can implement this to make
 * use of {@link PropertiesConfigItemPersister}
 * 
 * @author ctaylor
 * 
 */
public interface ConfigItem {
    public String getName();

    public String getVal();
}
