
import menu.BurgerMenu;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;

import org.junit.Test;

import static menu.IngredientFactory.makeIngredient;
import static menu.MenuItemFactory.makeMenuItem;
import static org.junit.Assert.*;
import static restaurant.InventoryFactory.makeInventory;
import static restaurant.OrderFactory.makeOrder;

import restaurant.Inventory;
import restaurant.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryImplTest {
    private static final Menu menu = new BurgerMenu();

    @Test
    public void testAddToInventory(){
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
        Inventory inventory = makeInventory(menu);
        List<MenuItem> items = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        MenuItem item = makeMenuItem("Nona's pizza", 100.00, ingredients);
        items.add(item);
        Order o = makeOrder(items, 19, 77);
    }

    @Test
    public void testGetContents(){
    }

    @Test
    public void testMakeRestockRequest(){
    }

    @Test
    public void testPrintContents(){
    }




}