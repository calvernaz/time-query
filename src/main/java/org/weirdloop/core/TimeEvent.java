package org.weirdloop.core;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.temporal.Temporal;
import java.util.Objects;

/**
 * Base event used as a reference for the {@link TimeQueue}.
 *
 * @param <K>
 * @param <V>
 */
public class TimeEvent<K extends Temporal & Comparable<K>, V extends Comparable<V>>
        implements Comparable<TimeEvent<K, V>> {

    private final K key;

    private final V value;

    private TimeEvent(K key, V value) {
        Objects.requireNonNull(key);
        this.key = key;
        this.value = value;
    }

    public static <K extends Temporal & Comparable<K>, V extends Comparable<V>> TimeEvent<K, V> create(K key) {
        return new TimeEvent<>(key, null);
    }

    public static <K extends Temporal & Comparable<K>, V extends Comparable<V>> TimeEvent<K, V> create(K key, V value) {
        return new TimeEvent<>(key, value);
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    @Override
    public int compareTo(TimeEvent<K, V> o) {
        return key.compareTo(o.key);
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TimeEvent)) return false;

        TimeEvent<?, ?> timeEvent = (TimeEvent<?, ?>) o;

        return new EqualsBuilder()
                .append(key, timeEvent.key)
                .append(value, timeEvent.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(key)
                .append(value)
                .toHashCode();
    }

}