package org.weirdloop.query;

import org.junit.Test;
import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindow;
import org.weirdloop.window.TimeWindowInterval;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.weirdloop.core.TimeEvent.create;

public class TimeQueryTest extends AbstractTestData {

    @Test
    public void queryall_and_groupby() {
        TimeWindow<TimeEvent> windows = TimeQuery.create(testdata())
                .groupBy(Buckets.of(5, ChronoUnit.MINUTES))
                .windows();

        assertThat(windows.size()).isEqualTo(20);
    }


    @Test
    public void query_interval_window() {
        Instant to = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);
        Instant from = LocalDateTime.of(2016, 10, 10, 5, 0, 0).toInstant(ZoneOffset.UTC);

        TimeWindow<TimeEvent> timeWindow = TimeQuery.create(ten_thousands_events_with_instant(from, to))
                .interval(Span.openClosed(create(to.minus(4, HOURS)), create(to.minus(2, HOURS))))
                .windows();

        assertThat(timeWindow.size()).isEqualTo(1); // one interval
        long count = timeWindow.stream()
                .map(TimeWindowInterval::events)
                .mapToLong(Collection::size)
                .sum();
        assertThat(count).isEqualTo(120); // number of events in that interval
    }

    @Test
    public void query_down_to_window() {
        Instant hour_ago = Instant.now().minus(1, HOURS);
        TimeWindow<TimeEvent> timeWindow = TimeQuery.create(ten_thousands_events())
                .downTo(Span.downTo(create(hour_ago), BoundType.OPEN))
                .windows();

        assertThat(timeWindow.size()).isEqualTo(1); // only one interval
        int count = timeWindow.stream().mapToInt(TimeWindowInterval::size)
                .reduce(0, (a, b) -> a + b);

        assertThat(count).isBetween(60, 61);
    }

    @Test
    public void query_upto_window() {
        Instant now = Instant.now();
        Instant hourAgo = now.minus(1, HOURS);
        TimeWindow<TimeEvent> timeWindow = TimeQuery.create(ten_thousands_events())
                .upTo(Span.upTo(create(hourAgo), BoundType.CLOSED))
                .windows();

        assertThat(timeWindow.size()).isEqualTo(1); // one interval
        int count = timeWindow.stream()
                .mapToInt(TimeWindowInterval::size)
                .reduce(0, (a, b) -> a + b);
        assertThat(count).isGreaterThanOrEqualTo(9939);
    }

    //select count(customer_id) from events where time > now() -1h groupy by time(1m), customer_id
    @Test
    public void should_create_last_hour_time_window_grouped_by_minute() {
        Instant now = Instant.now();
        Instant minusAnHour = now.minus(1,ChronoUnit.HOURS);
        TimeWindow rangeQuery = TimeQuery.create(every_minute_two_hours_from_now())
                .downTo(Span.closedOpen(TimeEvent.create(minusAnHour), TimeEvent.create(now)))
                .groupBy(Buckets.of(1, ChronoUnit.MINUTES))
                .windows();

        assertThat(rangeQuery.size()).isEqualTo(60);
    }

    @Test
    public void should_create_last_hour_time_window_grouped_by_five_minute_histogram() {
        Instant now = Instant.now();
        Instant minusAnHour = now.minus(1, ChronoUnit.HOURS);
        Map<Integer, Long> histogram = TimeQuery.create(every_minute_two_hours_from_now())
                .downTo(Span.closedOpen(TimeEvent.create(minusAnHour), TimeEvent.create(now)))
                .groupBy(Buckets.of(5, ChronoUnit.MINUTES))
                .histogram();

        assertThat(histogram).hasSize(12);
    }

    private static Collection<TimeEvent> testdata() {
        return amount(100);
    }

    private static Collection<TimeEvent> ten_thousands_events() {
        return amount(10_000);
    }

    private static Collection<TimeEvent> every_minute_two_hours_from_now() {
        return amount(120);
    }

    private static Collection<TimeEvent> ten_thousands_events_with_instant(Instant from, Instant to) {
        return generate(from, to, 1);
    }

}