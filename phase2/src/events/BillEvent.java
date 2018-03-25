package events;

import restaurant.RestaurantLogger;
import restaurant.Table;

import java.util.logging.Level;

public class BillEvent implements Event {
    private final Table table;
    private final int seat;

    BillEvent(Table table, int seat) {
        this.table = table;
        this.seat = seat;
    }

    @Override
    public void doEvent() {
//        table.printBill();
        table.getSeat(seat).printBill();
        RestaurantLogger.log(Level.INFO, table.toString() + "billed");
    }

    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
