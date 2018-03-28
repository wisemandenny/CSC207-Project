package events;

import restaurant.Table;

public class EventFactory {
    private static final int TYPE_ADDRESS = 0;
    private static final int UPDATE_TYPE_ADDRESS = 1;
    private static final int TABLEID_ADDRESS = 1;
    private static final int ORDER_ADDRESS = 2;
    private static final int PAYMENT_ADDRESS = 2;
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


        //3 main types of Events: updates, shipments, and orders
        switch (type) {
            case UPDATE:
                int orderId = Integer.parseInt(splitString[ORDER_ADDRESS]);
                switch (EventType.fromString(splitString[UPDATE_TYPE_ADDRESS])) {
                    case COOKSEEN:
                        return new UpdateEvent(EventType.COOKSEEN, orderId);
                    case COOKFIRED:
                        return new UpdateEvent(EventType.COOKFIRED, orderId);
                    case COOKREADY:
                        return new UpdateEvent(EventType.COOKREADY, orderId);
                    case SERVERDELIVERED:
                        return new UpdateEvent(EventType.SERVERDELIVERED, orderId);
                    default:
                        throw new IllegalArgumentException("broken update event line in text file" + eventLine);
                }
            case RECEIVEDSHIPMENT:
                return new ShipmentEvent(splitString[1]);
            case ADDSEAT:
                return new SeatEvent(Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH)));
            case REMOVESEAT:
                return new SeatEvent(Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH)), Integer.parseInt(splitString[2]));
            case JOIN:
                return new JoinEvent(Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH)));
            case CLEAR:
                return new ClearEvent(Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH)));
            default:
                Table table = tables[Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH).split(" > ")[0])];
                Integer seat = Integer.parseInt(splitString[EventFactory.TABLEID_ADDRESS].substring(EventFactory.TABLE_LENGTH).split(" > ")[1]);
                switch (type) {
                    case ORDER:
                        return new OrderEvent(table, seat, splitString[EventFactory.ORDER_ADDRESS]);
                    case BILL:
                        return new BillEvent(table);
                    case SERVERRETURNED:
                        return new ReturnEvent(table, seat, splitString[EventFactory.ORDER_ADDRESS], splitString[EventFactory.COMMENT_ADDRESS]);
                    case PAY:
                        return new PayEvent(table, seat, splitString[EventFactory.PAYMENT_ADDRESS]);
                    default:
                        throw new IllegalArgumentException("broken event line in text file" + eventLine);
                }
        }
    }
}
