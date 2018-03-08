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

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();

        while (!events.isEmpty()) {
            Event e = events.remove();
            Order tableOrder = tables[e.getTableId()].getOrder();
            int tableId = e.getTableId();
            Table currentTable = tables[tableId];

            switch (e.getType()) {
                case ORDER:
                    Order order = e.getOrder();
                    List<MenuItem> newOrder = new ArrayList<>();
                    for (MenuItem item : order.getItems()) {
                        MenuItem itemToAdd = new MenuItemImpl(menu.getMenuItem(item), item.getQuantity());
                        for (Ingredient ing : item.getExtraIngredients()) {
                            itemToAdd.addExtraIngredient(menu.getMenuIngredient(ing));
                        }
                        for (Ingredient ing : item.getRemovedIngredients()) {
                            itemToAdd.removeIngredient(menu.getMenuIngredient(ing));
                        }
                        newOrder.add(itemToAdd);
                    }
                    currentTable.addOrderToTable(new OrderImpl(newOrder));
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
                        inventory.removeFromInventory(item, currentTable);
                    }
                    List<MenuItem> uncookedItems = currentTable.getUncookedMenuItems();
                    for (MenuItem item : uncookedItems) {
                        currentTable.addToDeductions(item, "Out of stock. ");
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
                    for (MenuItem item : e.getDeductions().getItems()) {
                        currentTable.addToDeductions(menu.getMenuItem(item), item.getQuantity(), item.getComment());
                    }

                    System.out.println("TABLE " + tableId +
                            " HAS RETURNED THE FOLLOWING ITEM(s): \n" + currentTable.stringDeductions());
                    break;
                //TODO: add "receivedShipment" for when
            }
        }
    }
}
