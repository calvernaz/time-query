package org.weirdloop.query;

import org.junit.Test;
import org.weirdloop.core.TimeEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;


public class SpanTest {


    @Test
    public void test() {
        LocalDateTime anHourAgo = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
        TimeEvent timeEvent = TimeEvent.create(anHourAgo.toInstant(ZoneOffset.UTC));
        Span span = Span.downTo(timeEvent, BoundType.CLOSED);

        LocalDateTime twoHoursAgoLd = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
        Instant twoHoursAgo = twoHoursAgoLd.toInstant(ZoneOffset.UTC);
        boolean contains = span.contains(TimeEvent.create(twoHoursAgo));
        assertThat(contains).isFalse();

        LocalDateTime minus = LocalDateTime.now().minus(30, ChronoUnit.MINUTES);
        Instant instant1 = minus.toInstant(ZoneOffset.UTC);

        boolean contains1 = span.contains(TimeEvent.create(instant1));
        assertThat(contains1).isTrue();

        Span timeEventSpan = Span.closedOpen(TimeEvent.create(twoHoursAgo), TimeEvent.create(anHourAgo.toInstant(ZoneOffset.UTC)));
        LocalDateTime hourAnHalfBack = LocalDateTime.now().minus(90, ChronoUnit.MINUTES);

        assertThat(timeEventSpan.contains(TimeEvent.create(hourAnHalfBack.toInstant(ZoneOffset.UTC
        )))).isTrue();


        assertThat(timeEventSpan.contains(TimeEvent.create(twoHoursAgo))).isTrue();
        assertThat(timeEventSpan.contains(TimeEvent.create(anHourAgo.toInstant(ZoneOffset.UTC)))).isFalse();


    }
}