import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

class Restaurant {
    private Map<Integer, Table> tables = new HashMap<>();
    private int numOfTables;
    private EventManager eventManager;

    Restaurant(int numOfTables) {
        this.numOfTables = numOfTables;
        for (int i = 1; i <= numOfTables; i++) {
            tables.put(i, new Table(i)); //add all the tables to the restaurant.
        }
        eventManager = new EventManager();
        handleEvents(eventManager);
    }

    private void printBill(int tableId) {
        tables.get(tableId).printBill();
    }

    private void addOrderToBill(int tableId, String[] order) {
        Table table = tables.get(tableId);
        for (String item : order) {
            table.addToBil(item);
        }
    }

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();
        while (!events.isEmpty()) {
            Event e = events.remove();
            if (e.getType().equals("order")) {
                addOrderToBill(e.getTableId(), e.getOrder()); //TODO: this should be put after the item has been delivered, but for now we are just testing.
            } else if (e.getType().equals("bill")) {
                printBill(e.getTableId());
            } else if (e.getType().equals("cookSeen")) {
                System.out.println("Cook has seen your order.");
            } else if (e.getType().equals("cookReady")) {
                System.out.println("Order of blabla is ready for pick up.");
            } else if (e.getType().equals("serverDelivered")) {
                System.out.println("Order of blabla has been delivered");
            } else if (e.getType().equals("serverReturned")) {
                System.out.println("We got a live one here! Order of blabla has been sent back to the kitchen!");
            } else {
                throw new IllegalStateException("We should never get here, this means there's a typo in events.txt.");
            }
        }
    }
}
