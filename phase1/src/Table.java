import java.util.ArrayList;
import java.util.List;

public class Table {
    private int id;
    private List<MenuItem> bill = new ArrayList<>();

    Table(int id) {
        this.id = id;
    }

    public void addToBil(final Order o) {
        for (MenuItem item : o.getItems()){
            bill.add(item);
            System.out.println(item.getName() + " added to table #" + id + '.');
        }

    }

    public void printBill() {
        System.out.println("\n" +
                "BILL FOR TABLE #" + id);
        for(MenuItem item : bill) {
            System.out.println(item.getName() + ": $" + item.getPrice());
        }
        System.out.println("");
    }
}
