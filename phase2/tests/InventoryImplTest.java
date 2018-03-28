import menu.BurgerMenu;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;
import org.junit.Test;
import restaurant.Inventory;
import restaurant.Order;
import restaurant.Restaurant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static menu.IngredientFactory.makeIngredient;
import static menu.MenuItemFactory.makeMenuItem;
import static org.junit.Assert.*;
import static restaurant.InventoryFactory.makeInventory;
import static restaurant.OrderFactory.makeOrder;

public class InventoryImplTest {
    private static final Menu menu = new BurgerMenu();

    Restaurant r = Restaurant.getInstance(10, 0.13);
    @Test
    public void testAddToInventory(){
        r.start();
        Inventory inventory = makeInventory(menu);
        Ingredient i =  makeIngredient("Tomato");
        int currValue = inventory.getContents().get(i);
        Map<Ingredient, Integer> mocShip = new HashMap<>();
        mocShip.put(i, 1);
        inventory.addToInventory(mocShip);

        assertTrue(currValue == inventory.getContents().get(i) - 1);
    }

    @Test
    public void testRemoveFromInventory(){
        //This block of code tests the second instance of removeFromInventory
        Inventory inventory = makeInventory(menu);
        Ingredient ingredient = makeIngredient("Tomato");
        int currValue = inventory.getContents().get(ingredient);
        inventory.removeFromInventory(ingredient);

        assertTrue(inventory.getContents().get(ingredient) == currValue - 1);
        assertFalse(inventory.getContents().get(ingredient) == currValue);

        //This block of code tests the first instance of removeFromInventory
        List<MenuItem> items = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(makeIngredient("Tomato"));
        MenuItem item = makeMenuItem("Hamburger", 100.00, ingredients);
        items.add(item);
        Order o = makeOrder(items, 19, 77, 1);
        currValue = inventory.getContents().get(ingredient);
        inventory.removeFromInventory(o);

        assertTrue(inventory.getContents().get(ingredient) == currValue - 1);
        assertFalse(inventory.getContents().get(ingredient) == currValue);
    }

    @Test
    public void testMakeRestockRequest (){
        Inventory inventory = makeInventory(menu);
        Ingredient ingredient = makeIngredient("Tomato");
        inventory.makeRestockRequest();

        String fileName = "requests.txt";
        String line;
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null) {
                assertEquals("We need 10 more Tomato.", line);
            }
            br.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
/*
    @Test
    public void testPrintContents(){
        Inventory inventory = makeInventory(menu);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        inventory.printContents();
        String expectedOutput = "";

        for(Map.Entry<Ingredient, Integer> entry : inventory.getContents().entrySet()){
            expectedOutput += "Name: " +entry.getKey().getName() + "       Quantity: " + entry.getValue() + "\n";
        }

        assertEquals(expectedOutput, outContent.toString());
    }*/
}