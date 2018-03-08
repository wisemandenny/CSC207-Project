import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Event {
    private static final int QUANTITY_ADDRESS = 0;
    private final EventType type;
    private final Map<Ingredient, Integer> shipment = new HashMap<>();
    private int tableId;
    private Order order;
    private Order deductions;

    Event(EventType type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    Event(EventType type, int tableId, String order) {
        this.type = type;
        this.tableId = tableId;
        if (type.equals(EventType.ORDER)) {
            this.order = orderConstructorHelper(order);
        }
    }

    Event(EventType type, int tableId, String itemList, String commentList) {
        this.type = type;
        this.tableId = tableId;
        deductions = orderConstructorHelper(itemList);
        commentSetter(commentList);
    }

    Event(EventType type, String shipment) {
        this.type = type;
        String[] info = shipment.split(", ");
        for (String ingredient : info) {
            String[] ingredientWithQuantity = ingredient.split("\\s");
            int quantity = Integer.parseInt(ingredientWithQuantity[0]);
            Ingredient i = new IngredientImpl(ingredientWithQuantity[1]);
            this.shipment.put(i, quantity);
        }
    }


    /**
     * Sets the comments of returned MenuItems in this Event.
     *
     * @param commentList the String containing the comments to be added to the deducted MenuItems
     */
    private void commentSetter(String commentList) {
        int count = 0;
        // make comments list
        String[] comments = new String[deductions.getItems().size()];
        // add every comment to the comment list
        for (String itemComment : commentList.split(",\\s")) {
            comments[count++] = itemComment;
        }
        // assign each comment to its respective returned menu item
        count = 0;
        for (MenuItem item : deductions.getItems()) {
            item.setComment(comments[count++]);
        }
    }

    /**
     * Returns an Order from a String of information from events.txt.
     *
     * @param strings an Order's ordered item(s) with any requested modifiers
     * @return the Order generated from strings
     */
    private Order orderConstructorHelper(String strings) {
        List<MenuItem> orderItems = new ArrayList<>();
        for (String item : strings.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItemSplitString = item.split("\\s", 2);
            int itemQuantity = Integer.parseInt(orderItemSplitString[Event.QUANTITY_ADDRESS]);
            String orderInfo = orderItemSplitString[1];

            // Separate the ordered item from any modifiers, there should only be one "/" break per menu item.
            String[] orderInfoSplit = orderInfo.split("\\s/\\s", 2);
            String orderedItemName = orderInfoSplit[0];
            MenuItem orderedMenuItem = new MenuItemImpl(orderedItemName, itemQuantity);

            // If there are and modifiers:
            if (orderInfoSplit.length > 1) {
                String[] orderedModifiers = orderInfoSplit[1].split("\\s");
                List<String> limitedModifiers = new ArrayList<>();
                for (String mod : orderedModifiers) {
                    if (limitedModifiers.size() < 5) {
                        limitedModifiers.add(mod);
                    }
                }
                for (String modifier : limitedModifiers) {
                    String ingredientName = modifier.substring(1);
                    if (modifier.startsWith("+")) {
                        orderedMenuItem.addExtraIngredient(new IngredientImpl(ingredientName));
                    } else if (modifier.startsWith("-")) {
                        orderedMenuItem.removeIngredient(new IngredientImpl(ingredientName));
                    } else {
                        throw new IllegalArgumentException("Invalid character found in mod: " + modifier);
                    }
                }
            }
            orderItems.add(orderedMenuItem);
        }
        return new OrderImpl(orderItems);
    }

    /**
     * Return the type of this Event (example: order, delivered).
     *
     * @return the type of this Event
     */
    EventType getType() {
        return EventType.fromString(type.toString());
    }

    Order getOrder() {
        return new OrderImpl(order.getItems());
    }

    Map<Ingredient, Integer> getShipment() {
        return shipment;
    }

    /**
     * Return the Table which this Event belongs to.
     *
     * @return the ID of the Table which this Event belongs to
     */
    int getTableId() {
        return tableId;
    }

    /**
     * Return an Order corresponding to this Event's deductions.
     *
     * @return the ordered MenuItem deductions
     */
    Order getDeductions() {
        return new OrderImpl(deductions.getItems());
    }
}
