package restaurant;

import events.Event;
import menu.*;

import java.util.*;

public class Restaurant implements Runnable{
    private static final Menu menu = new BurgerMenu();
    private static final Inventory inventory = new InventoryImpl(Restaurant.menu);
    private static final Set<Order> placedOrders = new HashSet<>();
    private static final Set<Order> cookingOrders = new HashSet<>();
    private static final Set<Order> rejectedOrders = new HashSet<>();
    private static final Set<Order> readyOrders = new HashSet<>();
    private static final Set<Order> deliveredOrders = new HashSet<>();
    private static double taxRate;
    private static boolean running;
    private static Restaurant instance = null; //singleton
    private static TableImpl[] tables;
    private Thread t;
    private EventManager eventManager;


    protected Restaurant() {
    }

    /**
     * Constructs a new restaurant.Restaurant object.
     *
     * @param numOfTables the number of Tables this restaurant.Restaurant should have.
     */
    private Restaurant(int numOfTables, double taxRate) {
        Restaurant.running = true;
        Restaurant.taxRate = taxRate;
        Restaurant.tables = new TableImpl[numOfTables + 1];
        for (int i = 1; i <= numOfTables; i++) {
            Restaurant.tables[i] = new TableImpl(i);
        }
    }

    public static Restaurant getInstance(int numOfTables, double taxRate) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables, taxRate);
        }
        return Restaurant.instance;
    }

    public void start(){
        if(t == null){
            t = new Thread (this, "backend");
            t.start();
        }
    }

    public void run(){
        eventManager = new EventManager(Restaurant.tables);
        System.out.println("tst");
        Queue<Event> eventQueue = eventManager.getEvents();
        while (running) {
            if (!eventQueue.isEmpty()) {
                Event e = eventQueue.remove();
                e.doEvent();
            } else {
                try{
                    eventQueue.addAll(eventManager.getEvents());
                    Thread.sleep(1000); //sleep for 1 second
                } catch (Exception ex) {
                    //TODO: log this exception (InterruptedException)
                }
            }
        }

    }

    public static void end(){
        running = false;
    }

    public static Menu getMenu() {
        return Restaurant.menu;
    }

    public static int getNumOfTables(){ return tables.length; }

    public static void newEvent(String eventString){
        Restaurant.getInstance(getNumOfTables(), taxRate).eventManager.addEventFromString(eventString, tables);
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

        for (MenuItem item : o.getItems()) {
            List<Ingredient> allIngredients = item.getAllIngredients();
            MenuItem itemToAdd = MenuItemFactory.makeMenuItem(item);
            MenuItem itemToRemove = MenuItemFactory.makeMenuItem(item);

            itemLoop:
            for (int i = 1; i <= item.getQuantity(); i++) {
                for (Ingredient ingredient : allIngredients) {
                    if (Restaurant.inventory.getContents().get(ingredient) == 0) {//not enough inventory
                        itemToRemove.setQuantity(item.getQuantity() - i);
                        rejectedItems.add(itemToRemove);
                        System.out.println(itemToRemove.getQuantity() + " " + itemToRemove.getName() + "(s) cannot be cooked because we are out of " + ingredient.getName() + ",");
                        //TODO: log a request here
                        break itemLoop;
                    } else { //enough inventory
                        Restaurant.inventory.removeFromInventory(ingredient);
                        if (ingredient.equals(allIngredients.get(allIngredients.size() - 1))) {//enough inventory for the whole item
                            itemToAdd.setQuantity(i);
                        }
                    }
                }
            }
            acceptedItems.add(itemToAdd);

        }
        if (!rejectedItems.isEmpty())
            Restaurant.rejectedOrders.add(new OrderImpl(rejectedItems, o.getId(), o.getTableId()));
        if (!acceptedItems.isEmpty())
            Restaurant.cookingOrders.add(new OrderImpl(acceptedItems, o.getId(), o.getTableId()));
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

    static double getTaxRate() {
        return Restaurant.taxRate;
    }

    public static Table getTable(int tableId) {
        return Restaurant.tables[tableId];
    }
}
