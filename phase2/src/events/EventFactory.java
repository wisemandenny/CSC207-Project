package events;

import restaurant.Table;

public class EventFactory {
    private static final int TYPE_ADDRESS = 0;
    private static final int TABLEID_ADDRESS = 1;
    private static final int ORDER_ADDRESS = 2;
    private static final int COMMENT_ADDRESS = 3;
    private static final int TABLE_LENGTH = 6;

    private EventFactory() {
    }

    public static Event makeEvent(String eventLine, Table[] tables) {
        String[] splitString = eventLine.split("\\s\\|\\s", 4);
        if (splitString.length < 2 || splitString.length > 5) {
            throw new IllegalArgumentException("Invalid line in events.txt" + eventLine);
        }

        EventType type = EventType.fromString(splitString[EventFactory.TYPE_ADDRESS]);

        switch (type) {
            case UPDATE: //TODO: fix this error message
                int orderId = Integer.parseInt(splitString[2]);
                switch (EventType.fromString(splitString[1])) {
                    case COOKSEEN:
                        return new UpdateEvent(EventType.COOKSEEN, orderId);
                    case COOKREADY:
                        return new UpdateEvent(EventType.COOKREADY, orderId);
                    case SERVERDELIVERED:
                        return new UpdateEvent(EventType.SERVERDELIVERED, orderId);
                    default:
                        throw new IllegalArgumentException("broken update event line in text file" + eventLine);
                }
            case RECEIVEDSHIPMENT:
                return new ShipmentEvent(splitString[1]);
            default:
                Table table = tables[Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH))];
                switch (type) {
                    case ORDER:
                        return new OrderEvent(table, splitString[EventFactory.ORDER_ADDRESS]);
                    case BILL:
                        return new BillEvent(table);
                    case SERVERRETURNED:
                        return new ReturnEvent(table, splitString[EventFactory.ORDER_ADDRESS], splitString[EventFactory.COMMENT_ADDRESS]);
                    default:
                        throw new IllegalArgumentException("broken event line in text file" + eventLine);
                }
        }
    }
}
