// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import java.lang.String;

privileged aspect FieldMappingValueMapEntry_Roo_ToString {
    
    public String FieldMappingValueMapEntry.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Source: ").append(getSource()).append(", ");
        sb.append("Target: ").append(getTarget());
        return sb.toString();
    }
    
}
