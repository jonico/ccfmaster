package com.collabnet.ccf.ccfmaster.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collabnet.ccf.ccfmaster.server.domain.ConfigItem;

public class MockConfigItemPersisterFactory<T extends ConfigItem, U> implements PersisterFactory<T, U> {

    private static final Logger log = LoggerFactory
                                            .getLogger(MockConfigItemPersisterFactory.class);

    @Override
    public Persister<T> get(U participant) {
        return new Persister<T>() {

            @Override
            public void delete(T cfg) {
                // do nothing.
                log.debug("deleting {} = {}", cfg.getName(), cfg.getVal());
            }

            @Override
            public void save(T cfg) {
                // do nothing.
                log.debug("updating {} = {}", cfg.getName(), cfg.getVal());
            }

        };
    }

}
