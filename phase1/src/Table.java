import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int id;
    private final List<MenuItem> bill = new ArrayList<>();
    private final List<MenuItem> deductions = new ArrayList<>();
    private final List<MenuItem> uncookedMenuItems = new ArrayList<>();
    private Order order;

    /**
     * Constructs a new Table object.
     *
     * @param id the integer representing the id of this Table
     */
    Table(int id) {
        this.id = id;
    }

    /**
     * Ads a MenuItem to the uncookedMenuItems for this Table.
     *
     * @param item The MenuItem to be added
     */
    public void addUncookedMenuitems(MenuItem item) {
        uncookedMenuItems.add(item);
    }

    /**
     * Returns the uncooked MenuItems from this Table.
     *
     * @return a List of MenuItems which are uncooked for this Table
     */
    public List<MenuItem> getUncookedMenuItems() {
        return uncookedMenuItems;
    }

    /**
     * Sets this Table's order to contain the Order o.
     *
     * @param o The Order object to be stored in this Table
     */
    void addOrderToTable(Order o) {
        order = o;
    }

    /**
     * Adds an Order o to this Table's bill.
     *
     * @param o the Order to be added to this Table's bill
     */
    void addToBill(Order o) {
        bill.addAll(o.getItems());
    }

    /**
     * Adds to this Table's deductions the quantity specified of MenuItem item and it's respective comment.
     *
     * @param item     the MenuItem to be deducted
     * @param quantity the quantity of MenuItem item to be deducted from this Table
     * @param comment  the comment explaining the reason for MenuItem item's deduction
     */
    void addToDeductions(MenuItem item, int quantity, String comment) {
        MenuItem itemToAdd = new MenuItemImpl(item, quantity);
        addToDeductions(itemToAdd, comment);
    }

    /**
     * Adds to this Table's deductions the MenuItem item and it's respective comment.
     *
     * @param item    the MenuItem to be deducted
     * @param comment the comment explaining the reason for MenuItem item's deduction
     */
    void addToDeductions(MenuItem item, String comment) {
        item.setComment(comment);
        item.setPrice(-item.getPrice());
        for (Ingredient mod : item.getExtraIngredients()) {
            mod.setPrice(-mod.getPrice());
        }
        deductions.add(item);
    }

    /**
     * Takes a list of deducted MenuItems and turns them into a readable String.
     *
     * @return a String of MenuItems that were removed from this Table's bill
     */
    String stringDeductions() {
        StringBuilder ret = new StringBuilder("");
        for (MenuItem item : deductions) {
            ret.append(item.getQuantity()).append(" ").append(item.getName()).append(", ").append(String.format("%.2f", item.getTotal())).append("\n");
            for (Ingredient mod : item.getExtraIngredients()) {
                ret.append(item.getQuantity()).append(" ").append(mod.getName()).append(", ").append(String.format("%.2f", mod.getPrice() * item.getQuantity())).append("\n");
            }
        }
        return ret.toString();
    }

    /**
     * Prints to the screen this Table's current bill.
     * The bill includes how many of each MenuItem were ordered, and it's respective Ingredient modifications, along with their respective prices.
     * If the bill had deductions they are shown below the order, with the number of deducted items, the price of the deduction and the total price of the order.
     */
    void printBill() {
        System.out.println("BILL FOR TABLE #" + id);
        for (MenuItem item : bill) {
            System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getTotal()));
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

    /**
     * Return a String representation of this Table's current bill's price.
     *
     * @return a String representation of this Table's bill price
     */
    private String getBillPrice() {
        double initialCost = 0.00;
        double deduct = 0.00;

        for (MenuItem item : bill) {
            initialCost += (item.getPrice() * item.getQuantity());
            initialCost -= item.getExtraIngredientPrice();
            initialCost -= item.getRemovedIngredientsPrice();
        }
        // remove the price of any items that were returned
        for (MenuItem item : deductions) {
            deduct += (item.getPrice() * item.getQuantity());
            deduct += item.getExtraIngredientPrice();
            deduct -= item.getRemovedIngredientsPrice();
        }

        double ret = initialCost + deduct;
        return String.format("%.2f", ret);
    }

    /**
     * Returns the Order that currently belongs to this Table.
     *
     * @return the Order belonging to this Table
     */
    protected Order getOrder() {
        return order;
    }
}
