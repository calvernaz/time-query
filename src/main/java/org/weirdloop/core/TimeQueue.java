package org.weirdloop.core;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * Acts as a container for instances of {@link TimeEvent}.
 *
 * @param <T>
 */
final public class TimeQueue<T extends TimeEvent> {
    private final Collection<T> collection;

    /**
     * Factory to create {@link TimeQueue} instance.
     *
     * @param queue any collection implementation
     *
     * @return a {@link TimeQueue}.
     */
    public static <T extends TimeEvent> TimeQueue<T> create(Collection<T> queue) {
        return new TimeQueue<>(queue);
    }

    /**
     * Constructor
     *
     * @param queue, any collection implementation.
     */
    private TimeQueue(Collection<T> queue) {
        this.collection = queue;
    }

    /**
     * Adds the element to the collection.
     *
     * @param e the element to add.
     *
     *  @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean put(T e) {
        return collection.add(e);
    }

    /**
     * Adds all of the elements in the specified collection to this collection.
     *
     * @param e the elements to add.
     */
    public void putAll(Collection<T> e) {
        e.forEach(this::put);
    }

    /**
     * Clear the collection.
     */
    public void removeAll() {
        collection.clear();
    }

    /**
     * Remove this collection from the underlying collection.
     *
     * @param elements to remove.
     */
    public void remove(Collection<T> elements) {
        collection.removeAll(elements);
    }

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     */
    public boolean removeIf(Predicate<T> filter) {
        return collection.removeIf(filter);
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if empty, false otherwise.
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * This method allows modules to provide users with "read-only" access to internal
     * collections.
     *
     * @return an unmodifiable view of the specified collection.
     */
    public Collection<T> immutableCollection() {
        return Collections.unmodifiableCollection(collection);
    }
}