public interface FoodMod {
    String getName();
    double getPrice();
    Ingredient getIngredient();

    void addTo(MenuItem item);

    void removeFrom(MenuItem item);
}

