package events;

import restaurant.*;

import java.util.logging.Level;

class OrderEvent implements Event {
    private final Order order;
    private final Table table;

    OrderEvent(Table table, int seat, String orderString) {
        this.table = table;
        order = OrderFactory.makeOrder(orderString, table.getId(), seat);
    }

    @Override
    public EventType getType() {
        return EventType.ORDER;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().addPlacedOrder(order);
        RestaurantLogger.log(Level.INFO, "Table " + String.valueOf(order.getTableId()) + " has ordered " +
                order.toString() + ". Order number " + String.valueOf(order.getId()));
    }

    Order getOrder() {
        return order;
    }
}

