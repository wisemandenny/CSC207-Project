package restaurant;

import menu.Ingredient;
import menu.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements Order {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int ORDER_ADDRESS = 1;
    private static int idCounter = 1;
    private final List<MenuItem> orderItems;
    private int tableId;
    private int id;

    OrderImpl() {
        orderItems = new ArrayList<>();
    }

    public OrderImpl(String orderString) {
        orderItems = orderStringParser(orderString);
        id = OrderImpl.idCounter++;
    }

    public OrderImpl(List<MenuItem> items, int id, int tableId) {
        orderItems = new ArrayList<>(items);
        this.id = id; //this might break things
        this.tableId = tableId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> getItems() {
        return new ArrayList<>(orderItems);
    }


    @Override
    public void add(Order o) {
        orderItems.addAll(o.getItems());
    }

    @Override
    public void remove(Order o) {
        for (MenuItem item : o.getItems()) {
            for (MenuItem orderItem : orderItems) {
                if (item.equalsWithExtras(orderItem)) {
                    orderItems.remove(item);
                }
            }
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getTableId() {
        return tableId;
    }

    @Override
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returned(MenuItem returnedItem) {
        //go through the "order" (bill) and set matching items price to 0, and set their comment to item.comment
        for (MenuItem item : orderItems) {
            if (item.equalsWithExtras(returnedItem)) {
                item.setPrice(0.0);
                item.setComment(returnedItem.getComment());
                for (Ingredient i : item.getExtraIngredients()) {
                    i.setPrice(0.0);
                }
                for (Ingredient i : item.getRemovedIngredients()) {
                    i.setPrice(0.0);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (MenuItem item : orderItems) {
            for (int i = 0; i < item.getQuantity(); i++) { //quantity multiplier
                ingredientList.addAll(item.getIngredients());
                ingredientList.addAll(item.getExtraIngredients());
                ingredientList.removeAll(item.getRemovedIngredients());
            }
        }
        return ingredientList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (MenuItem item : orderItems) {
            sb.append(i++).append(". ").append(item.getName()).append("\n");
            for (Ingredient mod : item.getExtraIngredients()) {
                sb.append(mod.getName()).append("\n");
            }
        }
        return sb.toString();
    }

    private List<MenuItem> orderStringParser(String orderString) {
        List<MenuItem> parsedOrder = new ArrayList<>();
        for (String item : orderString.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItem = item.split("\\s", 2);
            String orderInfo = orderItem[OrderImpl.ORDER_ADDRESS];

            // Separate the ordered item from any modifiers, there should only be one "/" break per menu item.
            String[] orderInfoSplit = orderInfo.split("\\s/\\s", 2);
            String orderedItemName = orderInfoSplit[0];
            MenuItem orderedMenuItem = Restaurant.getMenu().getMenuItem(orderedItemName);
            orderedMenuItem.setQuantity(Integer.parseInt(orderItem[OrderImpl.QUANTITY_ADDRESS]));

            // If there are any modifiers:
            if (orderInfoSplit.length > 1) {
                String[] orderedMenuItemModifiers = orderInfoSplit[1].split("\\s");
                List<String> limitedModifiers = new ArrayList<>();
                for (String mod : orderedMenuItemModifiers) {
                    if (limitedModifiers.size() < 5) {
                        limitedModifiers.add(mod);
                    }
                }
                for (String modifier : limitedModifiers) {
                    String ingredientName = modifier.substring(1);
                    if (modifier.startsWith("+")) {
                        orderedMenuItem.addExtraIngredient(Restaurant.getMenu().getMenuIngredient(ingredientName));
                    } else if (modifier.startsWith("-")) {
                        orderedMenuItem.removeIngredient(Restaurant.getMenu().getMenuIngredient(ingredientName));
                    } else {
                        throw new IllegalArgumentException("Invalid character found in mod: " + modifier);
                    }
                }
            }
            parsedOrder.add(orderedMenuItem);
        }
        return parsedOrder;
    }
}
