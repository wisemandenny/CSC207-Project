package restaurant;

import menu.Ingredient;
import menu.MenuItem;

import java.util.List;

public interface Order {
    void add(Order o);

    void remove(Order o);

    /**
     * Returns a list of MenuItems that are apart of this restaurant.Order.
     *
     * @return a list of MenuItems in this restaurant.Order
     */
    List<MenuItem> getItems();

    /**
     * Return the ID of this Order.
     *
     * @return an int representing this Order's ID.
     */
    int getId();

    /**
     * Return this Order's Table ID.
     *
     * @return an int representing this Order's Table ID.
     */
    int getTableId();

    /**
     * Sets this Order's Table ID.
     *
     * @param id the int that will represent this Order's Table ID.
     */
    void setTableId(int id);

    /**
     * Return this Order's seat ID.
     *
     * @return an int representing this Order's seat ID.
     */
    int getSeatId();

    /**
     * Sets this Order's seat ID.
     *
     * @param id the int that will represent this Order's seat ID.
     */
    void setSeatId(int id);

    /**
     * Returns a list of the Ingredients that are in the MenuItems in this restaurant.Order.
     *
     * @return a list of Ingredients in this restaurant.Order
     */
    List<Ingredient> getIngredients();

    /**
     * A boolean that represents if a restaurant.Order has been returned by the server.
     */
    void returned(MenuItem item);
}
