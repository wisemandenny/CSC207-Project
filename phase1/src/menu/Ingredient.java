package menu;

public interface Ingredient {
    /**
     * Returns the name of this menu.Ingredient.
     *
     * @return this menu.Ingredient's name
     */
    String getName();

    /**
     * Returns the price of this menu.Ingredient.
     *
     * @return the price of this menu.Ingredient
     */
    double getPrice();

    /**
     * Sets the price of this menu.Ingredient.
     *
     * @param price the new price of this menu.Ingredient
     */
    void setPrice(double price);

    /**
     * Returns if this menu.Ingredient has the same name as menu.Ingredient i.
     *
     * @param i the menu.Ingredient in question
     * @return a boolean that represents whether this menu.Ingredient has the same name as menu.Ingredient i
     */
    boolean sameAs(Ingredient i);

}
