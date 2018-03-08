import java.util.Map;
import java.util.List;

public interface Inventory {
    /**
     * Adds ingredient i with quantity amount to the inventory
     *
     * @param i  the ingredient being added to the inventory
     * @param amount  the amount of the ingredient being added.
     */
    void addToInventory(Ingredient i, int amount);

    /**
     * Removes the ingredients in the specified MenuItem from the inventory.
     *
     * @param item  the MenuItem that is composed of certain ingredients
     * @param t  the table that ordered the MenuItem
     */
    void removeFromInventory(MenuItem item, Table t);

    /**
     * Returns the current inventory of the restaurant.
     *
     * @return      the inventory of the restaurant.
     */
    Map<Ingredient, Integer> getContents();
}
