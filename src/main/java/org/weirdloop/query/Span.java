package org.weirdloop.query;

import org.weirdloop.core.TimeEvent;

import java.util.Objects;

/**
 * Predicate for time relative queries.
 *
 * @param <T>
 */
public class Span<T extends TimeEvent> {
    private final Cut<T> from;
    private final Cut<T> to;

    private Span(Cut<T> from, Cut<T> to) {
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        if (from.compareTo(to) > 0
                || from == Cut.<T>aboveAll()
                || to == Cut.<T>belowAll()) {
            throw new IllegalArgumentException("Invalid range: " + to + " " + from);
        }
    }

    static <T extends TimeEvent> Span<T> create(Cut<T> lowerBound, Cut<T> upperBound) {
        return new Span<>(lowerBound, upperBound);
    }

    private static final Span ALL =
            new Span<>(Cut.belowAll(), Cut.aboveAll());

    /**
     * Returns a range that contains every value of type {@code C}.
     *
     * @since 14.0
     */
    @SuppressWarnings("unchecked")
    public static <T extends TimeEvent> Span<T> all() {
        return (Span) ALL;
    }

    /**
     * Returns a range that contains all values strictly greater than {@code
     * lower} and strictly less than {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than <i>or
     *     equal to</i> {@code upper}
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> open(T lower, T upper) {
        return create(Cut.aboveValue(lower), Cut.belowValue(upper));
    }

    /**
     * Returns a range that contains all values greater than or equal to
     * {@code lower} and less than or equal to {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code
     *     upper}
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> closed(T lower, T upper) {
        return create(Cut.belowValue(lower), Cut.aboveValue(upper));
    }

    /**
     * Returns a range that contains all values strictly greater than {@code
     * lower} and less than or equal to {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code
     *     upper}
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> openClosed(T lower, T upper) {
        return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
    }


    /**
     * Returns a range that contains all values greater than or equal to
     * {@code lower} and strictly less than {@code upper}.
     *
     * @throws IllegalArgumentException if {@code lower} is greater than {@code
     *     upper}
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> closedOpen(T lower, T upper) {
        return create(Cut.belowValue(lower), Cut.belowValue(upper));
    }


    /**
     * Returns a range that contains all values strictly less than {@code
     * endpoint}.
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> lessThan(T endpoint) {
        return create(Cut.belowAll(), Cut.belowValue(endpoint));
    }

    /**
     * Returns a range that contains all values less than or equal to
     * {@code endpoint}.
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> atMost(T endpoint) {
        return create(Cut.belowAll(), Cut.aboveValue(endpoint));
    }

    /**
     * Returns a range with no lower bound up to the given endpoint, which may be
     * either inclusive (closed) or exclusive (open).
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> upTo(T endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return lessThan(endpoint);
            case CLOSED:
                return atMost(endpoint);
            default:
                throw new AssertionError();
        }
    }

    /**
     * Returns a range that contains all values strictly greater than {@code
     * endpoint}.
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> greaterThan(T endpoint) {
        return create(Cut.aboveValue(endpoint), Cut.aboveAll());
    }

    /**
     * Returns a range that contains all values greater than or equal to
     * {@code endpoint}.
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> atLeast(T endpoint) {
        return create(Cut.belowValue(endpoint), Cut.aboveAll());
    }

    /**
     * Returns a range from the given endpoint, which may be either inclusive
     * (closed) or exclusive (open), with no upper bound.
     *
     * @since 14.0
     */
    public static <T extends TimeEvent> Span<T> downTo(T endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return greaterThan(endpoint);
            case CLOSED:
                return atLeast(endpoint);
            default:
                throw new AssertionError();
        }
    }

    public static <C extends Comparable> int compareOrThrow(C left, C right) {
        return left.compareTo(right);
    }

    /**
     * Returns {@code true} if {@code value} is within the bounds of this range. For example, on the
     * range {@code [0..2)}, {@code contains(1)} returns {@code true}, while {@code contains(2)}
     * returns {@code false}.
     */
    public boolean contains(T value) {
        Objects.requireNonNull(value);
        // let this throw CCE if there is some trickery going on
        return from.isLessThan(value) && !to.isLessThan(value);
    }
}
