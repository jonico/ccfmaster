package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;

@RooDataOnDemand(entity = HospitalEntry.class)
public class HospitalEntryDataOnDemand {

    public boolean modifyHospitalEntry(HospitalEntry obj) {
        obj.setErrorCode("changed");
        obj.setOriginatingComponent("changed");
        return true;
    }
}
