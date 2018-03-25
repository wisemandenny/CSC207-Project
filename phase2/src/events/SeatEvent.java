package events;

import restaurant.Restaurant;

class SeatEvent implements Event{
    private int tableId;
    private int amount;
    private EventType type;

    SeatEvent(int tableId, int amount, char flag) {
        this.tableId = tableId;
        this.amount = amount;
        type = flag == '+' ? EventType.ADDSEAT : EventType.REMOVESEAT;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void doEvent() {
        if(type.equals(EventType.ADDSEAT)) Restaurant.getInstance().getTable(tableId).addSeats(amount);
        else Restaurant.getInstance().getTable(tableId).removeSeats(amount);
    }
}
