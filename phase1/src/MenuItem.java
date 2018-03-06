import java.util.ArrayList;
import java.util.List;

public interface MenuItem {
    // MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity);
    String getName();
    double getPrice();

    int getQuantity();
    double getModPrice();
    List<FoodMod> getMods();
    void addMod(FoodMod mod);
    void removeMod(FoodMod mod);
    void increaseModPrice(FoodMod mod);
    void decreaseModPrice(FoodMod mod);
    List<Ingredient> getIngredients();
    String printIngredients();
    boolean equals(MenuItem item);
}
