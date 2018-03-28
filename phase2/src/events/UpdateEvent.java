package events;

import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.util.logging.Level;

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
                Restaurant.getInstance().addReceivedOrder(orderId);
                break;
            case COOKFIRED:
                Restaurant.getInstance().addCookingOrder(orderId);
                RestaurantLogger.log(Level.INFO, "Order number " + Integer.toString(orderId) +  " prepared!");
                break;
            case COOKREADY:
                Restaurant.getInstance().addReadyOrder(orderId);
                RestaurantLogger.log(Level.INFO, "Order number " + Integer.toString(orderId) +  " ready!");
                break;
            case SERVERDELIVERED:
                Restaurant.getInstance().addDeliveredOrder(orderId);
                RestaurantLogger.log(Level.INFO, "Order number " + Integer.toString(orderId) +  " delivered!");
                break;
            default:
                throw new IllegalStateException("Invalid type of Update Event"+ type);
        }
    }


}
