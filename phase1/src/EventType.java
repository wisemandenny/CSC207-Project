public enum EventType {
    ORDER("order"),
    ADDON("addon"),
    BILL("check please!"),
    COOKSEEN("received"),
    COOKREADY("ready"),
    SERVERDELIVERED("delivered"),
    SERVERRETURNED("serverReturned");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.toString().equals(type)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unrecognized type " + type);
    }

    @Override
    public String toString() {
        return type;
    }
}
