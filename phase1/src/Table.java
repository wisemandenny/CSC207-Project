import java.util.ArrayList;
import java.util.List;

public class Table {
    private int id;
    private List<MenuItem> bill = new ArrayList<>();
    private Order order;

    Table(int id) {
        this.id = id;
    }

    protected void addOrderToTable(final Order o){
        order = o;
    }

    protected void addToBil(final Order o) {
        for (MenuItem item : o.getItems()){
            bill.add(item);
            //System.out.println("\n" + item.getQuantity() + " " +item.getName() + " added to bill for table #" + id + '.');
        }
    }

    protected void printBill() {
        System.out.println("BILL FOR TABLE #" + id);
        for(MenuItem item : bill) {
            System.out.println(item.getQuantity() + " " + item.getName() + ": $" + item.getPrice());
        }
        System.out.println("Total: $" + order.getTotalPrice() + "\n");
    }

    protected Order getOrder(){
        return order;
    }
}
