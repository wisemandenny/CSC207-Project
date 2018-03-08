import java.util.List;

public interface MenuItem {
    // MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity);

    /**
     * Returns the name of this MenuItem
     *
     * @return      the name of this MenuItem
     */
    String getName();

    /**
     * Returns the price of this MenuItem
     *
     * @return      the price of this MenuItem
     */
    double getPrice();

    /**
     * Sets the price of this MenuItem to the specified parameter price.
     *
     * @param price  the new price of this MenuItem
     */
    void setPrice(double price);

    /**
     * Returns the cost of this MenuItem multiplied by the quantity.
     *
     * @return      total cost of this MenuItem.
     */
    double getTotal();

    /**
     * Returns the cost of all the extra ingredients that were added to this MenuItem.
     *
     * @return      a double that represents the total cost of all added ingredients.
     */
    double getExtraIngredientPrice();

    /**
     * Returns the number of this MenuItem that have been ordered
     *
     * @return      an int that represents the amount of this MenuItem that have been ordered
     */
    int getQuantity();

    /**
     * Returns the comment associated with this MenuItem, which describes the reason why it was returned.
     *
     * @return      a String that describes why this MenuItem was returned to the kitchen.
     */
    String getComment();

    /**
     * Sets the comment for this MenuItem. The comment describes why this item was returned to the kitchen.
     *
     * @param comment  the comment that is to be associated with this MenuItem.
     */
    void setComment(String comment);

    /**
     * Returns a list of Ingredients that this MenuItem is comprised of.
     *
     * @return      a list of Ingredients
     */
    List<Ingredient> getIngredients();

    /**
     * Adds an allowed Ingredient to this MenuItem.
     *
     * @param ingredient  the extra ingredient being added to this MenuItem.
     */
    void addExtraIngredient(Ingredient ingredient);

    /**
     * Returns a list of the extra ingredients added to this MenuItem.
     *
     * @return      a list of added on Ingredients.
     */
    List<Ingredient> getExtraIngredients();

    /**
     * Returns a list of the Ingredients that were removed from this MenuItem.
     *
     * @return      a list of removed Ingredients.
     */
    List<Ingredient> getRemovedIngredients();

    /**
     * Returns the cost of the Ingredients that have been removed from this MenuItem.
     *
     * @return      a double that represents the sum of the costs of the removed Ingredients.
     */
    double getRemovedIngredientsPrice();

    /**
     * Removes Ingredient ingredient from the list of Ingredients.
     *
     * @param ingredient  The ingredient that is to be removed from this MenuItem's list of Ingredients.
     */
    void removeIngredient(Ingredient ingredient);

    /**
     * Returns whether this MenuItem has the same name as MenuItem item.
     *
     * @param item  the MenuItem being compared to this MenuItem.
     * @return      a boolean that represents if this MenuItem has the same name as MenuItem item.
     */
    boolean equals(MenuItem item);
}
