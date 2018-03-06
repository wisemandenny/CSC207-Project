import java.util.List;

public interface Order {
    List<MenuItem> getItems();
    //Order makeOrder(Order o);

    List<Ingredient> getIngredients();

    void receivedByCook();
    void readyForPickup();
    void delivered();
}
