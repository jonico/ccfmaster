package com.collabnet.ccf.ccfmaster.server.core;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;

public interface LandscapeModificationListener {
    void afterMerge(Landscape landscape);

    Landscape beforeMerge(Landscape landscape);

    void onException(Exception e);
}
