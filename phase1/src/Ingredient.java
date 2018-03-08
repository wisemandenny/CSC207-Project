public interface Ingredient {
    /**
     * Returns the name of this ingredient.
     *
     * @return      this ingredient's name
     */
    String getName();

    /**
     * Returns the price of this ingredient.
     *
     * @return      the price of this ingredient.
     */
    double getPrice();

    /**
     * Sets the price of this Ingredient.
     *
     * @param price  The new price of this Ingredient.
     */
    void setPrice(double price);

    /**
     * Returns if this ingredient has the same name as ingredient i.
     *
     * @param i  the ingredient in question
     * @return      a boolean that represents whether this ingredient has the same name as ingredient i.
     */
    boolean sameAs(Ingredient i);

}
