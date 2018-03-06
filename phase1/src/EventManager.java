import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

class EventManager {
    private Queue<Event> events = new LinkedList<>();

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

    //TODO: add dependency injection to this method
    private Event parseString(String s) {
        String[] splitString = s.split("\\s\\|\\s", 3);

        int tableId = Integer.parseInt(splitString[1].substring(6));
        String type = splitString[0];

        Event ret;
        switch (type) {
            case "order":
                ret = new Event("order", tableId, splitString[2]);
                break;
            case "check please!":
                ret = new Event("bill", tableId);
                break;
            case "received":
                ret = new Event("cookSeen", tableId);
                break;
            case "ready":
                ret = new Event("cookReady", tableId);
                break;
            case "delivered":
                ret = new Event("serverDelivered", tableId);
                break;
            default:
                throw new IllegalArgumentException("There is a typo in the events.txt file: unrecognized request");
        }
        return ret;
    }

    Queue<Event> getEvents() {
        return events;
    }
}