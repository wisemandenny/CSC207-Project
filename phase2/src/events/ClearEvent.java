package events;

import restaurant.Bill;
import restaurant.Restaurant;
import restaurant.Table;

public class ClearEvent implements Event {
    private int tableId;

    ClearEvent(int tableId){
        this.tableId = tableId;
    }
    @Override
    public EventType getType() {
        return EventType.CLEAR;
    }

    @Override
    public void doEvent() {
        Table table = Restaurant.getInstance().getTable(tableId);
        Bill tableBill = table.getBill();
        if(tableBill.getPaidAmount() >= tableBill.getTotal()){
            table.resetTable();
        }
        //TODO: log that this table was cleared (reset)
    }
}
