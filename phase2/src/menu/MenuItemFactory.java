package menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItemFactory {
    private MenuItemFactory() {
    }

    /**
     * Returns a MenuItem with the attributes of item.
     *
     * @param item the MenuItem that the returned MenuItem is based on.
     * @return  a MenuItem based on item
     */
    public static MenuItem makeMenuItem(MenuItem item) {
        return new MenuItemImpl(item.getName(), item.getPrice(), item.getIngredients(), new ArrayList<>(), new ArrayList<>(), 1);
    }

    /**
     * Returns a MenuItem with the name: name, price: price, consisting of Ingredients: ingredients.
     * @param name the name of this MenuItem.
     * @param price the price of this MenuItem.
     * @param ingredients the Ingredients in this MenuItem.
     * @return  a MenuItem with these attributes.
     */
    public static MenuItem makeMenuItem(String name, double price, List<Ingredient> ingredients) {
        return new MenuItemImpl(name, price, ingredients, new ArrayList<>(), new ArrayList<>(), 1);
    }
}