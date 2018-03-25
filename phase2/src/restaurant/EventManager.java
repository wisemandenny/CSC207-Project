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

    EventManager() {
        try (BufferedReader br = new BufferedReader(new FileReader("./events.txt"))) {
            do {
                addEventFromString(br.readLine());
            } while (br.ready());
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
    }

    /**
     * Return the events this restaurant.EventManager is holding.
     *
     * @return a Queue containing all of the events contained in this restaurant.EventManager
     */
    Queue<Event> getEvents() {
        return events;
    }

    void addEventFromString(String eventString){
        Table[] tables = Restaurant.getInstance().getTables();
        Event e = EventFactory.makeEvent(eventString, tables);
        events.add(e);
    }
}