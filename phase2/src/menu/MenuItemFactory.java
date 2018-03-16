package menu;

import java.util.Collections;
import java.util.List;

public class MenuItemFactory {
    private MenuItemFactory(){
    }

    public static MenuItem makeMenuItem(MenuItem item){
        return new MenuItemImpl(item.getName(), item.getPrice(), item.getIngredients(), item.getExtraIngredients(), item.getRemovedIngredients(), 1);
    }

    public static MenuItem makeMenuItem(String name, double price, List<Ingredient> ingredients){
        final List<Ingredient> emptyList = Collections.emptyList();
        return new MenuItemImpl(name, price, ingredients, emptyList, emptyList, 1);
    }
}
