import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class EventManager {
    //private static Scanner scanner = new Scanner ( System.in ); //we will use this for input later
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

    //TODO: This only works for orders and bills right now.
    private Event parseString(String s) {
        String[] splitString = s.split("\\s\\|\\s", 3);

        if (splitString[0].equals("order")) {
            return new Event("order", Integer.parseInt(splitString[1].substring(6)), splitString[2]);
        } else if (splitString[0].equals("check please!")) {
            return new Event("bill", Integer.parseInt(splitString[1].substring(6)));
        } else throw new IllegalArgumentException("There is a typo in the events.txt file: unrecognized request");
    }

    Queue<Event> getEvents() {
        return events;
    }
}