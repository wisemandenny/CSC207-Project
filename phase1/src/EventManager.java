import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

class EventManager {
    private static final int TYPE_ADDRESS = 0;
    private static final int TABLEID_ADDRESS = 1;
    private static final int ORDER_ADDRESS = 2;
    private static final int COMMENT_ADDRESS = 3;
    private static final int TABLE_LENGTH = 6;
    private final Queue<Event> events = new LinkedList<>();

    EventManager() {
        try (BufferedReader br = new BufferedReader(new FileReader("events.txt"))) {
            do {
                events.add(parseString(br.readLine()));
            } while (br.ready());
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open events.txt");
        } catch (IOException ex) {
            System.out.println("Error reading events.txt");
        }
    }

    /**
     * Returns an Event generated from the String s, obtained from events.txt.
     *
     * @param s the String containing Order type, Table, and information appropriate to the Order type
     * @return an Event generated from the String s
     */
    private Event parseString(String s) {
        String[] splitString = s.split("\\s\\|\\s", 4);
        if (splitString.length < 2 || splitString.length > 5) {
            throw new IllegalArgumentException("Invalid line in events.txt" + s);
        }
        String stringType = splitString[EventManager.TYPE_ADDRESS];
        int tableId = 0;
        if (!stringType.equals("receivedShipment")) {
            tableId = Integer.parseInt(splitString[EventManager.TABLEID_ADDRESS].substring(EventManager.TABLE_LENGTH));
        }
        EventType type = EventType.fromString(stringType);

        Event ret;
        switch (type) {
            case ORDER:
                ret = new Event(EventType.ORDER, tableId, splitString[EventManager.ORDER_ADDRESS]);
                break;
            case SERVERRETURNED:
                ret = new Event(EventType.SERVERRETURNED, tableId, splitString[EventManager.ORDER_ADDRESS], splitString[EventManager.COMMENT_ADDRESS]);
                break;
            case RECEIVEDSHIPMENT:
                ret = new Event(EventType.RECEIVEDSHIPMENT, splitString[EventManager.ORDER_ADDRESS]);
                break;
            default:
                ret = new Event(type, tableId);

        }
        return ret;
    }

    /**
     * Return the Events this EventManager is holding.
     *
     * @return a Queue containing all of the Events contained in this EventManager
     */
    Queue<Event> getEvents() {
        return new LinkedList<>(events); //return a copy of events so as to not expose the private internal state
    }
}