package menu;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BurgerMenuTest {

    @Test
    public void testGetMenuItem(){
        Ingredient burgerBun = new IngredientImpl("BurgerBun", 0.50);
        Ingredient patty = new IngredientImpl("Patty", 2.00);
        Ingredient lettuce = new IngredientImpl("Lettuce", 0.50);
        Ingredient tomato = new IngredientImpl("Tomato", 0.50);
        Ingredient ketchup = new IngredientImpl("Ketchup", 0.10);
        Ingredient mustard = new IngredientImpl("Mustard", 0.10);
        Ingredient bacon = new IngredientImpl("Bacon", 3.00);
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        MenuItem item = new MenuItemImpl("Hamburger", 7.99, hamburger);

        Menu menu = new BurgerMenu();
        assertTrue(menu.getMenuItem("Hamburger").equals(item));
    }

    @Test
    public void testGetMenuIngredient(){
        Ingredient potato = new IngredientImpl("Potato", 0.6);
        Menu menu = new BurgerMenu();
        assertEquals(potato, menu.getMenuIngredient("Potato"));
    }
    // TODO: test for when they fail? How does junit handle an exception?
}