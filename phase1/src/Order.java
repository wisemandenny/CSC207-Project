import java.util.List;

public interface Order {
    List<MenuItem> getItems();
    double getTotalPrice();

    //Order makeOrder(Order o);

    List<Ingredient> getIngredients();

    void receivedByCook();
    void readyForPickup();
    void delivered();
}
