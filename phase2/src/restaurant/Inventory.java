package restaurant;

import menu.Ingredient;

import java.util.Map;

public interface Inventory {
    /**
     * Adds a shipment with a given quantity of ingredients to Inventory.
     *
     * @param shipment a hash map data structure with Ingredients as the keys
     *                 and an associated Integer value as the amount of Ingredients
     *                 in the shipment
     */
    void addToInventory(Map<Ingredient, Integer> shipment);

    /**
     * Removes the Ingredients necessary to fulfill an order o from Inventory
     *
     * @param o An order of menu items.
     */
    void removeFromInventory(Order o);

    /**
     * Removes Ingredient i from Inventory.
     * @param i An Ingredient
     */
    void removeFromInventory(Ingredient i);

    /**
     * Returns the current inventory of the restaurant.
     *
     * @return the restaurant.Inventory of the Restaurant
     */
    Map<Ingredient, Integer> getContents();

    /**
     * Writes restock requests for any low ingredients to a text file.
     *
     */
    void makeRestockRequest();

    boolean enoughIngredients(Ingredient i, int amount);
}
