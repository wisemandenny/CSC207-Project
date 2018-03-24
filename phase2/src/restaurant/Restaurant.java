package restaurant;

import events.Event;
import menu.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Restaurant implements Runnable {
    private static Restaurant instance = null; //singleton
    private final Menu menu = new BurgerMenu();
    private final Inventory inventory = new InventoryImpl(menu);
    private final List<Order> placedOrders = new ArrayList<>();
    private final List<Order> cookingOrders = new ArrayList<>();
    private final List<Order> rejectedOrders = new ArrayList<>();
    private final List<Order> readyOrders = new ArrayList<>();
    private final List<Order> deliveredOrders = new ArrayList<>();
    private double taxRate;
    private boolean running;
    private TableImpl[] tables;
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
        running = true;
        this.taxRate = taxRate;
        tables = new TableImpl[numOfTables + 1];
        for (int i = 1; i <= numOfTables; i++) {
            tables[i] = new TableImpl(i);
        }
    }
    public static Restaurant getInstance(int numOfTables, double taxRate) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables, taxRate);
        }
        return Restaurant.instance;
    }

    public static Restaurant getInstance(){
        return Restaurant.instance;
    }



    public static void end() {
        Restaurant.getInstance().running = false;
    }

    public Menu getMenu() {
        return Restaurant.getInstance().menu;
    }

    public int getNumOfTables() {
        return Restaurant.getInstance().tables.length;
    }

    public void newEvent(String eventString) {
        Restaurant.getInstance().eventManager.addEventFromString(eventString);
    }

    /**
     * The manager can use the following three functions to check how the restaurant is doing.
     *
     * @return mmhmm i don't think so
     */
    public List<Order> getPlacedOrders() { return Restaurant.getInstance().placedOrders; }

    public List<Order> getCookingOrders() {
        return Restaurant.getInstance().cookingOrders;
    }

    public List<Order> getReadyOrders() {
        return Restaurant.getInstance().readyOrders;
    }

    public List<Order> getDeliveredOrders() {
        return Restaurant.getInstance().deliveredOrders;
    }

    public void addPlacedOrder(Order o) {
        Restaurant.getInstance().placedOrders.add(o);
        Restaurant.getInstance().tables[o.getTableId()].addOrder(o);
    }

    public void addCookingOrder(int orderId) {
        Order order = Restaurant.getInstance().findOrder(Restaurant.getInstance().placedOrders, orderId);
        Restaurant.getInstance().placedOrders.remove(order);
        Restaurant.getInstance().checkInventory(order);
        //Restaurant.cookingOrders.add(order);
        //Todo: reject items here
        Restaurant.getInstance().inventory.removeFromInventory(order);

        System.out.println("Order #" + order.getId() + " is now being cooked.");
        //Restaurant.inventory.printContents();


    }

    public void addReadyOrder(int orderId) {
        Order order = Restaurant.getInstance().findOrder(Restaurant.getInstance().cookingOrders, orderId);
        Restaurant.getInstance().cookingOrders.remove(order);
        Restaurant.getInstance().readyOrders.add(order);
        System.out.println("Order #" + order.getId() + " is now ready for pickup.");

    }

    public void addDeliveredOrder(int orderId) {
        Order order = Restaurant.getInstance().findOrder(Restaurant.getInstance().readyOrders, orderId);
        Restaurant.getInstance().readyOrders.remove(order);
        Restaurant.getInstance().deliveredOrders.add(order);
        Restaurant.getInstance().tables[order.getTableId()].addOrderToBill(order);

        System.out.println("Order #" + order.getId() + " has been delivered to Table " + order.getTableId() + ".");

    }

    /**
     * Take an order (o), and check every single item in that order to make sure that there's inventory to cook it
     * e.g. if you have 10 cokes in the inventory and you request 11, 10 should be cooked and one should be rejected
     *
     * @param o
     */
    private void checkInventory(Order o) {
        List<MenuItem> rejectedItems = new ArrayList<>();
        List<MenuItem> acceptedItems = new ArrayList<>();

        for (MenuItem item : o.getItems()) {
            List<Ingredient> allIngredients = item.getAllIngredients();
            MenuItem itemToAdd = MenuItemFactory.makeMenuItem(item);
            MenuItem itemToRemove = MenuItemFactory.makeMenuItem(item);

            itemLoop:
            for (int i = 1; i <= item.getQuantity(); i++) {
                for (Ingredient ingredient : allIngredients) {
                    if (Restaurant.getInstance().inventory.getContents().get(ingredient) == 0) {//not enough inventory
                        itemToRemove.setQuantity(item.getQuantity() - i);
                        rejectedItems.add(itemToRemove);
                        System.out.println(itemToRemove.getQuantity() + " " + itemToRemove.getName() + "(s) cannot be cooked because we are out of " + ingredient.getName() + ",");
                        //TODO: log a request here
                        break itemLoop;
                    } else { //enough inventory
                        Restaurant.getInstance().inventory.removeFromInventory(ingredient);
                        if (ingredient.equals(allIngredients.get(allIngredients.size() - 1))) {//enough inventory for the whole item
                            itemToAdd.setQuantity(i);
                        }
                    }
                }
            }
            acceptedItems.add(itemToAdd);

        }
        if (!rejectedItems.isEmpty())
            Restaurant.getInstance().rejectedOrders.add(new OrderImpl(rejectedItems, o.getId(), o.getTableId()));
        if (!acceptedItems.isEmpty())
            Restaurant.getInstance().cookingOrders.add(new OrderImpl(acceptedItems, o.getId(), o.getTableId()));
    }

    private Order findOrder(List<Order> searchSet, int orderId) {
        for (Order order : searchSet) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order #" + orderId + " not found.");
    }

    public void addToInventory(Map<Ingredient, Integer> shipment) {
        Restaurant.getInstance().inventory.addToInventory(shipment);
        /* uncomment this to check inventory restock
        for (menu.Ingredient i : Restaurant.inventory.getContents().keySet()) {
            System.out.println(i.getName() + ": " + Restaurant.inventory.getContents().get(i));
        }
        */
    }

    double getTaxRate() {
        return Restaurant.getInstance().taxRate;
    }

    Table[] getTables(){ return tables; }

    public Table getTable(int tableId) {
        return Restaurant.getInstance().tables[tableId];
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "backend");
            t.start();
        }
    }

    @Override
    public void run() {
        eventManager = new EventManager();
        Queue<Event> eventQueue = eventManager.getEvents();
        while (Restaurant.getInstance().running) {
            if (!eventQueue.isEmpty()) {
                eventQueue.remove().doEvent();
            } else {
                try {
                    eventQueue.addAll(eventManager.getEvents());
                    Thread.sleep(1000); //sleep for 1 second
                } catch (Exception ex) {
                    //TODO: log this exception (InterruptedException)
                }
            }
        }

    }
}
