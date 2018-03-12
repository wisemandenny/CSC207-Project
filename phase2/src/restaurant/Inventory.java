package restaurant;

import menu.Ingredient;

import java.util.Map;

public interface Inventory {
    /**
     * Adds menu.Ingredient i with quantity amount to the restaurant.Inventory.
     *
     * @param i      the ingredient being added to the inventory
     * @param amount the amount of the menu.Ingredient being added
     */
    void addToInventory(Map<Ingredient, Integer> shipment);

    /**
     * Removes the Ingredients in the specified menu.MenuItem from the restaurant.Inventory.
     *
     * @param item the menu.MenuItem that is composed of certain Ingredients
     * @param t    the restaurant.TableImpl that ordered the menu.MenuItem
     */
    void removeFromInventory(Order o);

    /**
     * Returns the current inventory of the restaurant.
     *
     * @return the restaurant.Inventory of the Restaurant
     */
    Map<Ingredient, Integer> getContents();

    void makeRestockRequest(Ingredient i);

    void printContents();
}
