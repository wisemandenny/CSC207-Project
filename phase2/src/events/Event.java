package events;

public interface Event{
    /**
     * Returns the type of Event
     *
     * @return  the type of Event
     */
    EventType getType();

    /**
     * Processes an event and logs it.
     */
    void doEvent();
}
