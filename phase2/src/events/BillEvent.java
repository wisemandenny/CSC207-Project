package events;

import menu.MenuItem;
import restaurant.Order;
import restaurant.RestaurantLogger;
import restaurant.Table;

import java.util.logging.Level;

public class BillEvent implements Event {
    private Table table;

    BillEvent(Table table) {
        this.table = table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doEvent() {
        StringBuilder s = new StringBuilder();
        s.append("Bill for table ");
        s.append(Integer.toString(table.getId()));
        s.append(":\n");
        for (Order o: table.getOrders()){
            for (MenuItem i: o.getItems()){
                s.append(Integer.toString(i.getQuantity()));
                s.append(" ");
                s.append(i.getName());
                s.append(" ");
                s.append(String.valueOf(i.getPrice()));
                s.append("\n");
            }
        }
        s.append("Subtotal: ");
        s.append(String.valueOf(table.getBill().getSubtotal()));
        s.append("\n");
        s.append("Total: ");
        s.append(String.valueOf(table.getBill().getTotal()));

        RestaurantLogger.log(Level.INFO, s.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
