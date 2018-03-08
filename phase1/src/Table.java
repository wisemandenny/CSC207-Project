import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int id;
    private final List<MenuItem> bill = new ArrayList<>();
    private final List<MenuItem> deductions = new ArrayList<>();
    private final List<MenuItem> uncookedMenuItems = new ArrayList<>();
    private Order order;

    Table(int id) {
        this.id = id;
    }

    public void addUncookedMenuitems(MenuItem item) {
        uncookedMenuItems.add(item);
    }

    public List<MenuItem> getUncookedMenuItems() {
        return uncookedMenuItems;
    }

    void addOrderToTable(Order o) {
        order = o;
    }

    void addToBill(Order o) {
        bill.addAll(o.getItems());
    }

    void addToDeductions(MenuItem item, int quantity, String comment) {
        MenuItem itemToAdd = new MenuItemImpl(item, quantity);
        addToDeductions(itemToAdd, comment);
    }

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
     * @return  a String of MenuItems that were removed from this Table's bill
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

    private String getBillPrice() {
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
        return order;
    }
}
