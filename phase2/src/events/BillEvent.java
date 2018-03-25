package events;

import restaurant.RestaurantLogger;
import restaurant.Table;

import java.util.logging.Level;

public class BillEvent implements Event {
    private Table table;
    private int seat;

    BillEvent(Table table, int seat) {
        this.table = table;
        this.seat = seat;
    }

    @Override
    public void doEvent() {
        //table.getSeat(seat).getBillString()
        RestaurantLogger.log(Level.INFO, table.toString() + "billed");
    }

    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
