package org.weirdloop.query;

public enum BoundType {
    /**
     * The endpoint value <i>is not</i> considered part of the set ("exclusive").
     */
    OPEN {
        @Override
        BoundType flip() {
            return CLOSED;
        }
    },
    /**
     * The endpoint value <i>is</i> considered part of the set ("inclusive").
     */
    CLOSED {
        @Override
        BoundType flip() {
            return OPEN;
        }
    };

    /**
     * Returns the bound type corresponding to a boolean value for inclusivity.
     */
    static BoundType forBoolean(boolean inclusive) {
        return inclusive ? CLOSED : OPEN;
    }

    abstract BoundType flip();
}