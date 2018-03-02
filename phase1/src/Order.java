import java.util.List;

public interface Order {
    List<MenuItem> getItems();
    double getTotalPrice();
    List<Ingredient> getIngredients();
    boolean isReceivedByCook();
    boolean isReadyForPickup();
    boolean isDelivered();
    void receivedByCook();
    void readyForPickup();
    void delivered();
}
