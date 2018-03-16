package menu;

import menu.Ingredient;
import menu.IngredientImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientImplTest {

    @Test
    public void testGetName(){
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        assertTrue(patty.getName().equals("Patty"));
        assertFalse(patty.getName().equals("patty"));
    }

    @Test
    public void testGetPrice(){
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        assertTrue(patty.getPrice() == 3.00);
        assertFalse(patty.getPrice() > 3.00);
        assertFalse(patty.getPrice() < 3.00);
        patty.setPrice(5.00);
        assertTrue(patty.getPrice() == 5.00);
    }

    @Test
    public void TestEquals(){
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        Ingredient other = new IngredientImpl("Patty", 3.00);
        Ingredient fruit = new IngredientImpl("fruit", 1.00);

        assertTrue(patty.equals(other));
        assertFalse(patty.equals(fruit));
    }

}