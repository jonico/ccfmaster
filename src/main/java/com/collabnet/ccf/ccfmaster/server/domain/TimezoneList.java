package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.collect.ForwardingList;

@XmlRootElement
public class TimezoneList extends ForwardingList<Timezone> {

    private List<Timezone> timezone;

    public TimezoneList() {
        this(new ArrayList<Timezone>());
    }

    public TimezoneList(List<Timezone> timezones) {
        this.setTimezone(timezones);
    }

    @XmlJavaTypeAdapter(Timezone.XmlAdapter.class)
    public List<Timezone> getTimezone() {
        return timezone;
    }

    public void setTimezone(List<Timezone> timezone) {
        this.timezone = timezone;
    }

    @Override
    protected List<Timezone> delegate() {
        return getTimezone();
    }

}
