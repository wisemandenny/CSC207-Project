package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Seat extends TableImpl {

    private final int id;
    private final List<Order> orders = new ArrayList<>();
    private final Bill bill;


    Seat(int id){
        this.id = id;
        bill = new BillImpl(id);
    }

    @Override
    public void addOrder(Order o) {
        orders.add(o);
    }

    @Override
    public List<Order> getOrders(){ return orders; }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public Bill getBill(){
        return bill;
    }

    @Override
    public void addOrderToBill(Order o) {
        orders.remove(o);
        bill.add(o);
    }

    @Override
    public void removeFromBill(Order o) {
        bill.remove(o);
    }

    @Override
    public void comp(Order o) {
        bill.comp(o);
    }

    /**
     * Prints to the screen this restaurant.TableImpl's current bill.
     * The bill includes how many of each menu.MenuItem were ordered, and its respective menu.Ingredient modifications, along with their respective prices.
     * If the bill had deductions they are shown below the order, with the number of deducted items, the price of the deduction and the total price of the order.
     */
    @Override
    public void printBill() {
        bill.getBillString();
    }

}
