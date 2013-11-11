package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Random;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;

@RooDataOnDemand(entity = Participant.class)
public class ParticipantDataOnDemand {

    private Random rnd = new java.security.SecureRandom();

    public Participant getNewTransientParticipant(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = new com.collabnet.ccf.ccfmaster.server.domain.Participant();
        obj.setDescription("description_" + index);
        java.lang.String systemId = "systemId_" + index;
        if (systemId.length() > 128) {
            systemId = systemId.substring(0, 128);
        }
        obj.setSystemId(systemId);
        SystemKind systemKind = (index % 2 == 0) ? SystemKind.TF
                : SystemKind.SWP;

        obj.setSystemKind(systemKind);
        java.lang.String encoding = "encoding_" + index;
        if (encoding.length() > 128) {
            encoding = encoding.substring(0, 128);
        }
        obj.setEncoding(encoding);
        obj.setTimezone(Timezone.class.getEnumConstants()[rnd
                .nextInt(Timezone.class.getEnumConstants().length)]);
        return obj;
    }
}
