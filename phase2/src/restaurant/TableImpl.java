package restaurant;

import menu.Ingredient;
import menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TableImpl implements Table {
    private final int id;
    private final Order bill = new OrderImpl();
    private final List<MenuItem> deductions = new ArrayList<>();
    private final List<MenuItem> uncookedMenuItems = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    /**
     * Constructs a new restaurant.TableImpl object.
     *
     * @param id the integer representing the id of this restaurant.TableImpl
     */
    TableImpl(int id) {
        this.id = id;
    }

    /**
     * Ads a menu.MenuItem to the uncookedMenuItems for this restaurant.TableImpl.
     *
     * @param item The menu.MenuItem to be added
     */
    public void addUncookedMenuitems(MenuItem item) {
        uncookedMenuItems.add(item);
    }

    /**
     * Returns the uncooked MenuItems from this restaurant.TableImpl.
     *
     * @return a List of MenuItems which are uncooked for this restaurant.TableImpl
     */
    public List<MenuItem> getUncookedMenuItems() {
        return uncookedMenuItems;
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

    public void removeFromBill() {
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
        for (MenuItem item : o.getItems()) {
            bill.returned(item);
        }
    }

    /**
     * Prints to the screen this restaurant.TableImpl's current bill.
     * The bill includes how many of each menu.MenuItem were ordered, and its respective menu.Ingredient modifications, along with their respective prices.
     * If the bill had deductions they are shown below the order, with the number of deducted items, the price of the deduction and the total price of the order.
     */
    @Override
    public void printBill() {
        System.out.println("BILL FOR TABLE #" + id);
        for (MenuItem item : bill.getItems()) {
            if (item.getPrice() == 0.0) {
                System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getTotal()) + "   Sent back because: " + item.getComment() + ".");
            } else {
                System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getTotal()));
            }
            for (Ingredient addedIng : item.getExtraIngredients()) {
                System.out.println("add " + item.getQuantity() + " " + addedIng.getName() + ": $" + String.format("%.2f", addedIng.getPrice() * item.getQuantity()));
            }
            for (Ingredient removedIng : item.getRemovedIngredients()) {
                System.out.println("remove " + item.getQuantity() + " " + removedIng.getName() + ": -$" + String.format("%.2f", removedIng.getPrice() * item.getQuantity()));
            }

        }
        System.out.println("Total: $" + getBillPrice() + "\n");
    }

    /**
     * Return a String representation of this restaurant.TableImpl's current bill's price.
     *
     * @return a String representation of this restaurant.TableImpl's bill price
     */
    private String getBillPrice() {
        double cost = 0.00;

        for (MenuItem item : bill.getItems()) {
            cost += (item.getPrice() * item.getQuantity());
            cost += item.getExtraIngredientPrice();
            cost -= item.getRemovedIngredientsPrice();
        }

        return String.format("%.2f", cost);
    }

    /**
     * Returns the restaurant.Order that currently belongs to this restaurant.TableImpl.
     *
     * @return the restaurant.Order belonging to this restaurant.TableImpl
     */
    /*
    @Override
    public Order getOrder() {
        return order;
    }*/
}
