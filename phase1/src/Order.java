import java.util.List;

public interface Order {
    /**
     * Returns a list of MenuItems that are apart of this Order.
     *
     * @return      a list of MenuItems in this Order
     */
    List<MenuItem> getItems();

    /**
     * Returns a list of the Ingredients that are in the MenuItems in this Order.
     *
     * @return      a list of Ingredients in this Order
     */
    List<Ingredient> getIngredients();

    /**
     * A boolean that represents if the cook has seen the Order.
     */
    void receivedByCook();

    /**
     * A boolean that represents if the Order is ready to be picked up by the server.
     */
    void readyForPickup();

    /**
     * A boolean that represents if the Order has been delivered to the Table that made the Order.
     */
    void delivered();

    /**
     * A boolean that represents if an Order has been returned by the server.
     */
    void returned();
}
