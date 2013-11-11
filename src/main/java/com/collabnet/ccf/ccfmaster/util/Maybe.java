package com.collabnet.ccf.ccfmaster.util;

import java.util.Iterator;

public abstract class Maybe<T> implements Iterable<T> {

    static final class None<T> extends Maybe<T> {

        @Override
        public boolean equals(Object other) {
            return (other != null && other instanceof None);
        }

        @Override
        public T get() {
            throw new IllegalArgumentException("called get() on None");
        }

        @Override
        public T getOrElse(T defaultVal) {
            return defaultVal;
        }

        @Override
        public <E extends Exception> T getOrThrow(E exception) throws E {
            throw exception;
        }

        @Override
        public int hashCode() {
            return 23;
        }

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new IllegalStateException("None has no values");
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    static final class Some<T> extends Maybe<T> {

        private T value;

        public Some(T val) {
            this.value = val;
        }

        @Override
        public boolean equals(Object other) {

            if (other != null && other instanceof Some) {
                final Some<?> someOther = (Some<?>) other;
                if (this.value == null) {
                    return someOther.value == null;
                } else {
                    return this.value.equals(someOther.value);
                }
            }
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T getOrElse(T defaultVal) {
            return value;
        }

        @Override
        public <E extends Exception> T getOrThrow(E exception) throws E {
            return get();
        }

        @Override
        public int hashCode() {
            return 7 * ((value != null) ? value.hashCode() : 23);
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {

                boolean hasNext = true;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public T next() {
                    if (hasNext) {
                        hasNext = false;
                        return value;
                    } else {
                        throw new IllegalStateException(
                                "can only call next() once");
                    }
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

            };
        }
    }

    public abstract T get();

    public abstract T getOrElse(T defaultVal);;

    public abstract <E extends Exception> T getOrThrow(E exception) throws E;

    public boolean isNone() {
        return !isSome();
    }

    public abstract boolean isSome();

    public <U> Maybe<U> map(Transformer<T, U> fun) {
        for (T val : this) {
            return maybe(fun.transform(val));
        }
        return none();
    }

    public static <T> Maybe<T> maybe(T val) {
        if (val == null) {
            return Maybe.none();
        } else {
            return Maybe.some(val);
        }
    }

    public static <T> Maybe<T> none() {
        return new None<T>();
    }

    public static <T> Maybe<T> some(T val) {
        return new Some<T>(val);
    }

}
