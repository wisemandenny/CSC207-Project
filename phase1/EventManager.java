import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EventManager {
    private static Scanner scanner = new Scanner ( System.in );
    public static void main(String[] args) {
        ArrayList<String> text = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("events.txt"))) {
            do {
                text.add(br.readLine());
            } while (br.ready());
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open events.txt");
        }
        catch(IOException ex) {
            System.out.println("Error reading events.txt");
        }

        for(String s : text){
            System.out.println(s);
        }
    }
}
