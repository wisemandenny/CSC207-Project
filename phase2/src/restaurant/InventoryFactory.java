package restaurant;


import menu.Menu;

import restaurant.Restaurant;

public class InventoryFactory {
    private InventoryFactory(){}

    public static Inventory makeInventory(Menu menu){
        return new InventoryImpl(menu);
    }
}
