// TODO: write these tests

import menu.BurgerMenu;
import menu.Ingredient;
import menu.IngredientImpl;
import menu.Menu;

import org.junit.Test;
import static org.junit.Assert.*;
import restaurant.Inventory;
import restaurant.InventoryImpl;
import restaurant.Order;
import restaurant.OrderImpl;

import java.util.HashMap;
import java.util.Map;

public class InventoryImplTest {
    private static final Menu menu = new BurgerMenu();

    @Test
    public void testAddToInventory(){
        Inventory inventory = new InventoryImpl(InventoryImplTest.menu);
        Map<Ingredient, Integer> shipment = new HashMap<>();
        Ingredient i = new IngredientImpl("Tomato", 1.00) ;
        shipment.put(i,1);
        int currValue = inventory.getContents().get(i);
        inventory.addToInventory(shipment);
        assertTrue(currValue == inventory.getContents().get(i) - 1);
    }
    // inventory and ingredient made public here

    @Test
    public void testRemoveFromInventory(){
        Inventory inventory = new InventoryImpl(InventoryImplTest.menu);
        Inventory copy = new InventoryImpl(InventoryImplTest.menu);

        Order o = new OrderImpl("1 Hamburger");
        inventory.removeFromInventory(o);
        for (Ingredient i: o.getIngredients()){
            assertTrue(inventory.getContents().get(i) + 1 == copy.getContents().get(i));
        }
    }

    @Test
    public void testGetContents(){
        Inventory inventory = new InventoryImpl(InventoryImplTest.menu);

    }

    @Test
    public void testMakeRestockRequest(){

    }

    @Test
    public void testPrintContents(){
    //String compare = "Name: The magic donut" + "       Quantity: 1";
    //Map<Ingredient, Integer> inventory = new HashMap<>();
    //Ingredient i = new IngredientImpl("The magic donut", 1.00);
    //inventory.put(i, 1);
    //assertTrue(compare.equals(inventory.printContents));
    }




}