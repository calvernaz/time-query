package org.weirdloop.query;

import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindow;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.weirdloop.collector.SingleTimeWindowCollector.toSingleWindowCollector;

/**
 * Base class for time queries.
 *
 * @param <T>
 */
public class TimeQuery<T extends TimeEvent> {
    private static final Predicate DEFAULT_PRED = x -> true;

    private final Collection<T> collection;
    final Buckets<T> bucket;
    final Span<T> span;

    private TimeQuery(Collection<T> collection) {
        this(collection, null, null);
    }

    protected TimeQuery(Collection<T> collection, Buckets<T> bucket, Span<T> span) {
        this.collection = collection;
        this.bucket = bucket;
        this.span = span;
    }

    public static <T extends TimeEvent> TimeQuery<T> create(Collection<T> collection) {
        return new TimeQuery<>(collection);
    }

    public BucketTimeQuery<T> groupBy(Buckets<T> bucket) {
        return new BucketTimeQuery<>(collection, bucket, span);
    }

    public TimeQuery<T> interval(Span<T> span) {
        return new TimeQuery<>(collection, bucket, span);
    }

    public TimeQuery<T> downTo(Span<T> span) {
        return new TimeQuery<>(collection, bucket, span);
    }

    public TimeQuery<T> upTo(Span<T> span) {
        return new TimeQuery<>(collection, bucket, span);
    }

    public TimeWindow<T> windows() {
        return streamSorted()
                .filter(predicate())
                .collect(toSingleWindowCollector());
    }

    protected Stream<T> streamSorted() {
        return collection.stream().sorted();
    }

    public Map<Integer, Long> histogram() {
        return streamSorted()
                .filter(predicate())
                .collect(groupingBy(bucket::bucket, counting()));
    }

    // TODO
    protected Predicate<T> predicate() {
        return span == null ? DEFAULT_PRED:
                span::contains;
    }
}
