import java.util.ArrayList;
import java.util.List;

public class Table {
    /**
     * A table at the restaurant with identification, a bill, and sometimes deductions and uncooked foods.
     */
    private final int id;
    private final List<MenuItem> bill = new ArrayList<>();
    private final List<MenuItem> deductions = new ArrayList<>();
    private final List<MenuItem> uncookedMenuItems = new ArrayList<>();
    private Order order;

    Table(int id) {
        /**
         * This table has identification id.
         */
        this.id = id;
    }

    public void addUncookedMenuItems(MenuItem item) {
        /**
         * Add a food item that hasn't been cooked, some reason, to a list of uncooked foods uncookedMenuItems.
         */
        uncookedMenuItems.add(item);
    }

    public List<MenuItem> getUncookedMenuItems() {
        /**
         * Returns the list of uncooked foods uncookedMenuItems.
         */
        return uncookedMenuItems;
    }

    void addOrderToTable(Order o) {
        /**
         * Assigns an order o to this table.
         */
        order = o;
    }

    void addToBill(Order o) {
        /**
         * Adds the items from order o to this tables' bill.
         */
        bill.addAll(o.getItems());
    }

    void addToDeductions(List<MenuItem> items) {
        /**
         * Adds the list of menu items items to this tables deductions.
         */
        deductions.addAll(items);
    }

    // get all the items that have been rejected and make them into a string
    String stringDeductions() {
        /**
         * Creates a client friendly representation of this tables' deductions.
         */
        StringBuilder ret = new StringBuilder("");
        for (MenuItem item : deductions) {
            ret.append(item.getQuantity()).append(" ").append(item.getName()).append(", ").append(item.getPrice()).append("\n");
        }
        return ret.toString();
    }

    void printBill() {
        /**
         * Creates a client friendly representation of this tables' bill.
         */
        System.out.println("BILL FOR TABLE #" + id);
        for (MenuItem item : bill) {
            System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getPrice()));
            for (Ingredient addedIng : item.getExtraIngredients()) {
                System.out.println("add " + item.getQuantity() + " " + addedIng.getName() + ": $" + String.format("%.2f", addedIng.getPrice() * item.getQuantity()));
            }
            for (Ingredient removedIng : item.getRemovedIngredients()) {
                System.out.println("remove " + item.getQuantity() + " " + removedIng.getName() + ": -$" + String.format("%.2f", removedIng.getPrice() * item.getQuantity()));
            }

        }
        if (!deductions.isEmpty()) {
            System.out.println("\nDEDUCTIONS (-)");
            for (MenuItem item : deductions) {
                System.out.println(item.getQuantity() + " " + item.getName() + ": $" +
                        String.format("%.2f", item.getPrice()) + " | Reason: " + item.getComment());
            }
        }
        System.out.println("Total: $" + getBillPrice() + "\n");
    }

    private String getBillPrice() {
        /**
         * Returns the total cost of this table's order as the bills' price.
         */
        double ret = 0.00;
        for (MenuItem item : bill) {
            ret += item.getPrice();
            ret += item.getExtraIngredientPrice();
        }
        // remove the price of any items that were returned
        for (MenuItem item : deductions) {
            ret += item.getPrice();
        }
        return String.format("%.2f", ret);
    }

    protected Order getOrder() {
        /**
         * Returns this tables' order.
         */
        return order;
    }
}
