package events;

import restaurant.*;

import java.util.logging.Level;


class OrderEvent implements Event {
    private final Order order;
    private final Table table;

    OrderEvent(Table table, int seat, String orderString) {
        this.table = table;
        order = new OrderImpl(orderString);
        order.setTableId(table.getId());
        order.setSeatId(seat);
    }

    @Override
    public EventType getType() {
        return EventType.ORDER;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().addPlacedOrder(order);
        RestaurantLogger.log(Level.INFO, table.toString() + "ordered" + order.toString());
    }

    Order getOrder() {
        return order;
    }
}

