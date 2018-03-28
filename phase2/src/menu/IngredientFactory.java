package menu;

import restaurant.Restaurant;

public class IngredientFactory {
    private IngredientFactory() {
    }

    /**
     * Returns an Ingredient with name: name.
     *
     * @param name the name of this Ingredient.
     * @return  an Ingredient.
     */
    public static Ingredient makeIngredient(String name) {
        return new IngredientImpl(Restaurant.getInstance().getMenu().getMenuIngredient(name));
    }
}
