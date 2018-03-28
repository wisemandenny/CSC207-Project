package menu;

import java.util.List;

public interface MenuItem {
    /**
     * Returns the name of this menu.MenuItem.
     *
     * @return  the name of this menu.MenuItem
     */
    String getName();

    /**
     * Returns the price of this menu.MenuItem.
     *
     * @return  the price of this menu.MenuItem
     */
    double getPrice();

    /**
     * Sets the price of this menu.MenuItem to the specified parameter price.
     *
     * @param price the new price of this menu.MenuItem
     */
    void setPrice(double price);

    /**
     * Returns the cost of this menu.MenuItem multiplied by the quantity.
     *
     * @return  total cost of this menu.MenuItem
     */
    double getTotal();

    /**
     * Returns the cost of all the extra ingredients that were added to this menu.MenuItem.
     *
     * @return  a double that represents the total cost of all added Ingredients
     */
    double getExtraIngredientPrice();

    /**
     * Returns the number of this menu.MenuItem that have been ordered.
     *
     * @return  an int that represents the amount of this menu.MenuItem that have been ordered
     */
    int getQuantity();

    /**
     * Sets the quantity of this MenuItem.
     *
     * @param quantity the amount of this MenuItem.
     */
    void setQuantity(int quantity);

    /**
     * Returns the comment associated with this menu.MenuItem, which describes the reason why it was returned.
     *
     * @return  a String that describes why this menu.MenuItem was returned to the kitchen
     */
    String getComment();

    /**
     * Sets the comment for this menu.MenuItem. The comment describes why this item was returned to the kitchen.
     *
     * @param comment the comment that is to be associated with this menu.MenuItem
     */
    void setComment(String comment);

    /**
     * Returns a list of Ingredients that this menu.MenuItem is comprised of.
     *
     * @return  a list of Ingredients
     */
    List<Ingredient> getIngredients();

    /**
     * Adds an allowed menu.Ingredient to this menu.MenuItem.
     *
     * @param ingredient the extra menu.Ingredient being added to this menu.MenuItem
     */
    void addExtraIngredient(Ingredient ingredient);

    /**
     * Returns a list of the extra Ingredients added to this menu.MenuItem.
     *
     * @return  a list of added on Ingredients
     */
    List<Ingredient> getExtraIngredients();

    /**
     * Returns  a list of the Ingredients that were removed from this menu.MenuItem.
     *
     * @return  a list of removed Ingredients
     */
    List<Ingredient> getRemovedIngredients();

    /**
     * Returns the cost of the Ingredients that have been removed from this menu.MenuItem.
     *
     * @return  a double that represents the sum of the costs of the removed Ingredients
     */
    double getRemovedIngredientsPrice();

    /**
     * Removes menu.Ingredient ingredient from the list of Ingredients.
     *
     * @param ingredient The ingredient that is to be removed from this menu.MenuItem's list of Ingredients
     */
    void removeIngredient(Ingredient ingredient);

    /**
     * Returns whether this menu.MenuItem has the same name as menu.MenuItem item.
     *
     * @param o the menu.MenuItem being compared to this menu.MenuItem
     * @return  a boolean that represents if this menu.MenuItem has the same name as menu.MenuItem item
     */
    boolean equals(Object o);

    /**
     * A more detailed comparison that checks if MenuItem item has the same mods as this MenuItem.
     *
     * @param item the MenuItem being compared.
     * @return  a boolean representing whether they are equivalent or not.
     */
    boolean equalsWithExtras(MenuItem item);

    /**
     * Returns a List of all Ingredients.
     *
     * @return  a List of all the Ingredients.
     */
    List<Ingredient> getAllIngredients();
}
