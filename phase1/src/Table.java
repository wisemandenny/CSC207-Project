import java.util.ArrayList;

public class Table {
    private int id;
    private ArrayList<String> bill = new ArrayList<>();

    Table(int id) {
        this.id = id;
    }

    public void addToBil(final String s) {
        bill.add(s);
        System.out.println(s + " added to table #" + id + '.');
    }

    public void printBill() {
        /*
        This uses the lambda pattern, which you can recognize by the ->.
        The parameter for the forEach function is a consumer. You can think of it like this:
        (name of the iterable element) -> do this function with it.
        so it just prints out every item in the bill. otherwise we'd have to do a longer code.
        TODO: Delete this
         */
        System.out.println("\n" +
                "BILL FOR TABLE #" + id);
        bill.forEach(billItem -> System.out.println(billItem));
        System.out.println("");
    }
}
