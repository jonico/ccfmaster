package com.collabnet.ccf.ccfmaster.util;

import java.util.Arrays;
import java.util.List;

public class PartialTransformer<T, U> implements Transformer<T, U> {
    public static class NotApplicableException extends IllegalArgumentException {
        private static final long serialVersionUID = 1L;
        private final Object      arg;

        public NotApplicableException(Object arg) {
            super();
            this.arg = arg;
        }

        public Object getArg() {
            return arg;
        }
    }

    public static interface Part<T, U> extends Transformer<T, U> {
        public boolean appliesTo(T arg);
    }

    private final List<Part<T, U>> parts;

    private PartialTransformer(final List<Part<T, U>> parts) {
        this.parts = parts;
    }

    @Override
    public U transform(T t) {
        for (Part<T, U> part : parts) {
            if (part.appliesTo(t)) {
                return part.transform(t);
            }
        }
        throw new NotApplicableException(t);
    }

    public static <T, U> Transformer<T, U> create(final List<Part<T, U>> parts) {
        return new PartialTransformer<T, U>(parts);
    }

    public static <T, U> Transformer<T, U> create(final Part<T, U>... parts) {
        return new PartialTransformer<T, U>(Arrays.asList(parts));
    }
}
