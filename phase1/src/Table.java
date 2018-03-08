import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int id;
    private final List<MenuItem> bill = new ArrayList<>();
    private final List<MenuItem> deductions = new ArrayList<>();
    private final List<MenuItem> uncookedMenuItems = new ArrayList<>();
    private Order order;

    Table(final int id) {
        this.id = id;
    }

    public void addUncookedMenuitems(final MenuItem item) {
        uncookedMenuItems.add(item);
    }

    public List<MenuItem> getUncookedMenuItems() {
        return uncookedMenuItems;
    }

    void addOrderToTable(final Order o) {
        order = o;
    }

    void addToBill(final Order o) {
        bill.addAll(o.getItems());
    }

    void addToDeductions(final MenuItem item, final int quantity, final String comment) {
        final MenuItem itemToAdd = new MenuItemImpl(item, quantity);
        itemToAdd.setComment(comment);

        double cost = item.getPrice();
        cost *= item.getQuantity() * -1;
        itemToAdd.setPrice(cost);
        deductions.add(itemToAdd);

        for (final Ingredient mod : itemToAdd.getRemovedIngredients()) {

        }
    }

    void addToDeductions(final List<MenuItem> items) {
        deductions.addAll(items);
    }

    // get all the items that have been rejected and make them into a string
    String stringDeductions() {
        final StringBuilder ret = new StringBuilder("");
        for (final MenuItem item : deductions) {
            ret.append(item.getQuantity()).append(" ").append(item.getName()).append(", ").append(item.getPrice()).append("\n");
        }
        return ret.toString();
    }

    void printBill() {
        System.out.println("BILL FOR TABLE #" + id);
        for (final MenuItem item : bill) {
            System.out.println(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getPrice()));
            for (final Ingredient addedIng : item.getExtraIngredients()) {
                System.out.println("add " + item.getQuantity() + " " + addedIng.getName() + ": $" + String.format("%.2f", addedIng.getPrice() * item.getQuantity()));
            }
            for (final Ingredient removedIng : item.getRemovedIngredients()) {
                System.out.println("remove " + item.getQuantity() + " " + removedIng.getName() + ": -$" + String.format("%.2f", removedIng.getPrice() * item.getQuantity()));
            }

        }
        if (!deductions.isEmpty()) {
            System.out.println("\nDEDUCTIONS (-)");
            for (final MenuItem item : deductions) {
                System.out.println(item.getQuantity() + " " + item.getName() + ": $" +
                        String.format("%.2f", item.getPrice()) + " | Reason: " + item.getComment());
            }
        }
        System.out.println("Total: $" + getBillPrice() + "\n");
    }

    private String getBillPrice() {
        double ret = 0.00;
        for (final MenuItem item : bill) {
            ret += item.getPrice();
            ret += item.getExtraIngredientPrice();
        }
        // remove the price of any items that were returned
        for (final MenuItem item : deductions) {
            ret += item.getPrice();
        }
        return String.format("%.2f", ret);
    }

    protected Order getOrder() {
        return order;
    }
}
