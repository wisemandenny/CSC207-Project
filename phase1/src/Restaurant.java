import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class Restaurant {
    private Table[] tables;
    private Inventory inventory = new InventoryImpl();
    private EventManager eventManager;
    private Menu menu = new BurgerMenu();

    Restaurant(int numOfTables) {
        this.tables = new Table[numOfTables+1];
        for (int i = 1; i <= numOfTables; i++) {
            tables[i] = new Table(i);
        }
        eventManager = new EventManager();
        handleEvents(eventManager);
    }

    private void printBill(int tableId) {
        tables[tableId].printBill();
    }

    private void addOrderToBill(int tableId, Order order) {
        Table table = tables[tableId];
        table.addToBil(order);
    }

    private void addToInventory(Ingredient ingredient, int amount){
        inventory.addToInventory(ingredient, amount);
    }

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();

        while (!events.isEmpty()) {
            Event e = events.remove();
            Order tableOrder = tables[e.getTableId()].getOrder();
            int tableId = e.getTableId();

            if (e.getType().equals("order")) {
                Order order = e.getOrder();
                List<MenuItem> newOrder = new ArrayList<>();
                for(MenuItem item : order.getItems()){
                    newOrder.add(new MenuItemImpl(menu.getMenuItem(item), item.getQuantity()));
                }
                tables[tableId].addOrderToTable(new OrderImpl(newOrder)); //TODO: fix this because it references the implementation (use dependency injection or a factory or something)

            } else if (e.getType().equals("addon")) {
                String[] instructions = e.getAddOn().split("\\s");
                String instructionType = instructions[1];
                int itemIndex = Integer.parseInt(instructions[0]) - 1;
                FoodMod modifier = new FoodModImpl(new IngredientImpl(instructions[2]));
                if (instructionType.equals("add")) {
                    modifier.addTo(tableOrder.getItems().get(itemIndex));
                } else {
                    modifier.removeFrom(tableOrder.getItems().get(itemIndex));
                }

            } else if (e.getType().equals("bill")) {
                printBill(tableId);
            } else if (e.getType().equals("cookSeen")) {
                tableOrder.receivedByCook();
                System.out.println("COOK HAS SEEN:\n" + tableOrder);
            } else if (e.getType().equals("cookReady")) {
                tableOrder.readyForPickup();
                System.out.println("READY FOR PICKUP!\n" + tableOrder);
            } else if (e.getType().equals("serverDelivered")) {
                tableOrder.delivered();
                addOrderToBill(tableId, tableOrder);
                System.out.println("DELIVERED TO TABLE " + tableId + "\n" + tableOrder);
            } else if (e.getType().equals("serverReturned")) { //TODO: not implemented yet
                System.out.println("We got a live one here! Order of blabla has been sent back to the kitchen!");
            }
        }
    }
}
