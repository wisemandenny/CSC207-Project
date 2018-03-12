//TODO: delete this file once everything has been taken out from it.


/*import events.Event;
import events.EventType;
import menu.Ingredient;
import menu.IngredientImpl;
import menu.MenuItem;
import restaurant.Order;
import restaurant.OrderImpl;

import java.util.HashMap;
import java.util.Map;

class BaseEvent implements Event {
    private final EventType type;
    private final Map<Ingredient, Integer> shipment = new HashMap<>();
    private int tableId;
    private Order order;
    private Order deductions;

    BaseEvent(EventType type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    BaseEvent(EventType type, int tableId, String order) {
        this.type = type;
        this.tableId = tableId;
        if (type.equals(EventType.ORDER)) {
            this.order = orderConstructorHelper(order);
        }
    }

    BaseEvent(EventType type, int tableId, String itemList, String commentList) {
        this.type = type;
        this.tableId = tableId;
        deductions = orderConstructorHelper(itemList);
        commentSetter(commentList);
    }

    BaseEvent(EventType type, String shipment) {
        this.type = type;
        String[] info = shipment.split(", ");
        for (String ingredient : info) {
            String[] ingredientWithQuantity = ingredient.split("\\s");
            int quantity = Integer.parseInt(ingredientWithQuantity[0]);
            Ingredient i = new IngredientImpl(ingredientWithQuantity[1]);
            this.shipment.put(i, quantity);
        }
    }



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


    EventType getType() {
        return EventType.fromString(type.toString());
    }

    Order getOrder() {
        return new OrderImpl(order.getItems());
    }

    Map<Ingredient, Integer> getShipment() {
        return shipment;
    }


    int getTableId() {
        return tableId;
    }


    Order getDeductions() {
        return new OrderImpl(deductions.getItems());
    }
}*/
