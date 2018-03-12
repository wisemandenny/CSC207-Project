package events;

import menu.MenuItem;
import restaurant.Order;
import restaurant.OrderImpl;
import restaurant.Restaurant;
import restaurant.Table;

public class ReturnEvent implements Event {
    private final Order o;

    ReturnEvent(Table table, String order, String reasonList) {
        //set the price of the item in the order to be 0, then add the reason somewhere
        o = new OrderImpl(order);
        o.setTableId(table.getId());

        int index = 0;
        String[] comments = new String[o.getItems().size()];
        for (String itemComment : reasonList.split(",\\s")) {
            comments[index++] = itemComment;
        }
        index = 0;
        for (MenuItem item : o.getItems()) {
            item.setComment(comments[index++]);
        }
    }

    @Override
    public EventType getType() {
        return EventType.SERVERRETURNED;
    }

    @Override
    public void doEvent() {
        Restaurant.getTable(o.getTableId()).removeFromBill(o);
    }
}
