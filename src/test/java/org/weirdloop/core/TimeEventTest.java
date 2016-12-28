package org.weirdloop.core;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeEventTest {

    @Test(expected = NullPointerException.class)
    public void given_null_inputs_should_throw_exceptions() {
        TimeEvent.create(null, null);
    }

    @Test
    public void should_key_be_instanceof_comparable() {
        TimeEvent<?, ?> evt = TimeEvent.create(Instant.now());
        assertThat(evt.key()).isInstanceOf(Comparable.class);
    }

    @Test
    public void should_value_be_anything() {
            TimeEvent<?, ?> evt = TimeEvent.create(LocalDateTime.now(), "");
        assertThat(evt.value()).isInstanceOf(String.class);
    }

    @Test
    public void should_key_use_chronological_order() {
        TimeEvent<Instant, Integer> evt1 = TimeEvent.create(Instant.now().minus(1, ChronoUnit.MINUTES));
        TimeEvent<Instant, Integer> evt2 = TimeEvent.create(Instant.now());
        assertThat(evt1.compareTo(evt2)).isLessThan(0);
    }

    @Test
    public void should_key_be_parseable() {
        TimeEvent<Instant, ?> evt1 = TimeEvent.create(Instant.now().minus(1, ChronoUnit.MINUTES));
        assertThat(Instant.parse(evt1.toString())).isNotNull();
    }
}