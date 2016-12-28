package org.weirdloop.query;


import org.weirdloop.core.TimeEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AbstractTestData {

    protected static List<TimeEvent> amount(int howMany) {
        return amount(howMany, ChronoUnit.MINUTES);
    }

    protected static List<TimeEvent> amount(int howMany, TemporalUnit temporalUnit) {
        Instant now = Instant.now();
        return IntStream.range(0, howMany)
                .mapToObj(i -> {
                    Instant ldt = now.minus(i, temporalUnit);
                    return TimeEvent.create(ldt);
                }).collect(toList());
    }

    protected static List<TimeEvent> generate(Instant from, Instant to, int interval) {
        return Stream.iterate(from, ldt -> ldt.plus(interval, ChronoUnit.MINUTES))
                .limit((long) (Math.ceil(ChronoUnit.MINUTES.between(from, to) / interval)))
                .map(TimeEvent::create)
                .collect(toList());
    }
}
