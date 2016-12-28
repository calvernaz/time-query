package org.weirdloop.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimeQueueTest {

    private TimeQueue<TimeEvent> sut;

    @Mock
    private Collection<TimeEvent> collection;

    @Before
    public void setup() {
        sut = TimeQueue.create(collection);
    }

    @Test
    public void should_queue_all_collection() {
        sut.putAll(Collections.singletonList(TimeEvent.create(LocalDateTime.now())));
        verify(collection, times(1)).add(any(TimeEvent.class));
    }

    @Test
    public void should_remove_all_elements() {
        sut.removeAll();
        verify(collection, atLeastOnce()).clear();
    }

    @Test
    public void should_remove_elements_in_the_collections() {
        sut.remove(Collections.emptyList());
        verify(collection, atLeastOnce()).removeAll(Mockito.anyCollectionOf(TimeEvent.class));
    }

    @Test
    public void should_remove_elements_according_to_predicate() {
        sut.removeIf(Predicate.isEqual(true));
        verify(collection, atLeastOnce()).removeIf(any(Predicate.class));
    }

    @Test
    public void should_be_empty_if_no_ops() {
        boolean empty = sut.isEmpty();
        verify(collection, only()).isEmpty();
    }

    @Test
    public void should_return_immutable_collection() {
        assertThat(sut.immutableCollection()).isInstanceOf(Collection.class);
    }
}