/**
 * Pair.java
 *
 * By Sebastian Raaphorst, 2018.
 *
 * Simple implementation of a pair.
 */

package com.vorpal.utils;

import java.io.Serializable;

public class Pair<T,U> implements Serializable {
    public T first;
    public U second;

    public Pair() {}

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }
}
