package events;

import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.util.logging.Level;

class SeatEvent implements Event{
    private int tableId;
    private int seatNumber;
    private EventType type;

    SeatEvent(int tableId) {
        this.tableId = tableId;
        type = EventType.ADDSEAT;
    }

    SeatEvent(int tableId, int seatNumber){
        this.tableId = tableId;
        this.seatNumber = seatNumber;
        type = EventType.REMOVESEAT;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void doEvent() {
        StringBuilder s = new StringBuilder();
        if(type.equals(EventType.ADDSEAT)) {
            Restaurant.getInstance().getTable(tableId).addSeat();
            s.append("Added seat number ");
            s.append(Integer.toString(seatNumber));
            s.append(" to table ");
            s.append(Integer.toString(tableId));
            s.append(".");
        }
        else{
            Restaurant.getInstance().getTable(tableId).removeSeat(seatNumber);
            s.append("Removed seat number ");
            s.append(Integer.toString(seatNumber));
            s.append(" from table ");
            s.append(Integer.toString(tableId));
            s.append(".");
        }

        RestaurantLogger.log(Level.INFO,  s.toString());
    }
}
