import java.util.List;

public interface Order {
    List<MenuItem> getItems();

    List<Ingredient> getIngredients();

    void receivedByCook();

    void readyForPickup();

    void delivered();

    void returned();
}
