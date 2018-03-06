import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

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

            switch (e.getType()) {
                case "order":
                    Order order = e.getOrder();
                    List<MenuItem> newOrder = new ArrayList<>();
                    for (MenuItem item : order.getItems()) {
                        newOrder.add(MenuItemImpl.fromRestaurantMenu(menu.getMenuItem(item), item.getQuantity()));
                    }
                    tables[tableId].addOrderToTable(new OrderImpl(newOrder));
                    break;
                case "bill":
                    printBillForTable(tableId);
                    break;
                case "cookSeen":
                    tableOrder.receivedByCook();
                    System.out.println("COOK HAS SEEN:\n" + tableOrder);
                    break;
                case "cookReady":
                    tableOrder.readyForPickup();
                    System.out.println("READY FOR PICKUP!\n" + tableOrder);
                    for (MenuItem item: tableOrder.getItems()){
                        for (Ingredient i: item.getIngredients()) {
                            if (inventory.getContents().get(i) > 0) {
                                if (inventory.getContents().get(i) < i.getThreshold()){
                                    try (BufferedWriter br = new BufferedWriter(new FileWriter("requests.txt"))){
                                        br.write("We need more" + i.getName());
                                        br.newLine();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                this.inventory.removeFromInventory(i, 1);
                            } else{
                                System.out.println("NOT ENOUGH INGREDIENTS");
                            }
                        }
                    }
                    break;
                case "serverDelivered":
                    tableOrder.delivered();
                    addOrderToBill(tableId, tableOrder);
                    System.out.println("DELIVERED TO TABLE " + tableId + "\n" + tableOrder);
                    break;
                case "serverReturned":  //TODO: not implemented yet
                    System.out.println("We got a live one here! Order of blabla has been sent back to the kitchen!");
                    break;
                //TODO: add "receivedShipment" for when
            }
        }
    }
}
