import java.util.ArrayList;
import java.util.List;

public interface MenuItem {
    String getName();
    double getPrice();

    int getQuantity();
    double getModPrice();
    List<FoodMod> getMods();
    void increaseModPrice(FoodMod mod);
    void decreaseModPrice(FoodMod mod);
    List<Ingredient> getIngredients();
    String printIngredients();
    boolean equals(MenuItem item);
}
