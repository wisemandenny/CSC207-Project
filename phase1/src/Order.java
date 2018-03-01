import java.util.List;

public interface Order {
    List<MenuItem> getItems();
    double getTotalPrice();
    List<Ingredient> getIngredients();
}
