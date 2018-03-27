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
    private final double autoGratRate = 0.15;
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

    /**
     *
     * @param numOfTables
     * @param taxRate
     * @return
     */
    public static Restaurant getInstance(int numOfTables, double taxRate) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables, taxRate);
        }
        return Restaurant.instance;
    }

    /**
     *
     * @return
     */
    public static Restaurant getInstance(){ return Restaurant.instance; }

    /**
     *
     */
    public static void end(){ Restaurant.getInstance().running = false; }

    /**
     *
     * @return
     */
    public Menu getMenu() { return menu; }

    /**
     *
     * @return
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     *
     * @return
     */
    public double getAutoGratRate() { return autoGratRate; }

    /**
     *
     * @return
     */
    public int getNumOfTables() { return tables.length; }

    /**
     *
     * @param eventString
     */
    public void newEvent(String eventString) { eventManager.addEventFromString(eventString); }
    /**
     * The manager can use the following three functions to check how the restaurant is doing.
     *
     * @return mmhmm i don't think so
     */
    public List<Order> getPlacedOrders() { return placedOrders; }

    /**
     *
     * @return
     */
    public List<Order> getReceivedOrders() { return receivedOrders; }

    /**
     *
     * @return
     */
    public List<Order> getCookingOrders() {
        return cookingOrders;
    }

    /**
     *
     * @return
     */
    public List<Order> getReadyOrders() {
        return readyOrders;
    }

    /**
     *
     * @return
     */
    public List<Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    /**
     *
     * @param o
     */
    public void addPlacedOrder(Order o) {
        placedOrders.add(o);
        tables[o.getTableId()].getSeat(o.getSeatId()).addOrder(o); //TODO: check
    }

    /**
     *
     * @param orderId
     */
    public void addReceivedOrder(int orderId){
        Order order = findOrder(placedOrders, orderId);
        placedOrders.remove(order);
        receivedOrders.add(order);
    }

    /**
     *
     * @param orderId
     */
    public void addCookingOrder(int orderId) {
        Order order = findOrder(receivedOrders, orderId);
        receivedOrders.remove(order);
        cookingOrders.add(checkInventory(order));
    }

    /**
     *
     * @param orderId
     */
    public void addReadyOrder(int orderId) {
        Order order = findOrder(cookingOrders, orderId);
        cookingOrders.remove(order);
        readyOrders.add(order);
    }

    /**
     *
     * @param orderId
     */
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
    private Order checkInventory(Order o) {
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
            rejectedOrders.add(OrderFactory.makeOrder(rejectedItems, o.getId(), o.getTableId(), o.getSeatId()));
        Order ord = OrderFactory.makeOrder(Collections.<MenuItem>emptyList(), o.getId(), o.getTableId(), o.getSeatId());
        if (!acceptedItems.isEmpty()) {
            ord = OrderFactory.makeOrder(rejectedItems, o.getId(), o.getTableId(), o.getSeatId());
            inventory.removeFromInventory(ord);
        }
        return ord;
    }

    /**
     *
     * @param searchSet
     * @param orderId
     * @return
     */
    private Order findOrder(List<Order> searchSet, int orderId) {
        for (Order order : searchSet) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("Order #" + orderId + " not found in " + searchSet.toString());
    }

    /**
     *
     * @param shipment
     */
    public void addToInventory(Map<Ingredient, Integer> shipment) {
        inventory.addToInventory(shipment);
        /* uncomment this to check inventory restock
        for (menu.Ingredient i : Restaurant.inventory.getContents().keySet()) {

        }
        */
    }

    /**
     *
     * @return
     */
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

    /**
     *
     */
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
