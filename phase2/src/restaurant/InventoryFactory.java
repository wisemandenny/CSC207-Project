package restaurant;


import menu.Menu;


public class InventoryFactory {
    private InventoryFactory(){}

    public static Inventory makeInventory(Menu menu){
        return new InventoryImpl(menu);
    }
}