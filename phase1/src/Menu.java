public interface Menu {
    /**
     * Returns the entire Mnu as a list of MenuItems
     *
     * @return      the Menu
     */
    MenuItem[] getMenu();

    /**
     * Returns the MenuItem item from the menu if it exists.
     *
     * @param item  the MenuItem that is to be searched for on the Menu
     * @return      the MenuItem from the Menu with the same name
     */
    MenuItem getMenuItem(MenuItem item);

    /**
     * Returns the Ingredient query from the Ingredient menu if it exists.
     *
     * @param query  the Ingredient that is to be searched for on the Ingredient menu
     * @return      the Ingredient from the Ingredient menu with the same name
     */
    Ingredient getMenuIngredient(Ingredient query);
}
