// TODO: write these tests

import menu.BurgerMenu;
import menu.Ingredient;
import menu.Menu;

import org.junit.Test;

import static menu.IngredientFactory.makeIngredient;
import static org.junit.Assert.*;
import static restaurant.InventoryFactory.makeInventory;

import restaurant.Inventory;

import java.util.HashMap;
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