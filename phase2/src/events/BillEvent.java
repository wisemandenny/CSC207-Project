package events;

import restaurant.Table;

public class BillEvent implements Event {
    private final Table table;

    BillEvent(Table table) {
        this.table = table;
    }

    @Override
    public void doEvent() {
        table.printBill();
    }

    @Override
    public EventType getType() {
        return EventType.BILL;
    }
}
