package restaurant;

import events.Event;
import menu.BurgerMenu;
import menu.Ingredient;
import menu.Menu;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Restaurant {
    private static final Menu menu = new BurgerMenu();
    private static final Inventory inventory = new InventoryImpl(Restaurant.menu);
    private static final Set<Order> placedOrders = new HashSet<>();
    private static final Set<Order> cookingOrders = new HashSet<>();
    private static final Set<Order> readyOrders = new HashSet<>();
    private static final Set<Order> deliveredOrders = new HashSet<>();
    private static Restaurant instance = null; //singleton
    private static TableImpl[] tables;

    protected Restaurant() {
    }

    /**
     * Constructs a new restaurant.Restaurant object.
     *
     * @param numOfTables the number of Tables this restaurant.Restaurant should have.
     */
    private Restaurant(int numOfTables) {
        Restaurant.tables = new TableImpl[numOfTables + 1];
        for (int i = 1; i <= numOfTables; i++) {
            Restaurant.tables[i] = new TableImpl(i);
        }
        EventManager eventManager = new EventManager(Restaurant.tables);
        handleEvents(eventManager);
    }

    public static Restaurant getInstance(int numOfTables) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables);
        }
        return Restaurant.instance;
    }

    public static Menu getMenu() {
        return Restaurant.menu;
    }

    /**
     * The manager can use the following three functions to check how the restaurant is doing.
     *
     * @return mmhmm i don't think so
     */
    public static Set<Order> getCookingOrders() {
        return Restaurant.cookingOrders;
    }

    public static Set<Order> getReadyOrders() {
        return Restaurant.readyOrders;
    }

    public static Set<Order> getDeliveredOrders() {
        return Restaurant.deliveredOrders;
    }

    public static void addPlacedOrder(Order o) {
        Restaurant.placedOrders.add(o);
        System.out.println("Order #" + o.getId() + " placed.");
    }

    public static void addCookingOrder(int orderId) {
        Order order = Restaurant.findOrder(Restaurant.placedOrders, orderId);
        Restaurant.placedOrders.remove(order);
        Restaurant.cookingOrders.add(order);
        Restaurant.inventory.removeFromInventory(order);
        System.out.println("Order #" + order.getId() + " is now being cooked.");
        //Restaurant.inventory.printContents();


    }

    public static void addReadyOrder(int orderId) {
        Order order = Restaurant.findOrder(Restaurant.cookingOrders, orderId);
        Restaurant.cookingOrders.remove(order);
        Restaurant.readyOrders.add(order);
        System.out.println("Order #" + order.getId() + " is now ready for pickup.");

    }

    public static void addDeliveredOrder(int orderId) {
        Order order = Restaurant.findOrder(Restaurant.readyOrders, orderId);
        Restaurant.readyOrders.remove(order);
        Restaurant.deliveredOrders.add(order);
        Restaurant.tables[order.getTableId()].addOrderToBill(order);


        System.out.println("Order #" + order.getId() + " has been delivered to Table " + order.getTableId() + ".");

    }

    private static Order findOrder(Set<Order> searchSet, int orderId) {
        for (Order order : searchSet) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order #" + orderId + " not found.");
    }

    // TODO: Remove this if unused. IS THIS MEANT TO BE USED FOR RECEIVING ORDERS???

    /**
     * Adds a new menu.Ingredient to this restaurant.Restaurant's inventory.
     *
     * @param ingredient the menu.Ingredient object to be added
     * @param amount     the amount of this menu.Ingredient that should be added
     */
    public static void addToInventory(Map<Ingredient, Integer> shipment) {
        Restaurant.inventory.addToInventory(shipment);

        for (menu.Ingredient i : inventory.getContents().keySet()) {
            System.out.println(i.getName() + ": " + inventory.getContents().get(i));
        }
    }

    /**
     * Processes the events that are received by restaurant.EventManager, from events.txt.
     * For order events, the ordered MenuItems and menu.Ingredient modifiers are replaced with their corresponding existing menu.MenuItem or menu.Ingredient in the menu.Menu.
     *
     * @param manager The restaurant.EventManager object containing the events that need processing
     */
    private void handleEvents(EventManager manager) {
        Queue<Event> events = manager.getEvents();
        while (!events.isEmpty()) {
            Event e = events.remove();
            e.doEvent();/*
            int tableId = e.getTableId();
            Order tableOrder = new OrderImpl(Collections.emptyList());
            TableImpl currentTable = tables[0];
            if (tableId > 0) {
                tableOrder = tables[e.getTableId()].getOrder();
                currentTable = tables[tableId];
            }*/
/*
            switch (e.getType()) {

                case EventType.BILL:
                    printBillForTable(tableId);
                    break;
                case EventType.COOKSEEN:
                    tableOrder.receivedByCook();
                    System.out.println("COOK HAS SEEN:\n" + tableOrder);
                    break;
                case EventType.COOKREADY:
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
                case EventType.SERVERDELIVERED:
                    tableOrder.delivered();
                    addOrderToBill(tableId, tableOrder);
                    System.out.println("DELIVERED TO TABLE " + tableId + "\n" + tableOrder);
                    break;
                case EventType.SERVERRETURNED:
                    // add all the returned items to this table
                    tableOrder.returned();
                    for (MenuItem deductedItem : e.getDeductions().getItems()) {
                        MenuItem fromMenuItem = menu.getMenuItem(deductedItem);
                        for (MenuItem addOnItem : tableOrder.getItems()) {
                            for (Ingredient addOn : addOnItem.getExtraIngredients()) {
                                if (deductedItem.equals(addOnItem)) {
                                    fromMenuItem.addExtraIngredient(addOn);
                                }
                            }
                        }
                        currentTable.addToDeductions(fromMenuItem, deductedItem.getQuantity(), deductedItem.getComment());
                    }

                    System.out.println("TABLE " + tableId +
                            " HAS RETURNED THE FOLLOWING ITEM(s): \n" + currentTable.stringDeductions());
                    break;
                case EventType.RECEIVEDSHIPMENT:
                    for (Ingredient i : e.getShipment().keySet()) {
                        addToInventory(menu.getMenuIngredient(i), e.getShipment().get(i));
                    }
                    break;
            }*/
        }
    }
}
