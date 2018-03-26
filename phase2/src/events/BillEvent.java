package events;

import restaurant.RestaurantLogger;
import restaurant.Table;

import java.util.logging.Level;

public class BillEvent implements Event {
    private Table table;

    BillEvent(Table table) {
        this.table = table;
    }

    @Override
    public void doEvent() {
        RestaurantLogger.log(Level.INFO, table.getTableBillString());
    }

    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
