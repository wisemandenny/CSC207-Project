package events;

import restaurant.Table;

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
    }

    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
