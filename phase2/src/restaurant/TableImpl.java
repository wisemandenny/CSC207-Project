package restaurant;

import java.util.ArrayList;
import java.util.List;

public class TableImpl implements Table {
    private final int id;
    private final List<Order> orders = new ArrayList<>();
    private final Bill bill;

    /**
     * Constructs a new restaurant.TableImpl object.
     *
     * @param id the integer representing the id of this restaurant.TableImpl
     */
    TableImpl(int id) {
        this.id = id;
        bill = new BillImpl(id);
    }

    /**
     * Sets this restaurant.TableImpl's order to contain the restaurant.Order o.
     *
     * @param o The restaurant.Order object to be stored in this restaurant.TableImpl
     */
    @Override
    public void addOrder(Order o) {
        orders.add(o);
    }


    @Override
    public int getId() {
        return id;
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
        bill.printBill();
    }
}
