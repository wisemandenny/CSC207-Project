import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Event {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int NAME_ADDRESS = 1;
    private final EventType type;
    private final int tableId;
    private Order order;
    private List<MenuItem> deduction;

    Event(EventType type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    Event(EventType type, int tableId, String order) {
        this.type = type;
        this.tableId = tableId;
        if (type.equals(EventType.ORDER)) {
            orderConstructorHelper(order);
        }
    }

    Event(EventType type, int tableId, String itemList, String commentList) {
        this.type = type;
        this.tableId = tableId;
        itemBuilder(itemList);
        commentSetter(commentList);
    }


    private void commentSetter(String commentList) {
        int count = 0;
        // make comments list
        String[] comments = new String[deduction.size()];
        // add every comment to the comment list
        for (String itemComment : commentList.split(",\\s")) {
            comments[count++] = itemComment;
        }
        // assign each comment to each returned menu item
        count = 0;
        for (MenuItem item : deduction) {
            item.setComment(comments[count++]);
        }
    }

    private void itemBuilder(String strings) {
        deduction = new ArrayList<>();
        if (strings.length() < 2) {
            throw new IllegalArgumentException("Invalid strings length in Event.itemBuilder");
        }
        for (String item : strings.split(",\\s")) {
            String[] itemSplitString = item.split("\\s", 2);
            int quantity = Integer.parseInt(itemSplitString[Event.QUANTITY_ADDRESS]);
            String itemName = itemSplitString[Event.NAME_ADDRESS];
            // make a menu item and add it to the deduction list
            deduction.add(new MenuItemImpl(itemName, quantity));
        }
    }

    private void orderConstructorHelper(String strings) {
        List<MenuItem> orderItems = new ArrayList<>();
        for (String item : strings.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItemSplitString = item.split("\\s", 2);
            int itemQuantity = Integer.parseInt(orderItemSplitString[0]);
            String orderInfo = orderItemSplitString[1];

            // Separate the ordered item from any modifiers
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
        order = new OrderImpl(orderItems);
    }

    EventType getType() {
        return EventType.fromString(type.toString());
    }

    Order getOrder() {
        return new OrderImpl(order.getItems());
    }

    int getTableId() {
        return tableId;
    }

    List<MenuItem> getDeductions() {
        return new ArrayList<>(deduction);
    }
}
