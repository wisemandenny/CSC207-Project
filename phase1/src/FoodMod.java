import java.util.ArrayList;

public interface FoodMod {
    String getName();
    double getPrice();
    String getType();
    void setType(String type);
    Ingredient getIngredient();
//    void addTo(MenuItem item);
//    void removeFrom(MenuItem item);
}

