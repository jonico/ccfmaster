package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public interface LandscapeCreationListener {
    void afterCreate(Landscape landscape);

    Landscape beforeCreate(Landscape landscape);

    void onException(Exception e);
}
