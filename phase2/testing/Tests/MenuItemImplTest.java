package menu;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MenuItemImplTest {

    private MenuItem generateMenuItem(){
        Ingredient burgerBun = new IngredientImpl("BurgerBun", 0.50);
        Ingredient patty = new IngredientImpl("Patty", 2.00);
        Ingredient lettuce = new IngredientImpl("Lettuce", 0.50);
        Ingredient tomato = new IngredientImpl("Tomato", 0.50);
        Ingredient ketchup = new IngredientImpl("Ketchup", 0.10);
        Ingredient mustard = new IngredientImpl("Mustard", 0.10);
        Ingredient bacon = new IngredientImpl("Bacon", 3.00);
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        return new MenuItemImpl("Hamburger", 7.99, hamburger);
    }


    @Test
    public void testGetName(){
        MenuItem item = generateMenuItem();
        assertEquals("Hamburger", item.getName());
    }
    @Test
    public void testGetQuantity(){
        MenuItem item = generateMenuItem();
        assertEquals(1, item.getQuantity());
    }

    @Test
    public void testSetQuantity(){
        MenuItem item = generateMenuItem();
        item.setQuantity(5);
        assertEquals(5, item.getQuantity());
    }

    @Test
    public void testFalseQuantity(){
        MenuItem item = generateMenuItem();
        item.setQuantity(5);
        assertFalse(item.getQuantity() == 3);
    }

    @Test
    public void testGetPrice() {
        MenuItem item = generateMenuItem();
        assertTrue(item.getPrice() == 7.99);
    }

    @Test
    public void testGetIncorrectPrice() {
        MenuItem item = generateMenuItem();
        assertFalse(item.getPrice() == 3.99);
    }

    @Test
    public void testSetPrice() {
        MenuItem item = generateMenuItem();
        item.setPrice(5.99);
        assertTrue(item.getPrice() == 5.99);
    }
    @Test
    public void testIncorrectSetPrice() {
        MenuItem item = generateMenuItem();
        item.setPrice(5.99);
        assertFalse(item.getPrice() == 7.99);
    }


    @Test
    public void testGetTotal() {
        MenuItem item = generateMenuItem();
        assertTrue(item.getTotal() == 7.99);
        assertTrue(item.getTotal() == item.getQuantity() * item.getPrice());
    }

    @Test
    public void testIncorrectTotal() {
        MenuItem item = generateMenuItem();
        assertFalse(item.getTotal() == 5);
    }

    @Test
    public void testAlteredTotal() {
        MenuItem item = generateMenuItem();
        item.setQuantity(6);
        assertTrue(item.getTotal() == 47.94);
        assertTrue(item.getTotal() == 6 * item.getPrice());
    }

    @Test
    public void testAddExtraIngredient(){
        MenuItem item = generateMenuItem();
        assertTrue(item.getIngredients().size() == 7);
        item.addExtraIngredient(new IngredientImpl("Bacon", 2.00));
        assertTrue(item.getExtraIngredients().size() == 1);
    }


    @Test
    public void testRemoveIngredient(){
        MenuItem item = generateMenuItem();
        item.addExtraIngredient(new IngredientImpl("Bacon", 3.00));
        assertTrue(item.getExtraIngredients().size() == 1);
        item.removeIngredient(new IngredientImpl("Bacon", 3.00));
        assertTrue(item.getExtraIngredients().size() == 1);
        item.removeIngredient(new IngredientImpl("Lettuce", 0.50));
        assertTrue(item.getRemovedIngredients().size() == 2);
    }

    @Test
    public void testGetExtraIngredientPrice(){
        MenuItem item = generateMenuItem();
        assertTrue(item.getExtraIngredientPrice() == 0);

    }

    @Test
    public void testAdditionalGetExtraIngredientPrice(){
        MenuItem item = generateMenuItem();
        item.addExtraIngredient(new IngredientImpl("Bacon", 3.00));
        assertTrue(item.getExtraIngredientPrice() == 3.00);
        item.removeIngredient(new IngredientImpl("Bacon", 3.00));
        assertTrue(item.getExtraIngredientPrice() == 3.00);
        assertTrue(item.getRemovedIngredientsPrice() == 3.00);
    }

    @Test
    public void testIncorrectAdditionalGetExtraIngredientPrice(){
        MenuItem item = generateMenuItem();
        item.addExtraIngredient(new IngredientImpl("Bacon", 3.00));
        assertFalse(item.getExtraIngredientPrice() == 0.00);
    }


    @Test
    public void testGetAndSetComment(){
        MenuItem item = generateMenuItem();
        item.setComment("This is a comment");
        String comm = "This is a comment";
        assertTrue(item.getComment().length() > 0);
        assertEquals(item.getComment(), comm);
    }

    @Test
    public void testGetIngredients(){
        MenuItem item = generateMenuItem();
        assertTrue(item.getIngredients().size() > 0);
        assertTrue(item.getIngredients().size() == 7);
    }


    @Test
    public void testEquals(){
        MenuItem item = generateMenuItem();
        Ingredient burgerBun = new IngredientImpl("BurgerBun", 0.50);
        Ingredient patty = new IngredientImpl("Patty", 2.00);
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty);
        MenuItem other = new MenuItemImpl("Hamburger", 2.50, hamburger);
        assertTrue(item.equals(other));
    }


    @Test
    public void testNotEquals(){
        MenuItem item = generateMenuItem();
        Ingredient burgerBun = new IngredientImpl("BurgerBun", 0.50);
        Ingredient patty = new IngredientImpl("veganPatty", 3.00);
        List<Ingredient> plain = Arrays.asList(burgerBun, patty);
        MenuItem plainPatty = new MenuItemImpl("VeganBurger", 3.50, plain);
        assertFalse(item == plainPatty);
    }


    @Test
    public void testGetExtraIngredients(){
        MenuItem item = generateMenuItem();
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        Ingredient cheese = new IngredientImpl("Cheese", 0.50);
        item.addExtraIngredient(patty);
        item.addExtraIngredient(cheese);
        assertEquals(2, item.getExtraIngredients().size());
        List<Ingredient> items = Arrays.asList(patty, cheese);
        assertTrue(item.getExtraIngredients().containsAll(items));
    }

    @Test
    public void testGetRemovedIngredients(){
        MenuItem item = generateMenuItem();
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        Ingredient cheese = new IngredientImpl("Cheese", 0.50);
        item.addExtraIngredient(patty);
        item.addExtraIngredient(cheese);
        assertEquals(2, item.getExtraIngredients().size());
        item.removeIngredient(patty);
        List<Ingredient> removed = Arrays.asList(patty);
        assertTrue(item.getRemovedIngredients().size() == 1);
        assertEquals(item.getRemovedIngredients(), removed);
    }

    @Test
    public void testGetRemovedIngredientsPrice(){
        MenuItem item = generateMenuItem();
        Ingredient patty = new IngredientImpl("Patty", 3.00);
        Ingredient cheese = new IngredientImpl("Cheese", 0.50);
        Ingredient tomato = new IngredientImpl("Tomato", 0.50);
        item.addExtraIngredient(patty);
        item.addExtraIngredient(cheese);
        assertTrue(item.getRemovedIngredientsPrice() == 0.00);
        item.removeIngredient(patty);
        item.removeIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 3.50);
        item.addExtraIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 3.50);
        item.removeIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 4.00);
    }
}