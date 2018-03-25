package restaurant;

import events.Event;
import menu.*;

import java.util.*;
import java.util.logging.Level;

public class Restaurant extends Observable implements Runnable {
    private static Restaurant instance = null; //singleton
    private final Menu menu = new BurgerMenu();
    private final Inventory inventory = new InventoryImpl(menu);
    private final List<Order> placedOrders = new ArrayList<>();
    private final List<Order> receivedOrders = new ArrayList<>();
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
            tables[i] = new TableImpl(i, false);
        }
    }
    public static Restaurant getInstance(int numOfTables, double taxRate) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables, taxRate);
        }
        return Restaurant.instance;
    }

    public static Restaurant getInstance(){ return Restaurant.instance; }
    public static void end(){ Restaurant.getInstance().running = false; }
    public Menu getMenu() { return menu; }
    public int getNumOfTables() { return tables.length; }
    public void newEvent(String eventString) { eventManager.addEventFromString(eventString); }

    /**
     * The manager can use the following three functions to check how the restaurant is doing.
     *
     * @return mmhmm i don't think so
     */
    public List<Order> getPlacedOrders() { return placedOrders; }
    public List<Order> getReceivedOrders() { return receivedOrders; }
    public List<Order> getCookingOrders() {
        return cookingOrders;
    }
    public List<Order> getReadyOrders() {
        return readyOrders;
    }
    public List<Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    public void addPlacedOrder(Order o) {
        placedOrders.add(o);
        tables[o.getTableId()].getSeat(o.getSeatId()).addOrder(o); //TODO: check
    }

    public void addReceivedOrder(int orderId){
        Order order = findOrder(placedOrders, orderId);
        placedOrders.remove(order);
        receivedOrders.add(order);
    }

    public void addCookingOrder(int orderId) {
        Order order = findOrder(receivedOrders, orderId);
        receivedOrders.remove(order);
        checkInventory(order);
        //Todo: reject items here
        inventory.removeFromInventory(order);
    }

    public void addReadyOrder(int orderId) {
        Order order = findOrder(cookingOrders, orderId);
        cookingOrders.remove(order);
        readyOrders.add(order);
    }

    public void addDeliveredOrder(int orderId) {
        Order order = findOrder(readyOrders, orderId);
        readyOrders.remove(order);
        deliveredOrders.add(order);
        tables[order.getTableId()].addOrderToBill(order);
        tables[order.getTableId()].getSeat(order.getSeatId()).addOrderToBill(order);
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
                    if (inventory.getContents().get(ingredient) == 0) {//not enough inventory
                        itemToRemove.setQuantity(item.getQuantity() - i);
                        rejectedItems.add(itemToRemove);
                        RestaurantLogger.log(Level.WARNING, itemToRemove.getQuantity() + " " + itemToRemove.getName() + "(s) cannot be made because we are out of " + ingredient.getName() + ".");
                        break itemLoop;
                    } else { //enough inventory
                        inventory.removeFromInventory(ingredient);
                        if (ingredient.equals(allIngredients.get(allIngredients.size() - 1))) {//enough inventory for the whole item
                            itemToAdd.setQuantity(i);
                        }
                    }
                }
            }
            acceptedItems.add(itemToAdd);

        }
        if (!rejectedItems.isEmpty())
            rejectedOrders.add(new OrderImpl(rejectedItems, o.getId(), o.getTableId()));
        if (!acceptedItems.isEmpty()) {
            Order ord = new OrderImpl(acceptedItems, o.getId(), o.getTableId());
            ord.setSeatId(o.getSeatId()); //TODO: check
            cookingOrders.add(ord);
        }
    }

    private Order findOrder(List<Order> searchSet, int orderId) {
        for (Order order : searchSet) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order #" + orderId + " not found in " + searchSet.toString());
    }

    public void addToInventory(Map<Ingredient, Integer> shipment) {
        inventory.addToInventory(shipment);
        /* uncomment this to check inventory restock
        for (menu.Ingredient i : Restaurant.inventory.getContents().keySet()) {

        }
        */
    }

    double getTaxRate() {
        return taxRate;
    }

    Table[] getTables(){ return tables; }

    public Table getTable(int tableId) {
        return tables[tableId];
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
        while (running) {
            if (!eventQueue.isEmpty()) {
                eventQueue.remove().doEvent();
                setChanged();
                notifyObservers();
            } else {
                try {
                    eventQueue.addAll(eventManager.getEvents());
                    Thread.sleep(100); //wait for new events to be added by the frontend.
                } catch (Exception ex) {
                    RestaurantLogger.log(Level.SEVERE, "Exception: " + ex.toString());
                }
            }
        }

    }
}
