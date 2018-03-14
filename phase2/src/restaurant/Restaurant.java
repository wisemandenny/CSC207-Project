package restaurant;

import events.Event;
import menu.*;

import java.util.*;

public class Restaurant {
    private static final Menu menu = new BurgerMenu();
    private static final Inventory inventory = new InventoryImpl(Restaurant.menu);
    private static final Set<Order> placedOrders = new HashSet<>();
    private static final Set<Order> cookingOrders = new HashSet<>();
    private static final Set<Order> rejectedOrders = new HashSet<>();
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
        Restaurant.tables[o.getTableId()].addOrder(o);
        System.out.println("Order #" + o.getId() + " placed.");
    }

    public static void addCookingOrder(int orderId) {
        Order order = Restaurant.findOrder(Restaurant.placedOrders, orderId);
        Restaurant.placedOrders.remove(order);
        Restaurant.checkInventory(order);
        //Restaurant.cookingOrders.add(order);
        //Todo: reject items here
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

    private static void checkInventory(Order o) {
        List<MenuItem> rejectedItems = new ArrayList<>();
        List<MenuItem> acceptedItems = new ArrayList<>();

        for(MenuItem item: o.getItems()) {
            List<Ingredient> allIngredients = item.getAllIngredients();
            MenuItem itemToAdd = new MenuItemImpl(item);
            MenuItem itemToRemove = new MenuItemImpl(item);

            itemLoop:
            for(int i = 1; i <= item.getQuantity(); i++){
                for(Ingredient ingredient : allIngredients){
                    if(inventory.getContents().get(ingredient) == 0){//not enough inventory
                        itemToRemove.setQuantity(item.getQuantity() - i);
                        rejectedItems.add(itemToRemove);
                        System.out.println(itemToRemove.getQuantity() + " " + itemToRemove.getName() + "(s) cannot be cooked because we are out of " + ingredient.getName() + ",");
                        //TODO: log a request here
                        break itemLoop;
                    }else{ //enough inventory
                        inventory.removeFromInventory(ingredient);
                        if(ingredient.equals(allIngredients.get(allIngredients.size()-1))){//enough inventory for the whole item
                            itemToAdd.setQuantity(i);
                        }
                    }
                }
            }
            acceptedItems.add(itemToAdd);

        }
        if (!rejectedItems.isEmpty()) Restaurant.rejectedOrders.add(new OrderImpl(rejectedItems, o.getId(), o.getTableId()));
        if (!acceptedItems.isEmpty()) Restaurant.cookingOrders.add(new OrderImpl(acceptedItems, o.getId(), o.getTableId()));
    }


    private static Order findOrder(Set<Order> searchSet, int orderId) {
        for (Order order : searchSet) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order #" + orderId + " not found.");
    }

    public static void addToInventory(Map<Ingredient, Integer> shipment) {
        Restaurant.inventory.addToInventory(shipment);
        /* uncomment this to check inventory restock
        for (menu.Ingredient i : Restaurant.inventory.getContents().keySet()) {
            System.out.println(i.getName() + ": " + Restaurant.inventory.getContents().get(i));
        }
        */
    }

    public static Table getTable(int tableId) {
        return Restaurant.tables[tableId];
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
