package restaurant;

import events.BaseEvent;
import events.EventFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

class EventManager {
    private final Queue<BaseEvent> events = new LinkedList<>();

    EventManager() {
        try (BufferedReader br = new BufferedReader(new FileReader("./events.txt"))) {
            do {
                addEventFromString(br.readLine());
            } while (br.ready());
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open events.txt");
        } catch (IOException ex) {
            System.out.println("Error reading events.txt");
        }
    }

    /**
     * Return the events this restaurant.EventManager is holding.
     *
     * @return a Queue containing all of the events contained in this restaurant.EventManager
     */
    Queue<BaseEvent> getEvents() {
        return events;
    }

    void addEventFromString(String eventString){
        Table[] tables = Restaurant.getInstance().getTables();
        BaseEvent e = EventFactory.makeEvent(eventString, tables);
        e.addObserver(Restaurant.getInstance());
        events.add(e);
    }
}