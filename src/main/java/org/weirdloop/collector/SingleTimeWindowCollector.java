package org.weirdloop.collector;

import org.weirdloop.core.TimeEvent;
import org.weirdloop.window.TimeWindow;
import org.weirdloop.window.TimeWindowInterval;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Single {@link TimeWindow} collector, used to collect events not grouped.
 *
 * @param <T>
 */
public class SingleTimeWindowCollector<T extends TimeEvent>
        implements Collector<T, TimeWindowInterval<T>, TimeWindow<T>> {

    public static <T extends TimeEvent> Collector<T, TimeWindowInterval<T>, TimeWindow<T>> toSingleWindowCollector() {
        return new SingleTimeWindowCollector<>();
    }

    @Override
    public Supplier<TimeWindowInterval<T>> supplier() {
        return TimeWindowInterval::create;
    }

    @Override
    public BiConsumer<TimeWindowInterval<T>, T> accumulator() {
        return TimeWindowInterval::add;
    }

    @Override
    public BinaryOperator<TimeWindowInterval<T>> combiner() {
        return (left, right) -> {
            left.concat(right);
            return left;
        };
    }

    @Override
    public Function<TimeWindowInterval<T>, TimeWindow<T>> finisher() {
        return TimeWindow::create;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }

}
