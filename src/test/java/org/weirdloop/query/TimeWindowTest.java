package org.weirdloop.query;

import org.junit.Test;
import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindow;
import org.weirdloop.window.TimeWindowInterval;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeWindowTest extends AbstractTestData {

    @Test
    public void should_return_the_latest_time_event() {
        Instant s1 = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);
        Instant e1 = LocalDateTime.of(2016, 10, 10, 11, 0, 0).toInstant(ZoneOffset.UTC);
        Instant expected = LocalDateTime.of(2016, 10, 10, 10, 59, 0).toInstant(ZoneOffset.UTC);

        TimeWindowInterval<TimeEvent> tw1 = TimeWindowInterval.create(generate(s1, e1, 1));
        TimeWindow<TimeEvent> tw = new TimeWindow<>(Collections.singletonList(tw1));

        assertThat(tw.max()).isEqualTo(TimeEvent.create(expected));
    }

    @Test
    public void should_return_the_oldest_time_event() {
        Instant s1 = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);
        Instant e1 = LocalDateTime.of(2016, 10, 10, 11, 0, 0).toInstant(ZoneOffset.UTC);
        Instant expected = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);

        TimeWindowInterval<TimeEvent> tw1 = TimeWindowInterval.create(generate(s1, e1, 1));
        TimeWindow tw = new TimeWindow<>(Collections.singletonList(tw1));

        assertThat(tw.min()).isEqualTo(TimeEvent.create(expected));
    }

    @Test
    public void should_return_the_latest_by_value_using_comparator() {
        Instant s1 = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);
        Instant s2 = LocalDateTime.of(2016, 10, 10, 10, 10, 0).toInstant(ZoneOffset.UTC);
        Instant s3 = LocalDateTime.of(2016, 10, 10, 10, 20, 0).toInstant(ZoneOffset.UTC);
        Instant s4 = LocalDateTime.of(2016, 10, 10, 10, 30, 0).toInstant(ZoneOffset.UTC);
        Instant s5 = LocalDateTime.of(2016, 10, 10, 10, 40, 0).toInstant(ZoneOffset.UTC);
        Instant s6 = LocalDateTime.of(2016, 10, 10, 10, 50, 0).toInstant(ZoneOffset.UTC);

        Collection<TimeEvent> elements = new ArrayList<>();
        elements.add(TimeEvent.create(s1, 1));
        elements.add(TimeEvent.create(s2, 3));
        elements.add(TimeEvent.create(s3, 2));

        Collection<TimeEvent> elements2 = new ArrayList<>();
        elements2.add(TimeEvent.create(s4, 6));
        elements2.add(TimeEvent.create(s5, 10));
        elements2.add(TimeEvent.create(s6, 4));

        TimeWindowInterval<TimeEvent> tw1 = TimeWindowInterval.create(elements);
        TimeWindowInterval<TimeEvent> tw2 = TimeWindowInterval.create(elements2);
        TimeWindow window = TimeWindow.create(Arrays.asList(tw1, tw2));

        Comparator<TimeEvent> comparator = Comparator.comparing(TimeEvent::value);
        assertThat(window.maxBy(comparator)).isEqualTo(TimeEvent.create(s5, 10));
    }

    @Test
    public void should_return_the_oldest_by_value_using_comparator() {
        Instant s1 = LocalDateTime.of(2016, 10, 10, 10, 0, 0).toInstant(ZoneOffset.UTC);
        Instant s2 = LocalDateTime.of(2016, 10, 10, 10, 10, 0).toInstant(ZoneOffset.UTC);
        Instant s3 = LocalDateTime.of(2016, 10, 10, 10, 20, 0).toInstant(ZoneOffset.UTC);
        Instant s4 = LocalDateTime.of(2016, 10, 10, 10, 30, 0).toInstant(ZoneOffset.UTC);
        Instant s5 = LocalDateTime.of(2016, 10, 10, 10, 40, 0).toInstant(ZoneOffset.UTC);
        Instant s6 = LocalDateTime.of(2016, 10, 10, 10, 50, 0).toInstant(ZoneOffset.UTC);

        Collection<TimeEvent> elements = new ArrayList<>();
        elements.add(TimeEvent.create(s1, 1));
        elements.add(TimeEvent.create(s2, 3));
        elements.add(TimeEvent.create(s3, 2));

        Collection<TimeEvent> elements2 = new ArrayList<>();
        elements2.add(TimeEvent.create(s4, 6));
        elements2.add(TimeEvent.create(s5, 10));
        elements2.add(TimeEvent.create(s6, 4));

        TimeWindowInterval<TimeEvent> tw1 = TimeWindowInterval.create(elements);
        TimeWindowInterval<TimeEvent> tw2 = TimeWindowInterval.create(elements2);
        TimeWindow window = TimeWindow.create(Arrays.asList(tw1, tw2));

        Comparator<TimeEvent> comparator = Comparator.comparing(TimeEvent::value);
        assertThat(window.minBy(comparator)).isEqualTo(TimeEvent.create(s1, 1));
    }
}