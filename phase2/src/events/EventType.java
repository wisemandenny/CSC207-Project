package events;

public enum EventType {
    ORDER("order"),
    UPDATE("update"),
    BILL("check please!"),
    COOKSEEN("received"),
    COOKFIRED("fired"),
    COOKREADY("ready"),
    SERVERDELIVERED("delivered"),
    SERVERRETURNED("serverReturned"),
    RECEIVEDSHIPMENT("receivedShipment");
    //TODO: make a comp event in order to make it possible to comp items

    private final String type;

    /**
     * Constructs a new events.EventType.
     *
     * @param type the type of events.EventType
     */
    EventType(String type) {
        this.type = type;
    }

    /**
     * Returns the eventType that corresponds to the given String type.
     *
     * @param type the String representation of an events.EventType
     * @return an events.EventType generated that corresponds to the given String
     */
    public static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.toString().equals(type)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unrecognized type " + type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return type;
    }
}
