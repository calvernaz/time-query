package org.weirdloop.collector;

import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindowInterval;
import org.weirdloop.window.TimeWindow;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * The {@link TimeWindow} collector is used to collect {@link TimeWindowInterval} instances,
 * created by the group by query.
 *
 * @param <T>
 */
public class TimeWindowCollector<T extends TimeEvent> implements
        Collector<TimeWindowInterval<T>, List<TimeWindowInterval<T>>, TimeWindow<T>> {

    public static <T extends TimeEvent> TimeWindowCollector<T> toWindowCollector() {
        return new TimeWindowCollector<>();
    }

    @Override
    public Supplier<List<TimeWindowInterval<T>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<TimeWindowInterval<T>>, TimeWindowInterval<T>> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<TimeWindowInterval<T>>> combiner() {
        return (acc, t) -> {
            acc.addAll(t);
            return acc;
        };
    }

    @Override
    public Function<List<TimeWindowInterval<T>>, TimeWindow<T>> finisher() {
        return TimeWindow::create;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }

}