// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import java.lang.String;

privileged aspect Participant_Roo_ToString {
    
    public String Participant.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Encoding: ").append(getEncoding()).append(", ");
        sb.append("SystemId: ").append(getSystemId()).append(", ");
        sb.append("SystemKind: ").append(getSystemKind()).append(", ");
        sb.append("Timezone: ").append(getTimezone());
        return sb.toString();
    }
    
}
