package org.weirdloop.window;

import org.weirdloop.core.TimeEvent;

import java.util.*;

/**
 * Time window interval. They're rather useful for group by queries.
 *
 * @param <T>
 */
public class TimeWindowInterval<T extends TimeEvent> {
    private final Collection<T> elements;

    private TimeWindowInterval(Collection<T> elements) {
        this.elements = elements;
    }

    public static <T extends TimeEvent> TimeWindowInterval<T> create(Collection<T> elements) {
        return new TimeWindowInterval<>(elements);
    }

    public static <T extends TimeEvent> TimeWindowInterval<T> create() {
        return new TimeWindowInterval<>(new ArrayList<>());
    }

    public Collection<T> events() {
        return Collections.unmodifiableCollection(elements);
    }

    public T max() {
        Optional<T> max = elements.stream().max(Comparator.naturalOrder());
        if (max.isPresent()) return max.get();
        throw new IllegalArgumentException("No maximum found");
    }

    public T min() {
        Optional<T> min = elements.stream().min(Comparator.naturalOrder());
        if (min.isPresent()) return min.get();
        throw new IllegalArgumentException("No maximum found");
    }

    public T maxBy(Comparator<? super T> comparator) {
        Optional<T> max = elements.stream().max(comparator);
        if (max.isPresent()) return max.get();
        throw new IllegalArgumentException("No maximum found");
    }

    public T minBy(Comparator<? super T> comparator) {
        Optional<T> min = elements.stream().min(comparator);
        if (min.isPresent()) return min.get();
        throw new IllegalArgumentException("No minimum found");
    }

    public static <T extends TimeEvent> void add(TimeWindowInterval<T> windowInterval, T timeEvent) {
        windowInterval.add(timeEvent);
    }

    private void add(T timeEvent) {
        elements.add(timeEvent);
    }

    public void concat(TimeWindowInterval<T> windowInterval) {
        elements.addAll(windowInterval.events());
    }

    public int size() {
        return elements.size();
    }
}
