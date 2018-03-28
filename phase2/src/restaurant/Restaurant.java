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
     * Returns an instance of a Restaurant. This exists because a Restaurant is a Singleton.
     *
     * @param numOfTables the number of tables in this Restaurant.
     * @param taxRate the tax rate of this Restaurant.
     * @return  an instance of this Restaurant.
     */
    public static Restaurant getInstance(int numOfTables, double taxRate) {
        if (Restaurant.instance == null) {
            Restaurant.instance = new Restaurant(numOfTables, taxRate);
        }
        return Restaurant.instance;
    }

    /**
     * Returns an instance of a Restaurant.
     *
     * @return  an instance of this Restaurant.
     */
    public static Restaurant getInstance(){ return Restaurant.instance; }

    /**
     * End the simulation of this Restaurant.
     */
    public static void end(){ Restaurant.getInstance().running = false; }

    /**
     * Returns this Restaurants menu.
     *
     * @return  Menu object of this Restaurant.
     */
    public Menu getMenu() { return menu; }

    /**
     * Returns the tax rate of this Restaurant.
     *
     * @return  a double that represents this Restaurant's tax rate.
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Returns the auto gratuity rate of this Restaurant.
     *
     * @return  a double that represents this Restaurant's auto gratuity rate.
     */
    public double getAutoGratRate() { return autoGratRate; }

    /**
     * Returns the number of tables in this Restaurant.
     *
     * @return  an int representing the number of Tables in this Restaurant.
     */
    public int getNumOfTables() { return tables.length; }

    /**
     * Adds the event found in eventString to the event manager.
     *
     * @param eventString a String with event information.
     */
    public void newEvent(String eventString) { eventManager.addEventFromString(eventString); }

    /**
     * Returns a List of Orders that have been placed.
     *
     * @return  a List of Orders that have been placed.
     */
    public List<Order> getPlacedOrders() { return placedOrders; }

    /**
     * Returns a List of Orders that have been received by the cook.
     *
     * @return  a List of Orders that have been received.
     */
    public List<Order> getReceivedOrders() { return receivedOrders; }

    /**
     * Returns a List of Orders that are being made.
     *
     * @return  a List of Orders that are being cooked.
     */
    public List<Order> getCookingOrders() {
        return cookingOrders;
    }

    /**
     * Returns a List of Orders that are ready to be served.
     *
     * @return  a List of Orders that are ready to be served.
     */
    public List<Order> getReadyOrders() {
        return readyOrders;
    }

    /**
     * Returns a List of Orders that have been delivered.
     *
     * @return  a List of Orders that have been delivered.
     */
    public List<Order> getDeliveredOrders() {
        return deliveredOrders;
    }

    /**
     * Adds Order o to the placed orders List and adds the Order to the seat.
     *
     * @param o the Order that is to be added.
     */
    public void addPlacedOrder(Order o) {
        placedOrders.add(o);
        tables[o.getTableId()].getSeat(o.getSeatId()).addOrder(o); //TODO: check
    }

    /**
     * Adds Order with ID orderId to the received orders list and removes it from the placed
     * orders list.
     *
     * @param orderId the Order ID.
     */
    public void addReceivedOrder(int orderId){
        try{
            Order order = findOrder(placedOrders, orderId);
            placedOrders.remove(order);
            receivedOrders.add(order);
        } catch (IllegalArgumentException ex){
            //shhouldn't do anything at all, for now lett's just print the error
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Adds Order with ID orderId to the cooking orders list and removes it from the received
     * orders list.
     *
     * @param orderId the Order ID.
     */
    public void addCookingOrder(int orderId) {
        try{
            Order order = findOrder(receivedOrders, orderId);
            receivedOrders.remove(order);
            if (checkInventory(order)) {
                cookingOrders.add(order);
            }
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Adds Order with ID orderId to the ready orders list and removes it from the cooking
     * orders list.
     *
     * @param orderId the Order ID.
     */
    public void addReadyOrder(int orderId) {
        try{
            Order order = findOrder(cookingOrders, orderId);
            cookingOrders.remove(order);
            readyOrders.add(order);
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Adds Order with ID orderId to the ready orders list and remove it from the delivered
     * orders list.
     *
     * @param orderId the Order ID.
     */
    public void addDeliveredOrder(int orderId) {
        try{
            Order order = findOrder(readyOrders, orderId);
            readyOrders.remove(order);
            deliveredOrders.add(order);
            tables[order.getTableId()].addOrderToBill(order);
            tables[order.getTableId()].getSeat(order.getSeatId()).addOrderToBill(order);

        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Takes an order (o), and checks every single item in that order to make sure that there's inventory to cook it.
     * e.g. if you have 10 cokes in the inventory and you request 11, 10 should be cooked and one should be rejected.
     *
     * @param o the Order that is to be analyzed.
     */
    private Boolean checkInventory(Order o) {
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
                        return false;
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
            ord = OrderFactory.makeOrder(acceptedItems, o.getId(), o.getTableId(), o.getSeatId());
            inventory.removeFromInventory(ord);
        }
        return true;
    }

    /**
     * Returns an Order from the List of Orders in searchSet that has the ID orderId
     *
     * @param searchSet the List of Orders to be searched.
     * @param orderId the desired Order ID.
     * @return  the Order with Order ID orderId.
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
     * Add the shipment to the inventory.
     *
     * @param shipment the Map of Ingredients being shipped to the Restaurant.
     */
    public void addToInventory(Map<Ingredient, Integer> shipment) {
        inventory.addToInventory(shipment);
        /* uncomment this to check inventory restock
        for (menu.Ingredient i : Restaurant.inventory.getContents().keySet()) {

        }
        */
    }

    /**
     * Returns an array of Tables that belong to this Restaurant.
     *
     * @return  an Array of Tables.
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
     * Runs the simulation.
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
