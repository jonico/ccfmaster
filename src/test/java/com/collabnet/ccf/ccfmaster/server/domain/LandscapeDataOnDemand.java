package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Landscape.class)
public class LandscapeDataOnDemand {

    @Autowired
    private ParticipantDataOnDemand participantDataOnDemand;

    public Landscape getNewTransientLandscape(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = new com.collabnet.ccf.ccfmaster.server.domain.Landscape();
        obj.setName("description_" + index);
        obj.setTeamForge(participantDataOnDemand
                .getSpecificParticipant(index % 10));
        obj.setParticipant(participantDataOnDemand
                .getSpecificParticipant((index + 1) % 10));
        obj.setPlugId("plug" + index);
        return obj;
    }

    public boolean modifyLandscape(Landscape obj) {
        obj.setName(obj.getName() + " changed again");
        return true;
    }
}
