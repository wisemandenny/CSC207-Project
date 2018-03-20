// TODO: write these tests

import menu.*;
import org.junit.Test;
import restaurant.Order;
import restaurant.OrderImpl;

import static org.junit.Assert.assertTrue;

public class OrderImplTest {
    private static final Menu menu = new BurgerMenu();

    @Test
    public void testGetItems(){
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
        // order
        Order order = new OrderImpl("1 Hamburger");
        assertTrue(order.getItems().size() == 1);

        // Add a new order
        Order next = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Coke");
        order.add(next);
        assertTrue(order.getItems().size() == 3);

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
    public void testRemove() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        Order burger = new OrderImpl("1 Hamburger");
        order.remove(burger);
        // we are only removing 1 Hamburger (ideally the one without mods)
        assertTrue(order.getItems().size() == 1);
        // Create a Hamburger with mods
        MenuItem modBurger = menu.getMenuItem("Hamburger");

        // Ingredients for the modBurger
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        modBurger.addExtraIngredient(ketchup);
        modBurger.removeIngredient(bacon);

        // Check if the modBurger is still in the order
        assertTrue(order.getItems().contains(modBurger));

    }

    @Test
    public void testAddThenRemove(){

    }

    @Test
    public void testGetId() {
       Order first = new OrderImpl("1 Hamburger") ;
       Order second = new OrderImpl("1 Hamburger") ;
       Order third = new OrderImpl("1 Hamburger") ;
       assertTrue(first.getId() == 1);
       assertTrue(second.getId() == 2);
       assertTrue(third.getId() == 3);
    }

    @Test
    public void testReturned() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        MenuItem item = MenuItemFactory.makeMenuItem(menu.getMenuItem("Hamburger"));
        order.returned(item);
        order.getItems();
    }

    @Test
    public void testGetIngredients() {
        Order order = new OrderImpl("1 Hamburger / +Ketchup -Bacon, 1 Hamburger, 1 Coke");
        assertTrue(order.getIngredients().contains(IngredientFactory.makeIngredient("BurgerBun")));
        assertTrue(order.getIngredients().size() == 15);
    }

    @Test
    public void testToString() {

    }

    @Test
    public void testOrderStringParser() {

    }
}