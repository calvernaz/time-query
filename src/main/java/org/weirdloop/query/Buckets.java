package org.weirdloop.query;

import org.weirdloop.core.TimeEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import static java.time.Duration.between;

/**
 * Holds time buckets necessary for grouped queries.
 *
 * @param <T>
 */
public class Buckets<T extends TimeEvent> {
    private final Duration duration;
    private Temporal baseTime;

    private Buckets(int amount, ChronoUnit unit) {
        this.duration = Duration.of(amount, unit);
    }

    public static <T extends TimeEvent> Buckets<T> of(int amount, ChronoUnit unit) {
        return new Buckets<>(amount, unit);
    }

    public int bucket(T t) {
        if (this.baseTime == null)
            this.baseTime = t.key();
        return Math.toIntExact(
                between(baseTime, t.key()).getSeconds() / duration.getSeconds()
        );
    }
}
