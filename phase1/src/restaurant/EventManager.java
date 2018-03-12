package restaurant;

import events.Event;
import events.EventFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

class EventManager {
    private final Queue<Event> events = new LinkedList<>();


    EventManager(Table[] tables) {
        try (BufferedReader br = new BufferedReader(new FileReader("events.txt"))) {
            do {
                events.add(EventFactory.makeEvent(br.readLine(), tables));
            } while (br.ready());
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open events.txt");
        } catch (IOException ex) {
            System.out.println("Error reading events.txt");
        }
    }

  /*
    private BaseEvent parseString(String s) {
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

        BaseEvent ret;
        switch (type) {
            case ORDER:
                ret = new BaseEvent(EventType.ORDER, tableId, splitString[EventManager.ORDER_ADDRESS]);
                break;
            case SERVERRETURNED:
                ret = new BaseEvent(EventType.SERVERRETURNED, tableId, splitString[EventManager.ORDER_ADDRESS], splitString[EventManager.COMMENT_ADDRESS]);
                break;
            case RECEIVEDSHIPMENT:
                ret = new BaseEvent(EventType.RECEIVEDSHIPMENT, splitString[EventManager.ORDER_ADDRESS]);
                break;
            default:
                ret = new BaseEvent(type, tableId);

        }
        return ret;
    }*/

    /**
     * Return the events this restaurant.EventManager is holding.
     *
     * @return a Queue containing all of the events contained in this restaurant.EventManager
     */
    Queue<Event> getEvents() {
        return new LinkedList<>(events); //return a copy of events so as to not expose the private internal state
    }
}