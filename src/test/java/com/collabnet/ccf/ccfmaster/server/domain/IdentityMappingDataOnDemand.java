package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Date;

import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;

@RooDataOnDemand(entity = IdentityMapping.class)
public class IdentityMappingDataOnDemand {

	public boolean modifyIdentityMapping(IdentityMapping obj) {
		obj.setSourceArtifactVersion("234");
		obj.setTargetArtifactVersion("323");
		obj.setSourceLastModificationTime(new Date());
        return true;
    }
}
