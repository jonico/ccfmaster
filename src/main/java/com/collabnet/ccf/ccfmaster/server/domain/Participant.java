package com.collabnet.ccf.ccfmaster.server.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.Size;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import javax.persistence.Enumerated;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;

@XmlRootElement
@RooJavaBean
@RooToString
@RooEntity
@XmlAccessorType(XmlAccessType.FIELD)
public class Participant {

    @NotNull
    private String description;

    @NotNull
    @Size(max = 128)
    @Column(unique = true)
    private String systemId;

    @Size(max = 128)
    private String encoding;

    @NotNull
    @Enumerated(EnumType.STRING)
    @XmlJavaTypeAdapter(Timezone.XmlAdapter.class)
    private Timezone timezone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SystemKind systemKind;

    private String prefix;

    public static class XmlAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<Long, Participant> {

        @Override
        public Participant unmarshal(Long v) throws Exception {
            Participant res = findParticipant(v);
            return res;
        }

        @Override
        public Long marshal(Participant v) throws Exception {
            return v.getId();
        }
    }
}
