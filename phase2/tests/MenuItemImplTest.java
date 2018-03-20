import menu.Ingredient;
import menu.IngredientFactory;
import menu.MenuItem;
import menu.MenuItemFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MenuItemImplTest {

    private MenuItem generateMenuItem(){
        Ingredient burgerBun = IngredientFactory.makeIngredient("BurgerBun");
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient lettuce = IngredientFactory.makeIngredient("Lettuce");
        Ingredient tomato = IngredientFactory.makeIngredient("Tomato");
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        Ingredient mustard = IngredientFactory.makeIngredient("Mustard");
        Ingredient bacon = IngredientFactory.makeIngredient("Bacon");
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        return MenuItemFactory.makeMenuItem("Hamburger", 7.99, hamburger);
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
        item.addExtraIngredient(IngredientFactory.makeIngredient("Bacon"));
        assertTrue(item.getExtraIngredients().size() == 1);
    }


    @Test
    public void testRemoveIngredient(){
        MenuItem item = generateMenuItem();

        item.addExtraIngredient(IngredientFactory.makeIngredient("Bacon"));
        assertTrue(item.getExtraIngredients().size() == 1);
        item.removeIngredient(IngredientFactory.makeIngredient("Bacon"));
        assertTrue(item.getExtraIngredients().size() == 1);
        item.removeIngredient(IngredientFactory.makeIngredient("Lettuce"));
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
        item.addExtraIngredient(IngredientFactory.makeIngredient("Bacon"));
        assertTrue(item.getExtraIngredientPrice() == 3.00);
        item.removeIngredient(IngredientFactory.makeIngredient("Bacon"));
        assertTrue(item.getExtraIngredientPrice() == 3.00);
        assertTrue(item.getRemovedIngredientsPrice() == 3.00);
    }

    @Test
    public void testIncorrectAdditionalGetExtraIngredientPrice(){
        MenuItem item = generateMenuItem();
        item.addExtraIngredient(IngredientFactory.makeIngredient("Bacon"));
        System.out.println(item.getExtraIngredients());
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
        Ingredient burgerBun = IngredientFactory.makeIngredient("BurgerBun");
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty);
        MenuItem other = MenuItemFactory.makeMenuItem("Hamburger", 2.50, hamburger);
        assertTrue(item.equals(other));
    }


    @Test
    public void testNotEquals(){
        MenuItem item = generateMenuItem();
        Ingredient burgerBun = IngredientFactory.makeIngredient("BurgerBun");
        Ingredient ketchup = IngredientFactory.makeIngredient("Ketchup");
        List<Ingredient> plain = Arrays.asList(burgerBun, ketchup);
        MenuItem plainPatty = MenuItemFactory.makeMenuItem("VeganBurger", 3.50, plain);
        assertFalse(item == plainPatty);
    }


    @Test
    public void testGetExtraIngredients(){
        MenuItem item = generateMenuItem();
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient cheese = IngredientFactory.makeIngredient("Cheese");
        item.addExtraIngredient(patty);
        item.addExtraIngredient(cheese);
        assertEquals(2, item.getExtraIngredients().size());
        List<Ingredient> items = Arrays.asList(patty, cheese);
        assertTrue(item.getExtraIngredients().containsAll(items));
    }

    @Test
    public void testGetRemovedIngredients(){
        MenuItem item = generateMenuItem();
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient cheese = IngredientFactory.makeIngredient("Cheese");
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
        Ingredient patty = IngredientFactory.makeIngredient("Patty");
        Ingredient cheese = IngredientFactory.makeIngredient("Cheese");
        Ingredient tomato = IngredientFactory.makeIngredient("Tomato");
        item.addExtraIngredient(patty);
        item.addExtraIngredient(cheese);
        assertTrue(item.getRemovedIngredientsPrice() == 0.00);
        item.removeIngredient(patty);
        item.removeIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 2.50);
        item.addExtraIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 2.50);
        item.removeIngredient(tomato);
        assertTrue(item.getRemovedIngredientsPrice() == 3.00);
    }
}