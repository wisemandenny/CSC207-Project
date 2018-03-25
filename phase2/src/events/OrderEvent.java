package events;

import restaurant.Order;
import restaurant.OrderImpl;
import restaurant.Restaurant;
import restaurant.Table;


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
    }

    Order getOrder() {
        return order;
    }
}

