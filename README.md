TimeQuery: Query time library for Java
=======================================

Timequery is an utility library to query time based events. It's small, slow and unmaintainable.

Requires JDK 1.8.

Latest release
--------------

To add a dependency on Guava using Maven, use the following:

```xml
<dependency>
  <groupId>org.weirdloop</groupId>
  <artifactId>time-query</artifactId>
  <version>1.0</version>
</dependency>
```

Usage
====

TimeEvent
----

Time events can hold the any type of data, but keys are temporal instances.

```java
    TimeEvent.create(Instant.now());
```

or 

```java
    TimeEvent.create(Instant.now(), 3);
```

TimeQueue
----

Holds a queue with time events.

```java
TimeQueue queue = TimeQueue.create(new LinkedList<>());
queue.add(TimeEvent.create(instant, value));
```

TimeQuery
----

```sql
select count(event) from events where time > now() -1h groupy by time(5m), event
```
The equivalent of this typical query, at least on time-series databases, using TimeQuery.

```java
    Map<Integer, Long> histogram = TimeQuery.create(events)
                                   .downTo(Span.of(create(minusAnHour), create(now)))
                                   .groupBy(Buckets.of(5, ChronoUnit.MINUTES))
                                   .histogram();

    // Result
    {0, 3}, {1, 5}, {2, 10}, ... {bucket-index, count} 
```

Or just the interval windows:

```java
    TimeWindow<TimeEvent> windows = TimeQuery.create(events)
                               .groupBy(Buckets.of(5, ChronoUnit.MINUTES))
                               .windows();

    // Result
    TimeWindow:
      [0] -> TimeWindowInterval@{TimeEvent[0], TimeEvent[1], TimeEvent[2], TimeEvent[3], TimeEvent[4]}
      [1] -> TimeWindowInterval@{TimeEvent[5], TimeEvent[6], TimeEvent[7], TimeEvent[8], TimeEvent[9]}
```

Or using an interval spans:

```java
    TimeWindow<TimeEvent> timeWindow = TimeQuery.create(events)
                                        .interval(Span.openClosed(create(fourHoursAgo), create(twoHoursAgo)))
                                        .windows();

   // Result
   TimeWindow:
     TimeWindowInterval@{TimeEvent[0], TimeEvent[1], ...}
```

Or even:

```java
    TimeWindow rangeQuery = TimeQuery.create(events)
                             .downTo(Span.closedOpen(create(AnHourAgo), create(now)))
                             .groupBy(Buckets.of(1, ChronoUnit.MINUTES))
                             .windows();
```