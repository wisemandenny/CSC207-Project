import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

class EventManager {
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

    private Event parseString(String s) {
        String[] splitString = s.split("\\s\\|\\s", 4);
        if (splitString.length < 2 || splitString.length > 5) {
            throw new IllegalArgumentException("Invalid length of string"); //TODO: fix this warning message
        }
        int tableId = Integer.parseInt(splitString[1].substring(EventManager.TABLE_LENGTH));
        EventType type = EventType.fromString(splitString[0]);

        Event ret;
        //TODO: fix this (no special case)
        switch (type) {
            case ORDER:
                ret = new Event(EventType.ORDER, tableId, splitString[2]);
                break;
            case ADDON:
                ret = new Event(EventType.ADDON, tableId, splitString[2]);
                break;
            case SERVERRETURNED:
                ret = new Event(EventType.SERVERRETURNED, tableId, splitString[2], splitString[3]);
                break;
            default:
                ret = new Event(type, tableId);

        }
        return ret;
    }

    //TODO: add hasNextEvent and nextEvent so as to not expose the private ue

    Queue<Event> getEvents() {
        return events;
    }
}