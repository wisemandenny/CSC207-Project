package menu;

import java.util.ArrayList;
import java.util.List;

public class MenuItemFactory {
    private MenuItemFactory() {
    }

    public static MenuItem makeMenuItem(MenuItem item) {
        return new MenuItemImpl(item.getName(), item.getPrice(), item.getIngredients(), item.getExtraIngredients(), item.getRemovedIngredients(), 1);
    }

    public static MenuItem makeMenuItem(String name, double price, List<Ingredient> ingredients) {
        return new MenuItemImpl(name, price, ingredients, new ArrayList<>(), new ArrayList<>(), 1);
    }
}