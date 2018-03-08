import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

class Restaurant {
    private final Table[] tables;
    private final Menu menu = new BurgerMenu();
    private final Inventory inventory = new InventoryImpl(menu);

    /**
     * Constructs a new Restaurant object.
     *
     * @param numOfTables the number of Tables this Restaurant should have.
     */
    Restaurant(int numOfTables) {
        tables = new Table[numOfTables + 1];
        for (int i = 1; i <= numOfTables; i++) {
            tables[i] = new Table(i);
        }
        EventManager eventManager = new EventManager();
        handleEvents(eventManager);
    }

    /**
     * Signals the Table to print its bill.
     *
     * @param tableId the Table who's bill should be printed
     */
    private void printBillForTable(int tableId) {
        tables[tableId].printBill();
    }

    /**
     * Adds an Order to the Table tableId's bill.
     *
     * @param tableId the Table to add an Order to
     * @param order   the Order that should be added to the Table
     */
    private void addOrderToBill(int tableId, Order order) {
        Table table = tables[tableId];
        table.addToBill(order);
    }

    // TODO: Remove this if unused. IS THIS MEANT TO BE USED FOR RECEIVING ORDERS???

    /**
     * Adds a new Ingredient to this Restaurant's inventory.
     *
     * @param ingredient the Ingredient object to be added
     * @param amount     the amount of this Ingredient that should be added
     */
    private void addToInventory(Ingredient ingredient, int amount) {
        inventory.addToInventory(ingredient, amount);
        /* Uncomment this to test inventory reups
        for (Ingredient i : inventory.getContents().keySet()) {
            System.out.println(i.getName() + ": " + inventory.getContents().get(i));
        }*/
    }

    /**
     * Processes the Events that are received by EventManager, from events.txt.
     * For order events, the ordered MenuItems and Ingredient modifiers are replaced with their corresponding existing MenuItem or Ingredient in the Menu.
     *
     * @param manager The EventManager object containing the events that need processing
     */
    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();
        while (!events.isEmpty()) {
            Event e = events.remove();

            int tableId = e.getTableId();
            Order tableOrder = new OrderImpl(Collections.emptyList());
            Table currentTable = tables[0];
            if (tableId > 0) {
                tableOrder = tables[e.getTableId()].getOrder();
                currentTable = tables[tableId];
            }

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
                case RECEIVEDSHIPMENT:
                    for (Ingredient i : e.getShipment().keySet()) {
                        addToInventory(menu.getMenuIngredient(i), e.getShipment().get(i));
                    }
                    break;
            }
        }
    }
}
