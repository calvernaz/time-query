package org.weirdloop.window;

import org.weirdloop.core.TimeEvent;

import java.util.*;
import java.util.stream.Stream;

/**
 * Holds the query results.
 *
 * @param <T>
 */
public class TimeWindow<T extends TimeEvent> {
    private final List<TimeWindowInterval<T>> intervals;

    public TimeWindow(List<TimeWindowInterval<T>> intervals) {
        this.intervals = intervals;
    }

    public static <T extends TimeEvent> TimeWindow create(List<TimeWindowInterval<T>> intervals) {
        return new TimeWindow<>(intervals);
    }

    public static <T extends TimeEvent> TimeWindow<T> create(TimeWindowInterval<T> timeWindowInterval) {
        return new TimeWindow<>(Collections.singletonList(timeWindowInterval));
    }

    public int size() {
        return intervals.size();
    }

    public Stream<TimeWindowInterval<T>> stream() {
        return intervals.stream();
    }

    public T max() {
        Optional<T> max = stream()
                .map(TimeWindowInterval::max)
                .max(Comparator.naturalOrder());
        if (max.isPresent()) return max.get();
        throw new IllegalArgumentException("No max value found");
    }

    public T min() {
        Optional<T> min = stream()
                .map(TimeWindowInterval::min)
                .max(Comparator.reverseOrder());
        if (min.isPresent()) return min.get();
        throw new IllegalArgumentException("No max value found");
    }

    public T maxBy(Comparator<T> comparator) {
        Optional<T> max = stream()
                .map(twi -> twi.maxBy(comparator))
                .max(Comparator.naturalOrder());
        if (max.isPresent()) return max.get();
        throw new IllegalArgumentException("No max value found");
    }

    public T minBy(Comparator<T> comparator) {
        Optional<T> min = stream()
                .map(twi -> twi.minBy(comparator))
                .min(Comparator.naturalOrder());
        if (min.isPresent()) return min.get();
        throw new IllegalArgumentException("No minimum value found");

    }
}
