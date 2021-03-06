// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect RepositoryMappingDirectionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RepositoryMappingDirectionDataOnDemand: @Component;
    
    private Random RepositoryMappingDirectionDataOnDemand.rnd = new java.security.SecureRandom();
    
    @Autowired
    private FieldMappingDataOnDemand RepositoryMappingDirectionDataOnDemand.fieldMappingDataOnDemand;
    
    private void RepositoryMappingDirectionDataOnDemand.setDirection(RepositoryMappingDirection obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Directions direction = com.collabnet.ccf.ccfmaster.server.domain.Directions.class.getEnumConstants()[0];
        obj.setDirection(direction);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setRepositoryMapping(RepositoryMappingDirection obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping repositoryMapping = repositoryMappingDataOnDemand.getRandomRepositoryMapping();
        obj.setRepositoryMapping(repositoryMapping);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setStatus(RepositoryMappingDirection obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus status = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionStatus.class.getEnumConstants()[0];
        obj.setStatus(status);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setLastSourceArtifactModificationDate(RepositoryMappingDirection obj, int index) {
        java.util.Date lastSourceArtifactModificationDate = new java.util.GregorianCalendar(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR), java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE), java.util.Calendar.getInstance().get(java.util.Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setLastSourceArtifactModificationDate(lastSourceArtifactModificationDate);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setLastSourceArtifactVersion(RepositoryMappingDirection obj, int index) {
        java.lang.String lastSourceArtifactVersion = "lastSourceArtifactVersion_" + index;
        if (lastSourceArtifactVersion.length() > 128) {
            lastSourceArtifactVersion = lastSourceArtifactVersion.substring(0, 128);
        }
        obj.setLastSourceArtifactVersion(lastSourceArtifactVersion);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setLastSourceArtifactId(RepositoryMappingDirection obj, int index) {
        java.lang.String lastSourceArtifactId = "lastSourceArtifactId_" + index;
        if (lastSourceArtifactId.length() > 128) {
            lastSourceArtifactId = lastSourceArtifactId.substring(0, 128);
        }
        obj.setLastSourceArtifactId(lastSourceArtifactId);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setConflictResolutionPolicy(RepositoryMappingDirection obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy conflictResolutionPolicy = com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy.class.getEnumConstants()[0];
        obj.setConflictResolutionPolicy(conflictResolutionPolicy);
    }
    
    private void RepositoryMappingDirectionDataOnDemand.setActiveFieldMapping(RepositoryMappingDirection obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping activeFieldMapping = fieldMappingDataOnDemand.getSpecificFieldMapping(index);
        obj.setActiveFieldMapping(activeFieldMapping);
    }
    
    public RepositoryMappingDirection RepositoryMappingDirectionDataOnDemand.getSpecificRepositoryMappingDirection(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        RepositoryMappingDirection obj = data.get(index);
        return RepositoryMappingDirection.findRepositoryMappingDirection(obj.getId());
    }
    
    public RepositoryMappingDirection RepositoryMappingDirectionDataOnDemand.getRandomRepositoryMappingDirection() {
        init();
        RepositoryMappingDirection obj = data.get(rnd.nextInt(data.size()));
        return RepositoryMappingDirection.findRepositoryMappingDirection(obj.getId());
    }
    
    public boolean RepositoryMappingDirectionDataOnDemand.modifyRepositoryMappingDirection(RepositoryMappingDirection obj) {
        return false;
    }
    
}
