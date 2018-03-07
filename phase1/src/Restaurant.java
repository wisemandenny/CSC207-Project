import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class Restaurant {
    private final Table[] tables;
    private final Menu menu = new BurgerMenu();
    private final Inventory inventory = new InventoryImpl(menu);

    Restaurant(int numOfTables) {
        tables = new Table[numOfTables + 1];
        for (int i = 1; i <= numOfTables; i++) {
            tables[i] = new Table(i);
        }
        EventManager eventManager = new EventManager();
        handleEvents(eventManager);
    }

    private void printBillForTable(int tableId) {
        tables[tableId].printBill();
    }

    private void addOrderToBill(int tableId, Order order) {
        Table table = tables[tableId];
        table.addToBill(order);
    }

    private void addToInventory(Ingredient ingredient, int amount) {
        inventory.addToInventory(ingredient, amount);
    }

    private List<FoodMod> getMenuOrderedMods(List<FoodMod> orderedMods) {
        List<FoodMod> newMods = new ArrayList<>();
        for(FoodMod mod : orderedMods) {
            // finds the FoodMod in the BurgerMenu.modsMenu with the same name as mod.
            FoodMod menuModToAdd = menu.getFoodMod(mod);
            menuModToAdd.setType(mod.getType());
            newMods.add(menuModToAdd);
        }
        return newMods;
    }

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();

        while (!events.isEmpty()) {
            Event e = events.remove();
            Order tableOrder = tables[e.getTableId()].getOrder();
            int tableId = e.getTableId();

            switch (e.getType()) {
                case ORDER:
                    Order order = e.getOrder();
                    List<MenuItem> newOrder = new ArrayList<>();
                    for (MenuItem item : order.getItems()) {
                        MenuItem itemToAdd = MenuItemImpl.fromRestaurantMenu(menu.getMenuItem(item), item.getQuantity());
                        List<FoodMod> modsMenuOrderedMods = getMenuOrderedMods(item.getOrderedMods());
                        item.setOrderedMods(modsMenuOrderedMods);
                        item.applyMods();
                        newOrder.add(itemToAdd);
                    }
                    tables[tableId].addOrderToTable(new OrderImpl(newOrder));
                    break;
                case BILL:
                    printBillForTable(tableId);
                    break;
                case COOKSEEN:
                    tableOrder.receivedByCook();
                    System.out.println("COOK HAS SEEN:\n" + tableOrder);
                    break;
                case COOKREADY:
                    for (MenuItem item : tableOrder.getItems()) {
                        inventory.removeFromInventory(item);
                    }
                    tableOrder.readyForPickup();
                    System.out.println("READY FOR PICKUP!\n" + tableOrder);
                    break;
                case SERVERDELIVERED:
                    tableOrder.delivered();
                    addOrderToBill(tableId, tableOrder);
                    System.out.println("DELIVERED TO TABLE " + tableId + "\n" + tableOrder);
                    break;
                case SERVERRETURNED:
                    // add all the returned items to this table
                    tableOrder.returned();
                    tables[tableId].addToDeductions(e.getDeductions());
                    // set the cost of this event
                    for(MenuItem item : e.getDeductions()){
                        double cost = menu.getMenuItem(item).getPrice();
                        cost *= item.getQuantity() * -1;
                        item.setPrice(cost);
                    }

                    System.out.println("TABLE " + tableId +
                            " HAS RETURNED THE FOLLOWING ITEM(s): \n" + tables[tableId].stringDeductions());
                    break;
                //TODO: add "receivedShipment" for when
            }
        }
    }
}
