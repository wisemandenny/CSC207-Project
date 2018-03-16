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

    int getId();

    int getTableId();

    void setTableId(int id);

    /**
     * Returns a list of the Ingredients that are in the MenuItems in this restaurant.Order.
     *
     * @return a list of Ingredients in this restaurant.Order
     */
    List<Ingredient> getIngredients();

    /**
     * A boolean that represents if an restaurant.Order has been returned by the server.
     */
    void returned(MenuItem item);
}
