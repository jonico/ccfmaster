// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import java.lang.String;

privileged aspect Participant_Roo_JavaBean {
    
    public String Participant.getDescription() {
        return this.description;
    }
    
    public void Participant.setDescription(String description) {
        this.description = description;
    }
    
    public String Participant.getSystemId() {
        return this.systemId;
    }
    
    public void Participant.setSystemId(String systemId) {
        this.systemId = systemId;
    }
    
    public String Participant.getEncoding() {
        return this.encoding;
    }
    
    public void Participant.setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public Timezone Participant.getTimezone() {
        return this.timezone;
    }
    
    public void Participant.setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }
    
    public SystemKind Participant.getSystemKind() {
        return this.systemKind;
    }
    
    public void Participant.setSystemKind(SystemKind systemKind) {
        this.systemKind = systemKind;
    }
    
    public String Participant.getPrefix() {
        return this.prefix;
    }
    
    public void Participant.setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
}
