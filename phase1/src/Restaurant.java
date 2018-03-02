import java.util.Queue;

class Restaurant {
    private Table[] tables;
    private Inventory inventory = new InventoryImpl();
    private EventManager eventManager;
    private Menu menu = new BurgerMenu();

    Restaurant(int numOfTables) {
        this.tables = new Table[numOfTables+1];
        for (int i = 1; i <= numOfTables; i++) {
            tables[i] = new Table(i); //add all the tables to the restaurant.
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

    private void getItemsFromMenu(){

    }

    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();
        while (!events.isEmpty()) {
            Event e = events.remove();
            if (e.getType().equals("order")) {
                tables[e.getTableId()].addOrderToTable(e.getOrder()); //get the prices from the menu here. use the empty getItemsFromMenu() method.
            } else if (e.getType().equals("bill")) {
                printBill(e.getTableId());
            } else if (e.getType().equals("cookSeen")) {
                tables[e.getTableId()].getOrder().receivedByCook();
                System.out.println("COOK HAS SEEN:\n" + tables[e.getTableId()].getOrder().toString());
            } else if (e.getType().equals("cookReady")) {
                tables[e.getTableId()].getOrder().readyForPickup();
                System.out.println("READY FOR PICKUP!\n" + tables[e.getTableId()].getOrder());
            } else if (e.getType().equals("serverDelivered")) {
                tables[e.getTableId()].getOrder().delivered();
                addOrderToBill(e.getTableId(), tables[e.getTableId()].getOrder());
                System.out.println("DELIVERED TO TABLE\n" + tables[e.getTableId()].getOrder());
            } else if (e.getType().equals("serverReturned")) {
                System.out.println("We got a live one here! Order of blabla has been sent back to the kitchen!");
            }
        }
    }
}
