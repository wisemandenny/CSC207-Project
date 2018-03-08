import java.util.Map;

public interface Inventory {
    /**
     * Adds Ingredient i with quantity amount to the Inventory.
     *
     * @param i  the ingredient being added to the inventory
     * @param amount  the amount of the Ingredient being added
     */
    void addToInventory(Ingredient i, int amount);

    /**
     * Removes the Ingredients in the specified MenuItem from the Inventory.
     *
     * @param item  the MenuItem that is composed of certain Ingredients
     * @param t  the Table that ordered the MenuItem
     */
    void removeFromInventory(MenuItem item, Table t);

    /**
     * Returns the current inventory of the restaurant.
     *
     * @return      the Inventory of the Restaurant
     */
    Map<Ingredient, Integer> getContents();
}
