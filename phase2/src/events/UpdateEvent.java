package events;

import restaurant.Restaurant;

public class UpdateEvent implements Event {
    private final EventType type;
    private final int orderId;

    UpdateEvent(EventType type, int orderId) {
        this.type = type;
        this.orderId = orderId;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void doEvent() {
        switch (type) {
            case COOKSEEN:
                Restaurant.getInstance().addCookingOrder(orderId);
                break;
            case COOKREADY:
                Restaurant.getInstance().addReadyOrder(orderId);
                break;
            case SERVERDELIVERED:
                Restaurant.getInstance().addDeliveredOrder(orderId);
                break;
            default:
                throw new IllegalStateException("Invalid type of Update Event"+ type);
        }
    }


}
