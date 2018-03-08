public interface Ingredient {
    /**
     * Returns the name of this Ingredient.
     *
     * @return      this Ingredient's name
     */
    String getName();

    /**
     * Returns the price of this Ingredient.
     *
     * @return      the price of this Ingredient
     */
    double getPrice();

    /**
     * Sets the price of this Ingredient.
     *
     * @param price  the new price of this Ingredient
     */
    void setPrice(double price);

    /**
     * Returns if this Ingredient has the same name as Ingredient i.
     *
     * @param i  the Ingredient in question
     * @return      a boolean that represents whether this Ingredient has the same name as Ingredient i
     */
    boolean sameAs(Ingredient i);

}
