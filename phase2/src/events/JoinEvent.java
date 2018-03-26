package events;

import restaurant.Restaurant;

public class JoinEvent implements Event{
    int tableId;
    JoinEvent(int tableId){
        this.tableId = tableId;
    }
    @Override
    public EventType getType() {
        return EventType.JOIN;
    }

    @Override
    public void doEvent() {
        Restaurant.getInstance().getTable(tableId).joinCheques();
    }
}
