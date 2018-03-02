public interface FoodMod {
    String getName();
    double getPrice();
    Ingredient getIngredient();
    void addTo(MenuItemImpl item);
    void removeFrom(MenuItemImpl item);
}

