package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;

@RooDataOnDemand(entity = RepositoryMappingDirection.class)
public class RepositoryMappingDirectionDataOnDemand {

	private List<RepositoryMappingDirection> data;

	public void init() {
        data = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection.findRepositoryMappingDirectionEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'RepositoryMappingDirection' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection>();
        // only use two repository mapping directions due to unique key constraints for enum
        for (int i = 0; i < 2; i++) {
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = getNewTransientRepositoryMappingDirection(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }

	@Autowired
    private RepositoryMappingDataOnDemand repositoryMappingDataOnDemand;

	public RepositoryMappingDirection getNewTransientRepositoryMappingDirection(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = new com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection();
        obj.setConflictResolutionPolicy(com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy.class.getEnumConstants()[0]);
        obj.setDirection(com.collabnet.ccf.ccfmaster.server.domain.Directions.class.getEnumConstants()[index % 2]);
        java.lang.String lastSourceArtifactId = "lastSourceArtifactId_" + index;
        if (lastSourceArtifactId.length() > 128) {
            lastSourceArtifactId  = lastSourceArtifactId.substring(0, 128);
        }
        obj.setLastSourceArtifactId(lastSourceArtifactId);
        obj.setLastSourceArtifactModificationDate(new java.util.Date());
        java.lang.String lastSourceArtifactVersion = "lastSourceArtifactVersion_" + index;
        if (lastSourceArtifactVersion.length() > 128) {
            lastSourceArtifactVersion  = lastSourceArtifactVersion.substring(0, 128);
        }
        obj.setLastSourceArtifactVersion(lastSourceArtifactVersion);
        obj.setRepositoryMapping(repositoryMappingDataOnDemand.getRandomRepositoryMapping());
        return obj;
    }
}
