package com.collabnet.ccf.ccfmaster.util;

import org.junit.Test;

import static com.collabnet.ccf.ccfmaster.util.PartialTransformer.Part;
import static org.junit.Assert.*;

public class PartialTransformerTest {
    final Part<Integer, Integer> positivePlus1  = new Part<Integer, Integer>() {
                                                    @Override
                                                    public boolean appliesTo(
                                                            Integer arg) {
                                                        return arg > 0;
                                                    }

                                                    @Override
                                                    public Integer transform(
                                                            Integer t) {
                                                        return 1 + t;
                                                    }

                                                };
    final Part<Integer, Integer> negativeMinus1 = new Part<Integer, Integer>() {
                                                    @Override
                                                    public boolean appliesTo(
                                                            Integer arg) {
                                                        return arg < 0;
                                                    }

                                                    @Override
                                                    public Integer transform(
                                                            Integer t) {
                                                        return t - 1;
                                                    }

                                                };

    @Test(expected = PartialTransformer.NotApplicableException.class)
    public void noApplicablePartsThrows() {
        final Part<Integer, Integer> part = new Part<Integer, Integer>() {
            @Override
            public boolean appliesTo(Integer arg) {
                return false;
            }

            @Override
            public Integer transform(Integer t) {
                return null;
            }

        };
        @SuppressWarnings("unchecked")
        Transformer<Integer, Integer> transformer = PartialTransformer
                .create(part);
        Integer n = transformer.transform(1);
        fail("transformer returned " + n);
    }

    @Test(expected = PartialTransformer.NotApplicableException.class)
    public void noPartsThrows() {
        @SuppressWarnings("unchecked")
        final Part<Integer, Integer>[] parts = new PartialTransformer.Part[0];
        Transformer<Integer, Integer> transformer = PartialTransformer
                .create(parts);
        transformer.transform(1);
    }

    @Test
    public void showUsage() {
        @SuppressWarnings("unchecked")
        Transformer<Integer, Integer> transformer = PartialTransformer.create(
                positivePlus1, negativeMinus1);
        Integer n = transformer.transform(1);
        assertEquals(Integer.valueOf(2), n);
        n = transformer.transform(-1);
        assertEquals(Integer.valueOf(-2), n);
        final Integer zero = Integer.valueOf(0);
        try {
            n = transformer.transform(zero);
            fail("transformer returned " + n + " for argument=0");
        } catch (PartialTransformer.NotApplicableException e) {
            // exception expected, do nothing.
            assertSame(zero, e.getArg());
        }
    }
}
