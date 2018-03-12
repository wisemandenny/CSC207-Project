package events;

public class ReturnEvent implements Event {

    @Override
    public EventType getType() {
        return EventType.SERVERRETURNED;
    }

    @Override
    public void doEvent() {

    }
}
