package restaurant;


import menu.Menu;


public class InventoryFactory {
    private InventoryFactory(){}

    /**
     * Returns an Inventory based on the menu.
     *
     * @param menu the menu.
     * @return  Inventory.
     */
    public static Inventory makeInventory(Menu menu){
        return new InventoryImpl(menu);
    }
}