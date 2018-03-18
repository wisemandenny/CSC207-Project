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
        try (BufferedReader br = new BufferedReader(new FileReader("./events.txt"))) {
            do {
                events.add(EventFactory.makeEvent(br.readLine(), tables));
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
    Queue<Event> getEvents() {
        return new LinkedList<>(events); //return a copy of events so as to not expose the private internal state
    }
}