package org.weirdloop.query;

import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindow;
import org.weirdloop.window.TimeWindowInterval;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static org.weirdloop.collector.TimeWindowCollector.toWindowCollector;

/**
 * Time query specialization for grouped queries.
 *
 * @param <T>
 */
public class BucketTimeQuery<T extends TimeEvent> extends TimeQuery<T> {

    public BucketTimeQuery(Collection<T> collection, Buckets<T> bucket, Span<T> span) {
        super(collection, bucket, span);
    }

    @Override
    public TimeWindow<T> windows() {
        return streamSorted()
                .filter(predicate())
                .collect(collectingAndThen(groupingBy(bucket::bucket), Map::values))
                .stream()
                .map(TimeWindowInterval::create)
                .collect(toWindowCollector());
    }
}
