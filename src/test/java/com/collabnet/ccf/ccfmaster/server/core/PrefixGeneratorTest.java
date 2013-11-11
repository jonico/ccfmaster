package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PrefixGeneratorTest {

    static List<Character> valid = Arrays.asList('a', 'b', 'e', 'f');

    @Test
    public void alwaysStartsWithABEF() {
        for (int i = 0; i < 0x10; i++) {
            int val = i | 0xa;
            String s = String.format("%01x", val);
            final char firstChar = s.charAt(0);
            if (!valid.contains(firstChar)) {
                fail("bad firstChar (" + firstChar + ") for i=" + i
                        + ", val = " + val + ".");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixForLessThanZeroThrows() {
        CreateIntegratedAppStrategy.prefixFor(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void prefixForTooLargeThrows() {
        CreateIntegratedAppStrategy.prefixFor(0x1000000);
    }

    @Test
    public void testPrefixFor() {
        // vary the first digit
        for (int i = 0; i < 0x1000000; i += 0x100000) {
            // vary the last digit
            for (int j = 0; j < 0x10; j += 0x1) {
                final int num = i + j;
                final String prefix = CreateIntegratedAppStrategy
                        .prefixFor(num);
                verify(num, prefix);
            }
        }
    }

    static void verify(int i, final String prefix) {
        assertEquals(6, prefix.length());
        final char firstChar = prefix.charAt(0);
        if (!valid.contains(firstChar)) {
            fail("bad firstChar (" + firstChar + ") for num=" + i
                    + ", prefix = " + prefix + ".");
        }
    }
}
