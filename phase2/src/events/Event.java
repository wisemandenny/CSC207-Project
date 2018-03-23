package events;

public interface Event{
    EventType getType();

    void doEvent();
}
