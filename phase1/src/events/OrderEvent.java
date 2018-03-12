package events;

import menu.MenuItem;
import restaurant.Order;
import restaurant.OrderImpl;
import restaurant.Restaurant;
import restaurant.Table;

import java.util.ArrayList;
import java.util.List;


class OrderEvent implements Event {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int ORDER_ADDRESS = 1;
    private final Order order;
    private final Table table;

    OrderEvent(Table table, String orderString) {
        this.table = table;
        order = orderStringParser(orderString);
        order.setTableId(table.getId());
    }

    @Override
    public EventType getType() {
        return EventType.ORDER;
    }

    @Override
    public void doEvent() {
        table.addOrder(order);
        Restaurant.addPlacedOrder(order);
        //System.out.println(order + "added to table.");
    }

    Order getOrder() {
        return order; //TODO: make a copy of this item for returning to avoid exposing internal private state
    }

    /**
     * Returns an restaurant.Order from a String of information from events.txt.
     *
     * @param tableOrderString an restaurant.Order's ordered item(s) with any requested modifiers
     * @return the restaurant.Order generated from strings
     */
    private Order orderStringParser(String tableOrderString) {
        List<MenuItem> parsedOrder = new ArrayList<>();
        for (String item : tableOrderString.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItem = item.split("\\s", 2);
            String orderInfo = orderItem[OrderEvent.ORDER_ADDRESS];

            // Separate the ordered item from any modifiers, there should only be one "/" break per menu item.
            String[] orderInfoSplit = orderInfo.split("\\s/\\s", 2);
            String orderedItemName = orderInfoSplit[0];
            MenuItem orderedMenuItem = Restaurant.getMenu().getMenuItem(orderedItemName);
            orderedMenuItem.setQuantity(Integer.parseInt(orderItem[OrderEvent.QUANTITY_ADDRESS]));

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
        return new OrderImpl(parsedOrder);
    }

}

