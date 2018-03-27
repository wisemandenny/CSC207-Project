import menu.*;
import restaurant.*;
import org.junit.Test;
import restaurant.Restaurant;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BurgerMenuTest {
    Restaurant r = Restaurant.getInstance(10, 0.13);
    @Test
    public void testGetMenuItem(){
        r.start();
        Ingredient burgerBun = IngredientFactory.makeIngredient("BurgerBun");
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient lettuce = IngredientFactory.makeIngredient("Lettuce");
        Ingredient tomato = IngredientFactory.makeIngredient("Tomato");
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient mustard = IngredientFactory.makeIngredient("Mustard");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        MenuItem item = MenuItemFactory.makeMenuItem("Hamburger", 7.99, hamburger);

        Menu menu = new BurgerMenu();
        assertTrue(menu.getMenuItem("Hamburger").equals(item));
    }

    @Test
    public void testGetMenuIngredient(){
        Ingredient potato = IngredientFactory.makeIngredient("Potato");
        Menu menu = new BurgerMenu();
        assertEquals(potato, menu.getMenuIngredient("Potato"));
        Ingredient hotdog = IngredientFactory.makeIngredient("Hotdog");
        Ingredient bun = IngredientFactory.makeIngredient("HotdogBun");
        List<Ingredient> l = Arrays.asList(hotdog, bun);
        MenuItem item = MenuItemFactory.makeMenuItem("Hotdog", 1.50, l);
        item.removeIngredient(hotdog);
        assertTrue(item.getRemovedIngredients().size() == 1);
        assertTrue(item.getRemovedIngredients().contains(hotdog));
        assertTrue(menu.getMenuIngredient("Hotdog").equals(hotdog));
    }
}