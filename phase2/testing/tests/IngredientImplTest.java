package tests;
import menu.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientImplTest {

    @Test
    public void testGetName(){
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        assertTrue(patty.getName().equals("Patty"));
        assertFalse(patty.getName().equals("patty"));
    }

    @Test
    public void testGetPrice(){
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        assertTrue(patty.getPrice() == 2.00);
        assertFalse(patty.getPrice() > 2.00);
        assertFalse(patty.getPrice() < 2.00);
        patty.setPrice(5.00);
        assertTrue(patty.getPrice() == 5.00);
    }

    @Test
    public void TestEquals(){
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient other = IngredientFactory.makeIngredient("Patty");
        Ingredient fruit = IngredientFactory.makeIngredient("Potato");

        assertTrue(patty.equals(other));
        assertFalse(patty.equals(fruit));
    }

}