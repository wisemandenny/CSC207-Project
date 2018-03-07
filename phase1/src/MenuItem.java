import java.util.ArrayList;
import java.util.List;

public interface MenuItem {
    // MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity);
    String getName();
    double getPrice();

    int getQuantity();
    double getModPrice();
    List<FoodMod> getMods();
    List<FoodMod> getOrderedMods();
    void setOrderedMods(List<FoodMod> mods);
    void orderMod(FoodMod mod);
    void applyMods();
    List<FoodMod> getAllowedMods();
    void increaseModPrice(FoodMod mod);
    void decreaseModPrice(FoodMod mod);
    void setComment(String comment);
    String getComment();
    void setPrice(double price);
    List<Ingredient> getIngredients();
    String printIngredients();
    boolean equals(MenuItem item);
}
