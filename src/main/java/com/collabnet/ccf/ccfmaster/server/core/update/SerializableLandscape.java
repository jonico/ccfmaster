package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.Serializable;
import java.util.List;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.google.common.base.Preconditions;

public final class SerializableLandscape implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String      name;

    /**
     * Dig around in the database to find the participant. Default constructor
     * is necessary to make Spring happy.
     */
    public SerializableLandscape() {
        List<Landscape> landscapes = Landscape.findAllLandscapes();
        Preconditions.checkState(!landscapes.isEmpty(),
                "no landscapes in database");
        this.name = landscapes.get(0).getName();
    }

    public SerializableLandscape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
