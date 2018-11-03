// Pair.java
//
// By Sebastian Raaphorst, 2018.

package com.vorpal.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Simple implementation of a pair of two values.
 * @param <T> type of the first value
 * @param <U> type of the second value
 */
public class Pair<T,U> implements Serializable {
    final public T first;
    final public U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
