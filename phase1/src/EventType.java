public enum EventType {
    ORDER("order"),
    BILL("check please!"),
    COOKSEEN("received"),
    COOKREADY("ready"),
    SERVERDELIVERED("delivered"),
    SERVERRETURNED("serverReturned"),
    RECEIVEDSHIPMENT("receivedShipment");

    private final String type;

    /**
     * Constructs a new EventType.
     *
     * @param type the type of EventType
     */
    EventType(String type) {
        this.type = type;
    }

    /**
     * Returns the eventType that corresponds to the given String type.
     *
     * @param type the String representation of an EventType
     * @return an EventType generated that corresponds to the given String
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
