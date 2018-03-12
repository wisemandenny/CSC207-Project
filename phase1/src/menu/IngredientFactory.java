package menu;

import restaurant.Restaurant;

public class IngredientFactory {
    private IngredientFactory() {
    }

    public static Ingredient makeIngredient(String name) {
        return new IngredientImpl(Restaurant.getMenu().getMenuIngredient(name));
    }
}
