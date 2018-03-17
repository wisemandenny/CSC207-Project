// TODO: write these tests
package tests;
import org.junit.Test;
import restaurant.*;
import menu.*;


import static org.junit.Assert.*;

public class OrderImplTest {

    @Test
    public void testGetItems(){
        // menu
        Menu menu = new BurgerMenu();
        // order
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        assertTrue(order.getItems().size() == 3);

        // Check if there is a plain hamburger in the inventory
        MenuItem burger = menu.getMenuItem("Hamburger");
        assertTrue(order.getItems().contains(burger));

        // Create a Hamburger with mods
        MenuItem modBurger = menu.getMenuItem("Hamburger");

        // Ingredients for the modBurger
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        modBurger.addExtraIngredient(ketchup);
        modBurger.removeIngredient(bacon);

        // Check if the modBurger has been added
        assertTrue(order.getItems().contains(modBurger));

        // Check if a Coke has been added.
        assertTrue(order.getItems().contains(menu.getMenuItem("Coke")));
    }

    @Test
    public void testAdd() {

    }

    @Test
    public void testRemove() {

    }

    @Test
    public void testGetId() {

    }

    @Test
    public void testGetTableId() {

    }

    @Test
    public void testReturned() {

    }

    @Test
    public void testGetIngredients() {

    }

    @Test
    public void testToString() {

    }

    @Test
    public void testOrderStringParser() {

    }
}