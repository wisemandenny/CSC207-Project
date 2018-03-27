package restaurant;

import menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class OrderFactory {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int ORDER_ADDRESS = 1;
    private static int idCounter = 1;

    private OrderFactory() {}

    /**
     * Returns an Order that has a list of MenuItems, an ID, a Table ID, and a seat ID.
     *
     * @param items a List of MenuItems that are part of this Order.
     * @param orderId the unique ID of this Order.
     * @param tableId the Table ID of this Order.
     * @param seatId the seat ID of this Order's Table.
     * @return  an Order with all this information.
     */
    public static Order makeOrder(List<MenuItem> items, int orderId, int tableId, int seatId){
        return new OrderImpl(items, orderId, tableId, seatId);
    }

    /**
     * Returns an Order with orderString MenuItems, Table ID tableId, and seat ID seatId.
     *
     * @param orderString a String that must be parsed, which contains MenuItems and information about them.
     * @param tableId the Table ID of this Order.
     * @param seatId the seat ID of this Order's Table.
     * @return  an Order with all this information.
     */
    public static Order makeOrder(String orderString, int tableId, int seatId){
        return makeOrder(orderStringParser(orderString), idCounter++, tableId, seatId);
    }

    /**
     * Returns an Order based on information extracted from String orderString.
     *
     * @param orderString a String that must be parsed, which contains information about an order.
     * @return  an Order based on information extracted from String orderString.
     */
    public static Order makeOrder(String orderString){
        return makeOrder(orderStringParser(orderString), idCounter++, 1, 1);
    }

    /**
     * Returns a List of MenuItems that are extracted from String orderString.
     *
     * @param orderString a String containing information that can be gathered to build Orders.
     * @return  a List of MenuItems based on information extracted from String orderString.
     */
    private static List<MenuItem> orderStringParser(String orderString) {
        List<MenuItem> parsedOrder = new ArrayList<>();
        for (String item : orderString.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItem = item.split("\\s", 2);
            String orderInfo = orderItem[ORDER_ADDRESS];

            // Separate the ordered item from any modifiers, there should only be one "/" break per menu item.
            String[] orderInfoSplit = orderInfo.split("\\s/\\s");
            String orderedItemName = orderInfoSplit[0];
            MenuItem orderedMenuItem = Restaurant.getInstance().getMenu().getMenuItem(orderedItemName);
            orderedMenuItem.setQuantity(Integer.parseInt(orderItem[QUANTITY_ADDRESS]));

            // If there are any modifiers:
            if (orderInfoSplit.length > 1) {
                String[] orderedMenuItemModifiers = orderInfoSplit[1].split("\\s");
                List<String> limitedModifiers = new ArrayList<>();
                for (String mod : orderedMenuItemModifiers) {
                    limitedModifiers.add(mod);
                }
                for (String modifier : limitedModifiers) {
                    String ingredientName = modifier.substring(1);
                    if (modifier.startsWith("+")) {
                        orderedMenuItem.addExtraIngredient(Restaurant.getInstance().getMenu().getMenuIngredient(ingredientName));
                    } else if (modifier.startsWith("-")) {
                        orderedMenuItem.removeIngredient(Restaurant.getInstance().getMenu().getMenuIngredient(ingredientName));
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
