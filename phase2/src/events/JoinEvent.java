package events;

import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.util.logging.Level;

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
        RestaurantLogger.log(Level.INFO, "Table " + String.valueOf(tableId) + "has had their bill joined.");
    }
}
