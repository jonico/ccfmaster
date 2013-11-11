package com.collabnet.ccf.ccfmaster.server.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimezoneTest {
    @Test
    public void allTimezonesFromString() {
        Set<String> tzs = new HashSet<String>(Arrays.asList(TimeZone
                .getAvailableIDs()));
        for (Timezone tz : Timezone.class.getEnumConstants()) {
            String str = tz.toString();
            assertTrue(str, tzs.contains(str));
        }
    }
}
